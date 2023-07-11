async function registration() {
    try {
        const response = await fetch('/admin/registrationRest');
        const data = await response.json();
        document.getElementById('email').textContent = data.email;
        document.getElementById('role').textContent = roles(data);
    } catch (error) {
        console.error(error);
    }
}

let roles = function (data) {

    let rolesArray = data.roleSet.map(role => {
        if (role.roleUser.replace(/"/g, "") === 'ROLE_ADMIN') {
            return "ADMIN";
        } else {
            return "USER";
        }
    });
    let rolesString = rolesArray.join(", ");
    return `${rolesString}.`;
}
document.querySelector('.btn').addEventListener('click', function() {
    window.location.href = '/logout';
});
const link = document.getElementById('btnRegistration');
link.addEventListener('click', function(event) {
    event.preventDefault();
    const href = link.getAttribute('href');

    window.location.href = href;
});

async function userPost() {
    let role = [];
    if (Array.from(document.getElementById('rolesPost').options)
        .filter(option => option.selected)
        .map(option => option.value)[0] === "1") {
        role.push("ROLE_ADMIN");
    } else {
        role.push("ROLE_USER");
    }

    const addUser = {
        name: document.getElementById('namePost').value,
        username: document.getElementById('surnamePost').value,
        age: document.getElementById('agePost').value,
        email: document.getElementById('emailPost').value,
        pass: document.getElementById('passwordPost').value,
        roleSet: role
    };
    if (role.includes("ROLE_ADMIN")) {
        console.log(role);
    }
    console.log(addUser.roleSet);

    registrationUser(addUser);

    function registrationUser(addUser) {
        fetch(`/admin/registration`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(addUser)
        })
            .then(response => {

                if (response.ok) {
                    console.log('Пользователь создан');
                    redirectToNewPage();
                } else {
                    console.error('Ошибка создания пользователя');
                }
            })
            .catch(error => {
                console.error(error);
            });
    }
}
const putBtn = document.getElementById('putBtn');
putBtn.addEventListener('click', async (event) => {
    event.preventDefault();
    userPost();
});

function handleValidationErrors(errors) {
    for (const field of Object.keys(errors)) {
        const errorMessage = errors[field];
        // Отобразить сообщение об ошибке рядом с полем формы
        // или выполнить другую логику обработки ошибки
    }
}

function redirectToNewPage() {
    window.location.replace("/admin/AllUsers");
}
document.addEventListener("DOMContentLoaded", registration);
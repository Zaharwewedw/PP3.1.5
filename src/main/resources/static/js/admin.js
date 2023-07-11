async function getData() {

    try {
        const response = await fetch('/admin/AllUsersRest', {
            headers: {
                'Cache-Control': 'no-cache',
            },
        });
        const data = await response.json();
        document.getElementById('email').textContent = data[data.length - 1].email;
        document.getElementById('role').textContent = await rolesUser(data[data.length - 1]);

        await allUser(data);

    } catch (error) {
        console.error(error);
    }
}

let rolesUser = async function (user) {
    let rolesArray = user.roleSet.map(role => {
        if (role.roleUser.replace(/"/g, "") === 'ROLE_ADMIN') {
            return "ADMIN";
        } else {
            return "USER";
        }
    });
    let rolesString = rolesArray.join(", ");
    return `${rolesString}.`;
}

async function allUser(users) {

    let el = document.getElementById('users');
    let st = "";
    for (let i = 0; i < users.length - 1; i++ ) {
        let deleteButtonId = `deleteButton${users[i].id}`; // Создаем уникальный идентификатор для каждой кнопки "Delete"
        let editButtonId = `editButton${users[i].id}`;
        st += `
            <tr>
                <td>${users[i].id}</td>
                <td>${users[i].name}</td>
                <td>${users[i].username}</td>
                <td>${users[i].age}</td>
                <td>${users[i].email}</td>
                <td>${await rolesUser(users[i])}</td>
                <td>
                   <button id="${editButtonId}" type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" onclick="usersHtmlPut('${users[i].name}', '${users[i].username}', '${users[i].age}', '${users[i].email}', '${users[i].pass}'); editeUsersPutServer('${users[i].id}')">Edit</button>
                </td>
                <td>
                    <button id="${deleteButtonId}" type="button" class="btn btn-sm btn-danger" onclick="usersHtml('${users[i].id}', '${users[i].name}', '${users[i].username}', '${users[i].age}', '${users[i].email}'); deleteStatus('${users[i].id}');">Delete</button>
                </td>
            </tr>`;
        el.innerHTML = st;
    }
    for (let i = 0; i < users.length - 1; i++ ) {
        let editButtonId = `editButton${users[i].id}`;
        const deleteButton = document.getElementById(editButtonId);
        deleteButton.addEventListener("click", openModalPut);
    }

    for (let i = 0; i < users.length - 1; i++ ) {
        let deleteButtonId = `deleteButton${users[i].id}`;
        const deleteButton = document.getElementById(deleteButtonId);
        deleteButton.addEventListener("click", openModal);
    }

}
////////////////////////////////model edit windows open//////////////////////////////////
function usersHtmlPut(name, username, age, email, pass) {
    document.getElementById('namePut').value = name;
    document.getElementById('surnamePut').value = username;
    document.getElementById('agePut').value = age;
    document.getElementById('emailPut').value = email;
    document.getElementById('passwordPut').value = pass;
}
function openModalPut() {
    const modal = document.querySelector("#myModalPut");
    modal.style.display = "block";
}
const closeButtonPut = document.getElementById("closeButtonPut");
closeButtonPut.addEventListener("click", closeModalPut);

function closeModalPut() {
    const modal = document.querySelector("#myModalPut");
    modal.style.display = "none";
}

let closeButtons = document.querySelectorAll('[data-bs-dismiss="modal"]');
////////////////////////////////close windows model//////////////////////////////////
closeButtons.forEach(function(button) {
    button.addEventListener("click", function() {
        let modal = button.closest(".modal");
        modal.style.display = "none";
    });
});

////////////////////////////////model delete windows open//////////////////////////////////

function usersHtml(id, name, username, age, email) {
    document.getElementById('id').value = id;
    document.getElementById('name1').value = name;
    document.getElementById('surname1').value = username;
    document.getElementById('age1').value = age;
    document.getElementById('email1').value = email;
}

function deleteModel(id) {
    document.querySelector('#deleteBtn').addEventListener('click', function() {
        deleteUser(id);
        hideModal();
    });
}
function hideModal() {
    let modal = document.querySelector('.modal');
    modal.style.display = 'none';
}
function openModal() {
    const modal = document.querySelector("#myModal");
    modal.style.display = "block";
}

const closeButton = document.getElementById("closeButton");
closeButton.addEventListener("click", closeModal);

function closeModal() {
    const modal = document.querySelector("#myModal");
    modal.style.display = "none";
}
/////////////////////////////////////logout//////////////////////////////////////////////
document.querySelector('.btn').addEventListener('click', function() {
    window.location.href = '/logout';
});

/////////////////////////////////////edit////////////////////////////////////////////////
function putModel(id) {
    document.querySelector('#putBtn').addEventListener('click', function() {
        editeUsersPut(id);
        hideModal();
    });
}

async function editeUsersPut(id) {
    let role = [];
    if (Array.from(document.getElementById('roles').options)
        .filter(option => option.selected)
        .map(option => option.value)[0] === "1") {
        role.push("ROLE_ADMIN");
    } else {
        role.push("ROLE_USER");
    }

    const updatedUser = {
        id: id,
        name: document.getElementById('namePut').value,
        username: document.getElementById('surnamePut').value,
        age: document.getElementById('agePut').value,
        email: document.getElementById('emailPut').value,
        pass: document.getElementById('passwordPut').value,
        roleSet: role
    };

    if (role.includes("ROLE_ADMIN")) {
        console.log(role);
    }
    console.log(updatedUser.roleSet);

    await updateUser(updatedUser);

    async function updateUser(updatedUser) {
        try {
            const response = await fetch('/admin/update', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedUser),
            });

            if (response.ok) {
                // Пользователь успешно обновлен
                console.log('Пользователь обновлен');
                const response = await fetch('/admin/AllUsersRest');
                const data = await response.json();
                await allUser(data); // Обновить данные на фронтенде
            } else {
                // Обработка ошибки, если запрос не был успешным
                console.error('Ошибка обновления пользователя');
            }
        } catch (error) {
            console.error(error);
        }
    }


   }
    async function editeUsersPutServer(id) {
        const putBtn = document.getElementById('putBtn');
        putBtn.addEventListener('click', async (event) => {
            event.preventDefault();
            editeUsersPut(id);
            closeModalPut();
        });
    }

async function deleteUser(userId) {
    try {
        const response = await fetch(`/admin/delete/${userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            // Пользователь успешно удален
            console.log('Пользователь удален');
            const response = await fetch('/admin/AllUsersRest');
            const data = await response.json();
            await allUser(data);

        } else {
            // Обработка ошибки, если запрос не был успешным
            console.error('Ошибка удаления пользователя');
        }
    } catch (error) {
        console.error(error);
    }
}
function deleteStatus(id){
    const deleteBtn = document.getElementById('deleteBtn');
    deleteBtn.addEventListener('click', async (event) => {
        event.preventDefault();
        deleteUser(id);
        closeModal();
    });
}

getData();
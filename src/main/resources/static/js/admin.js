
async function getData() {
    try {
        const response = await fetch('/admin/AllUsersRest');
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

let updateButtonHandlers = [];
let deleteButtonHandlers = [];

async function allUser(users) {
    updateButtonHandlers.forEach(handler => {
        handler.button.removeEventListener("click", handler.eventHandler);
    });
    deleteButtonHandlers.forEach(handler => {
        handler.button.removeEventListener("click", handler.eventHandler);
    });

    updateButtonHandlers = [];
    deleteButtonHandlers = [];

    const el = document.getElementById('users');
    let st = "";
    for (let i = 0; i < users.length - 1; i++) {
        const deleteButtonId = `deleteButton${users[i].id}`;
        const editButtonId = `editButton${users[i].id}`;
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
          <button id="${deleteButtonId}" type="button" class="btn btn-sm btn-danger" onclick="usersHtml('${users[i].id}', '${users[i].name}', '${users[i].username}', '${users[i].age}', '${users[i].email}'); deleteStatus('${users[i].id}')">Delete</button>
        </td>
      </tr>`;
    }
    el.innerHTML = st;

    for (let i = 0; i < users.length - 1; i++) {
        const updateButtonId = `editButton${users[i].id}`;
        const updateButton = document.getElementById(updateButtonId);
        const updateEventHandler = openModalPut.bind(null, users[i].id);
        updateButton.addEventListener("click", updateEventHandler);

        updateButtonHandlers.push({ button: updateButton, eventHandler: updateEventHandler });
    }

    for (let i = 0; i < users.length - 1; i++) {
        const deleteButtonId = `deleteButton${users[i].id}`;
        const deleteButton = document.getElementById(deleteButtonId);
        const deleteEventHandler = openModal.bind(null, users[i].id);
        deleteButton.addEventListener("click", deleteEventHandler);

        deleteButtonHandlers.push({ button: deleteButton, eventHandler: deleteEventHandler });
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

function userEmpty(){document.getElementById('errorEmail').textContent = "";
    document.getElementById('errorAge').textContent =  "";
    document.getElementById('errorName').textContent =  "";
    document.getElementById('errorUsername').textContent =  "";
    document.getElementById('errorPassword').textContent =  "";}

function closeModalPut() {
    const modal = document.querySelector("#myModalPut");
    userEmpty();
    modal.style.display = "none";
}

let closeButtons = document.querySelectorAll('[data-bs-dismiss="modal"]');
////////////////////////////////close windows model//////////////////////////////////
closeButtons.forEach(function(button) {
    button.addEventListener("click", function() {
        let modal = button.closest(".modal");
        userEmpty();
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
    userEmpty();
    modal.style.display = "none";
}
/////////////////////////////////////logout//////////////////////////////////////////////
document.querySelector('.btn').addEventListener('click', function() {
    window.location.href = '/logout';
});

/////////////////////////////////////edit////////////////////////////////////////////////
async function editeUsersPut(id) {
    console.log(id);
    let cnt = 0;
    console.log(cnt);
    cnt++;
    let role = [];
    if (Array.from(document.getElementById('roles').options)
        .filter(option => option.selected)
        .map(option => option.value)[0] === "1") {
        role.push("ROLE_ADMIN");
    } else {
        role.push("ROLE_USER");
    }

    let updatedUser = {
        id: id,
        name: document.getElementById('namePut').value,
        username: document.getElementById('surnamePut').value,
        age: document.getElementById('agePut').value,
        email: document.getElementById('emailPut').value,
        pass: document.getElementById('passwordPut').value,
        roleSet: role
    };
    console.log(updatedUser);
    fetch(`/admin/update`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedUser),
    })
        .then(async response => {
            if (response.ok) {
                await getData();
            } else if (response.status >= 400) {
                const errorMessage = await response.json();
                errorFieldUserPut(errorMessage);
                openModalPut();
            } else {
                console.error('Ошибка создания пользователя');
            }
        })
        .catch(error => {
            console.error(error);
        });
}

function errorFieldUserPut(err) {
    for (let i = 0; i < err.length; i++) {
        let a = err[i].split(":");
        switch (a[0]) {
            case "email" :  document.getElementById('errorEmail').textContent = a[1]; break;
            case "age" :  document.getElementById('errorAge').textContent = a[1]; break;
            case "name" :  document.getElementById('errorName').textContent = a[1]; break;
            case "username" :  document.getElementById('errorUsername').textContent = a[1]; break;
            case "password" :  document.getElementById('errorPassword').textContent = a[1]; break;
        }
    }
}
async function editeUsersPutServer(id) {
    const putBtn = document.getElementById('putBtn');
    putBtn.addEventListener('click', async (event) => {
        event.preventDefault();
        await editeUsersPut(id);
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
        await deleteUser(id);
        closeModal();
    });
}
document.addEventListener("DOMContentLoaded", getData);
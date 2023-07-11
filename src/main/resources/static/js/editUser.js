fetch(`/user`)
    .then(response => response.json())
    .then(data => {

        let roles = function () {
            try {
                let rolesArray = data.principal.roleSet.map(role => {
                    if (role.roleUser.replace(/"/g, "") === 'ROLE_ADMIN') {
                        return "ADMIN";
                    } else {
                        return "USER";
                    }
                });
                let rolesString = rolesArray.join(", ");
                return `${rolesString}.`;
            } catch (e) {
                let rolesUser = function () {
                    let rolesArray = data.user.roleSet.map(role => {
                        if (role.roleUser.replace(/"/g, "") === 'ROLE_ADMIN') {
                            return "ADMIN";
                        } else {
                            return "USER";
                        }
                    });
                    let rolesString = rolesArray.join(", ");
                    return `${rolesString}.`;
                }
            }

        }
        let rolesUser = function () {
            let rolesArray = data.user.roleSet.map(role => {
                if (role.roleUser.replace(/"/g, "") === 'ROLE_ADMIN') {
                    return "ADMIN";
                } else {
                    return "USER";
                }
            });
            let rolesString = rolesArray.join(", ");
            return `${rolesString}.`;
        }

        try {
            document.getElementById('jsonContainer').textContent = JSON.stringify(data.principal.email).replace(/"/g, "");
        } catch (e) {
            document.getElementById('jsonContainer').textContent = JSON.stringify(data.user.email).replace(/"/g, "");
        }

        document.getElementById('rolesPrincipal').textContent = JSON.stringify(roles()).replace(/"/g, "");


        let element = document.getElementById("userPanelBody");
        element.innerHTML = `
            <td>${data.user.id}</td>
            <td>${data.user.name}</td>
            <td>${data.user.username}</td>
            <td>${data.user.age}</td>
            <td>${data.user.email}</td>  
            <td>${rolesUser()}</td>  
        `;

        let currentUserRole = data.principal.admin.replace(/"/g, "");
        console.log(currentUserRole);
        if (currentUserRole === 'ACTIVE') {
            let adminElements = document.querySelectorAll('.admin-only');
            Array.from(adminElements).forEach(function(element) {
                element.style.display = 'block';
            });
            let userElements = document.querySelectorAll('.user-only');
            Array.from(userElements).forEach(function(element) {
                element.style.display = 'block';
            });
        } else {
            let userElements = document.querySelectorAll('.user-only');
            Array.from(userElements).forEach(function(element) {
                element.style.display = 'block';
            });
            let adminElements = document.querySelectorAll('.admin-only');
            Array.from(adminElements).forEach(function(element) {
                element.style.display = 'none';
            });
        }

    });
document.querySelector('.btn').addEventListener('click', function() {
    window.location.href = '/logout';
});
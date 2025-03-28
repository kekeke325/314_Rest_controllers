const BASE_URL = 'http://localhost:8080/api'

const modalEditUser = document.getElementById('edit-user-modal')
const modalDeleteUser = document.getElementById('delete-user-modal')

const deleteUserForm = document.getElementById('delete-user-form');
const editUserForm = document.getElementById('edit-user-form');
const addNewUserForm = document.getElementById('add-new-user-form')

refreshTable()

function refreshTable() {
    fetch(BASE_URL + '/users')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            fillTableWithData(data);
            console.log('Table refreshed successfully');
        })
        .catch(error => {
            console.error('Failed to load user data', error);
            showAlert('Failed to load user data. Please try again.', 'error');
        });
}

function fillTableWithData(data) {
    const table = document.getElementById('userTable')
    const tbody = table.querySelector('tbody');

    tbody.innerHTML = ''

    data.forEach(user => {
        const row = tbody.insertRow();

        row.insertCell().textContent = user.id;
        row.insertCell().textContent = user.name;
        row.insertCell().textContent = user.lastName;  // Added lastName field
        row.insertCell().textContent = user.age;
        row.insertCell().textContent = user.email;

        const roles = user.roles.map(role => role.name.split('_')[1])
        row.insertCell().textContent = roles.join(' ');

        const editButton = document.createElement('button');
        editButton.textContent = 'Edit';
        editButton.classList.add('btn')
        editButton.classList.add('btn-info')
        editButton.addEventListener('click', event => {showEditForm(event);});
        row.insertCell().appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Delete';
        deleteButton.classList.add('btn')
        deleteButton.classList.add('btn-danger')
        deleteButton.addEventListener('click', event => {showDeleteForm(event);});
        row.insertCell().appendChild(deleteButton);
    });
}

function showDeleteForm (event) {
    let modal = new bootstrap.Modal(modalDeleteUser);
    const button = event.currentTarget
    const tableRow = button.parentNode.parentNode;

    const userId = document.getElementById('delete-user-id')
    const userName = document.getElementById('delete-user-name')
    const userLastName = document.getElementById('delete-user-lastName')
    const userAge = document.getElementById('delete-user-age')
    const userEmail = document.getElementById('delete-user-email')

    userId.value = tableRow.querySelector('td:nth-child(1)').textContent;
    userName.value = tableRow.querySelector('td:nth-child(2)').textContent;
    userLastName.value = tableRow.querySelector('td:nth-child(3)').textContent;
    userAge.value = tableRow.querySelector('td:nth-child(4)').textContent;
    userEmail.value = tableRow.querySelector('td:nth-child(5)').textContent;
    modal.show()
}

function showEditForm (event) {
    let modal = new bootstrap.Modal(modalEditUser);
    const button = event.currentTarget
    const tableRow = button.parentNode.parentNode;

    const userId = document.getElementById('edited-user-id')
    const userName = document.getElementById('edited-user-name')
    const userLastName = document.getElementById('edited-user-lastName')
    const userAge = document.getElementById('edited-user-age')
    const userEmail = document.getElementById('edited-user-email')

    userId.value = tableRow.querySelector('td:nth-child(1)').textContent;
    userName.value = tableRow.querySelector('td:nth-child(2)').textContent;
    userLastName.value = tableRow.querySelector('td:nth-child(3)').textContent;
    userAge.value = tableRow.querySelector('td:nth-child(4)').textContent;
    userEmail.value = tableRow.querySelector('td:nth-child(5)').textContent;
    modal.show()
}

deleteUserForm.addEventListener('submit', event => {
    event.preventDefault();

    const userId = document.getElementById('delete-user-id').value

    const URL = BASE_URL + '/users/' + userId;
    fetch(URL, {method: 'DELETE', credentials: 'include'})
        .then(response => {
            if (response.ok) {
                refreshTable()
                showAlert(`User with ID ${userId} was successfully deleted`, 'success')
            } else {
                showAlert(`Error while deleting user with ID ${userId}`, 'error')
            }
        })
        .catch(error => {
            console.log('Error while deleting user', error)
            showAlert(`Error: ${error.message}`, 'error');
        })

    let modal = bootstrap.Modal.getInstance(modalDeleteUser);
    modal.hide()
});

editUserForm.addEventListener('submit', event => {
    event.preventDefault();

    const email = document.getElementById('edited-user-email').value;
    const userData = {
        id: document.getElementById('edited-user-id').value,
        email: email,
        password: document.getElementById('edited-user-password').value,
        name: document.getElementById('edited-user-name').value,
        lastName: document.getElementById('edited-user-lastName').value,  // Added lastName field
        age: document.getElementById('edited-user-age').value,
        roles: Array.from(document.getElementById('role-select-edit-user').selectedOptions)
            .map(option => ({
                id: option.value,
                name: 'ROLE_' + option.text
            }))
    }

    const options = {
        method: 'PATCH',
        headers: {
            'content-type': 'application/json'
        },
        body: JSON.stringify(userData)
    }

    const URL = BASE_URL + '/users';

    fetch(URL, options)
        .then(response => {
            if (response.ok) {
                showAlert(`The user was edited successfully`, 'success')
                refreshTable()
            } else {
                showAlert('Error while editing user', 'error')
            }
        })
        .catch(error => {
            console.log('Error while editing user', error)
            showAlert(`Error: ${error.message}`, 'error');
        })

    let modal = bootstrap.Modal.getInstance(modalEditUser);
    modal.hide()
});

addNewUserForm.addEventListener('submit', event => {
    event.preventDefault();

    const email = document.getElementById('new-user-email').value;
    const userData = {
        email: email,
        password: document.getElementById('new-user-password').value,
        name: document.getElementById('new-user-name').value,
        lastName: document.getElementById('new-user-lastName').value,  // Added lastName field
        age: document.getElementById('new-user-age').value,
        roles: Array.from(document.getElementById("role-select").selectedOptions)
            .map(option => ({
                id: option.value,
                name: 'ROLE_' + option.text
            }))
    }

    const options = {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        },
        body: JSON.stringify(userData)
    }

    const URL = BASE_URL + '/users';

    fetch(URL, options)
        .then(response => {
            if (response.ok) {
                showAlert(`The user with email '${email}' was added successfully`, 'success')
                refreshTable()
                clearAddUserForm();
                const tabUserList = document.getElementById('tab-users-list')
                tabUserList.click()
            } else {
                    showAlert('Error while adding new user', 'error')
            }
        })
        .catch(error => {
            console.log('Error while adding new user', error)
            showAlert(`Error: ${error.message}`, 'error');

        })
});

function clearAddUserForm() {
    const form = document.getElementById('add-new-user-form');
    form.reset();

    const roleSelect = document.getElementById('role-select');
    roleSelect.selectedIndex = -1;
}

function showAlert(message, alertType = 'error') {
    let alertContainer = document.getElementById('alert-container');
    if (!alertContainer) {
        alertContainer = document.createElement('div');
        alertContainer.id = 'alert-container';
        alertContainer.style.position = 'fixed';
        alertContainer.style.top = '20px';
        alertContainer.style.left = '50%';
        alertContainer.style.transform = 'translateX(-50%)';
        alertContainer.style.zIndex = '1000';
        document.body.appendChild(alertContainer);
    }

    const alertElement = document.createElement('div');
    alertElement.textContent = message;
    alertElement.style.padding = '10px 20px';
    alertElement.style.margin = '10px 0';
    alertElement.style.borderRadius = '4px';
    alertElement.style.color = '#fff';

    if (alertType === 'error') {
        alertElement.style.backgroundColor = '#dc3545';
    } else if (alertType === 'success') {
        alertElement.style.backgroundColor = '#28a745';
    } else {
        alertElement.style.backgroundColor = '#17a2b8';
    }

    alertContainer.appendChild(alertElement);

    setTimeout(() => {
        alertElement.remove();
        if (alertContainer.children.length === 0) {
            alertContainer.remove();
        }
    }, 2000);
}
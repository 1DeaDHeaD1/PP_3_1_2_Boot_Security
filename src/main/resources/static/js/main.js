var usersTable;

$( document ).ready( function () {

    $( 'a' ).each( function () {
        if( $( this ).parent( 'div' ).hasClass('navbar-side') && ((document.location.pathname === ($( this ).attr( 'href' ))) || (document.location.pathname.indexOf($( this ).attr( 'href' )+'/') != -1)) ) {
            $( this ).parent( 'div' ).addClass( 'active' );
        }
    });

    usersTable = $('#usersList').DataTable({
        "columns": [
            { "data": "id" },
            { "data": "firstName" },
            { "data": "lastName" },
            { "data": "email" },
            { "data": "username" },
            { "data": "password" },
            { "data": "enabled",
                "render": function (data, type, row, meta) {
                if (typeof data == 'string') {
                    return data;
                }
                return  (data == true) ? '<input type="checkbox" disabled="disabled" checked="checked" ' + data +'</input>': '<input type="checkbox" disabled="disabled" '+ data+'</input>';
                }
            },
            { "data": "roles",
                "render": function (data, type, row, meta) {
                if (Array.isArray(data)) {
                     return data.map(d => d.authority).join(" ")
                }
                return data;
                }
            },
            null,
            null
        ],
        "columnDefs": [ {
            "targets": 5,
            'visible': false
        }, {
            "targets": -2,
            defaultContent: '<button class="btn btn-primary" id="editUserButton" data-bs-toggle="modal" data-bs-target="#userInfoModal">Edit</button>',
        }, {
            "targets": -1,
            defaultContent: '<button class="btn btn-danger" id="deleteUserButton" data-bs-toggle="modal" data-bs-target="#userInfoModal">Delete</button>'
        } ]
    });
    $('#saveModalButton').click(handleFormSubmit);
    $('#deleteModalButton').click(handleFormDelete);
    $('#createUserButton').click(handleCreateUser);
});

// Prefill modal data
$(function() {
    //Take the data from the TR during the event button
    $('table#usersList').on('click', '#editUserButton',function (ele) {
        var id = cloneData(ele);
        $('#userInfoModalLabel').html('Edit user: ');
        $("#userInfoForm").attr('action', '/admin/users/edit/'+id);
        $("#userInfoForm fieldset").attr('disabled', false);


        $('#saveModalButton').prop('disabled', false);
        $('#saveModalButton').prop('hidden', false);
        $('#deleteModalButton').prop('disabled', true);
        $('#deleteModalButton').prop('hidden', true);
        $('#saveModalButton').html('Save');
    });
    $('table#usersList').on('click', '#deleteUserButton',function (ele) {
        var id = cloneData(ele);
        $('#userInfoModalLabel').html('Delete user: ');
        $("#userInfoForm").attr('action', '/admin/users/delete/'+id);
        $("#userInfoForm fieldset").attr('disabled', true);


        $('#saveModalButton').prop('disabled', true);
        $('#saveModalButton').prop('hidden', true);
        $('#deleteModalButton').prop('disabled', false);
        $('#deleteModalButton').prop('hidden', false);
        $('#deleteModalButton').html('Delete');
    });
});

function cloneData(table) {
    var tr = table.target.closest('tr');
    //I get the value from the cells (td) using the parentNode (var tr)
    var id = tr.cells[0].textContent;
    var firstName = tr.cells[1].textContent;
    var lastName = tr.cells[2].textContent;
    var email = tr.cells[3].textContent;
    var userName = tr.cells[4].textContent;
    var password = tr.cells[5].textContent;
    var enabled = $(tr.cells[6]).find('input').prop('checked');
    var roles = $(tr.cells[6]);
    console.log(roles);
    //Prefill the fields with the gathered information
    $('#editId').val(id);
    $('#editFirstname').val(firstName);
    $('#editLastname').val(lastName);
    $('#editEmail').val(email);
    $('#editUsername').val(userName);
    $('#editPassword').val(password);
    $('#editEnabled').attr('checked',enabled);
    return id;
}

async function handleFormSubmit(event) {
    event.preventDefault();
    var obj = $(event.target).parents('form#userInfoForm').serializeJSON();
    obj.roles = getRoles(($(event.target)).parents('form#userInfoForm').find('#editRoles'));
    obj.enabled = getEnabled(($(event.target)).parents('form#userInfoForm').find('#editEnabled'))
    var data = JSON.stringify(obj);
    var response = await sendData('/api/admin/users/edit/'+obj.id, 'put', data);

    fetchUsers();
}

async function handleFormDelete(event) {
    event.preventDefault();
    var form = $(event.target).parents('form#userInfoForm');
    form.children('fieldset').prop('disabled', false);
    var obj = form.serializeJSON();
    form.children('fieldset').prop('disabled', true);
    var data = JSON.stringify(obj);
    var response = await sendData('/api/admin/users/'+obj.id, 'delete', data);

    fetchUsers();
}

async function handleCreateUser(event) {
    event.preventDefault();
    var form = $(event.target).parents('form');
    var obj = form.serializeJSON();
    var data = JSON.stringify(obj);
    var response = await sendData('/api/admin/users/'+obj.id, 'post', data);

    if (response.status != 200) {
        alert("Ошибка создания!");
        return;
    }
    fetchUsers();
}


async function sendData(url, sMethod, data) {
    return await fetch(url, {
        method: sMethod,
        headers: { 'Content-Type' : 'application/json' },
        body: data,
    })
}

async function fetchUsers() {
    var userListResp = await fetch('/api/admin/users');
    var userList = await userListResp.json();

    usersTable.clear();
    usersTable.rows.add(userList);
    usersTable.draw();
}

function getRoles(multyOption) {
    return multyOption.children(' :selected').map((_, e) => e.value).get();
}

function getEnabled(checkbox) {
    return (checkbox.prop('checked')) ? true : false;
}
$( document ).ready( function () {

    $( 'a' ).each( function () {
        if( $( this ).parent( 'div' ).hasClass('navbar-side') && ((document.location.pathname === ($( this ).attr( 'href' ))) || (document.location.pathname.indexOf($( this ).attr( 'href' )+'/') != -1)) ) {
            $( this ).parent( 'div' ).addClass( 'active' );
        }
    });
});

$(function() {
    //Take the data from the TR during the event button
    $('table#usersList').on('click', '#editUserButton',function (ele) {
        var id = cloneData(ele);
        $('#userInfoModalLabel').html('Edit user: ');
        $("#userInfoForm").attr('action', '/admin/users/edit/'+id);
        $("#userInfoForm fieldset").attr('disabled', false);
        $('#saveModalButton').removeClass('btn-danger');
        $('#saveModalButton').addClass('btn-primary');
        $('#saveModalButton').html('Save');
    });
    $('table#usersList').on('click', '#deleteUserButton',function (ele) {
        var id = cloneData(ele);
        $('#userInfoModalLabel').html('Delete user: ');
        $("#userInfoForm").attr('action', '/admin/users/delete/'+id);
        $("#userInfoForm fieldset").attr('disabled', true);
        $('#saveModalButton').addClass('btn-danger');
        $('#saveModalButton').removeClass('btn-primary');
        $('#saveModalButton').html('Delete');
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
    var enabled = $(tr.cells[6]).find('input').attr('checked');

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
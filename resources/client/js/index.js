"use strict";

function renderMessage(message) {
    return `<div class="border border-primary p-2 m-2">` +
        `<div>` +
        `<span class="badge badge-primary mr-2">${message.author}</span>` +
        `<span class="badge badge-info">${message.postDate}</span>` +
        `<div class="float-right">` +
        `<button class="editMessage btn btn-sm btn-secondary ml-2" data-message-id="${message.id}">` +
            `Edit` +
        `</button>` +
        `<button class="saveMessage btn btn-sm btn-success ml-2" data-message-id="${message.id}">` +
            `Save` +
        `</button>` +
        `<button class="cancelEditMessage btn btn-sm btn-warning ml-2" data-message-id="${message.id}">` +
            `Cancel` +
        `</button>` +
        `<button class="deleteMessage btn btn-sm btn-danger ml-2" data-message-id="${message.id}">` +
            `Delete` +
        `</button>` +
        `</div>` +
        `</div>` +
        `<div class="messageText py-2 mx-2" id="text${message.id}">${message.text}</div>` +
        `<input class="messageEditInput w-100 form-control" id="editInput${message.id}">` +
        `</div>`;
}


function loadMessages() {
    let messagesHTML = '';
    $.ajax({
        url: '/message/list',
        type: 'GET',
        success: messageList => {
            if (messageList.hasOwnProperty('error')) {
                alert(messageList.error);
            } else {
                for (let message of messageList) {
                    messagesHTML += renderMessage(message);
                }
                $('#messages').html(    messagesHTML);
                resetDeleteButtons();
                resetEditButtons();
                cancelEdits();
            }
        }
    });
}

function saveEdit(event) {

    const messageId = $(event.target).attr('data-message-id');
    const editInput = $("#editInput" + messageId);

    $.ajax({
        url: '/message/edit',
        type: 'POST',
        data: {"messageId": messageId, "messageText": editInput.val()},
        success: response => {
            if (response === 'OK') {
                pageLoad();
            } else {
                alert(response);
            }
        }
    })

}

function cancelEdits() {

    $(".messageEditInput").hide();
    $(".cancelEditMessage").hide();
    $(".saveMessage").hide();

    $(".messageText").show();
    $(".editMessage").show();
    $(".deleteMessage").show();
}

function resetDeleteButtons() {

    $('.deleteMessage').click(event => {

        const messageId = $(event.target).attr('data-message-id');

        $.ajax({
            url: '/message/delete',
            type: 'POST',
            data: {"messageId": messageId},
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        });

    });
}

function resetEditButtons() {
    $('.editMessage').click(event => {

        cancelEdits();

        const editButton = $(event.target);
        const saveButton = editButton.next();
        const cancelButton = saveButton.next();
        const deleteButton = cancelButton.next();
        const messageId = editButton.attr("data-message-id");
        const textDiv = $("#text" + messageId);
        const currentText = textDiv.text();
        const editInput = textDiv.next();

        editButton.hide();
        saveButton.show();
        saveButton.click(event => saveEdit(event));
        cancelButton.show();
        cancelButton.click(event => cancelEdits());
        deleteButton.hide();
        editInput.val(currentText);
        editInput.show().focus().select();
        textDiv.hide();

    });
}



function resetForm() {
    const form = $('#messageForm');
    form.unbind("submit");
    form.submit(event => {
        event.preventDefault();
        $.ajax({
            url: '/message/new',
            type: 'POST',
            data: form.serialize(),
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        });
    });
}

function checkLogin() {

    let token = Cookies.get("sessionToken");

    if (token === undefined) {
        window.location.href = "/client/login.html";
    } else {
        $.ajax({
            url: '/user/get',
            type: 'GET',
            success: username => {
                if (username === "") {
                    window.location.href = "/client/login.html";
                } else {
                    $("#username").html(username);
                }
            }
        });
    }

    $("#logout").click(event => {
        Cookies.remove("sessionToken");
    window.location.href = "/client/login.html";
});
}



function pageLoad() {

    checkLogin();
    loadMessages();
    resetForm();

}

"use strict";

function renderMessage(message) {
    return `<div class="border border-primary p-2 m-2">` +
        `<div>` +
        `<span class="badge badge-primary mr-2">${message.author}</span>` +
        `<span class="badge badge-info">${message.postDate}</span>` +
        `<div id="messageButtons${message.id}" class="float-right">` +
            `<button class="editMessage btn btn-sm btn-secondary" data-message-id="${message.id}">` +
                `Edit` +
            `</button>` +
            `<button class="deleteMessage btn btn-sm btn-danger ml-2" data-message-id="${message.id}">` +
                `Delete` +
            `</button>` +
        `</div>` +
        `</div>` +
        `<div class="messageText py-2 mx-2" id="text${message.id}">${message.text}</div>` +
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
                $('#messages').html(messagesHTML);
                resetDeleteButtons();
                resetEditButtons();
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
        const deleteButton = editButton.next();

        const messageId = editButton.attr("data-message-id");
        const messageButtons = $("#messageButtons" + messageId);

        const textDiv = $("#text" + messageId);
        const currentText = textDiv.text();

        editButton.hide();
        deleteButton.hide();

        const editInput = $(`<input class="messageEditInput w-100 form-control"` +
            `id="editInput${messageId}" value="${currentText}"/>`);
        textDiv.after(editInput);
        editInput.focus().select();
        textDiv.hide();

        const saveButton = $(`<button class="saveMessage btn btn-sm btn-success float-right ml-2"` +
            `data-message-id="${messageId}">Save</button>`);
        saveButton.click(event => saveEdit(event));
        messageButtons.append(saveButton);

        const cancelButton = $(`<button class="cancelEditMessage btn btn-sm btn-warning float-right ml-2"` +
            `data-message-id="${messageId}">Cancel</button>`);
        cancelButton.click(event => cancelEdits());
        messageButtons.append(cancelButton);

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

function pageLoad() {

    loadMessages();
    resetForm();

}

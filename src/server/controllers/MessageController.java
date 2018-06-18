package server.controllers;

import org.json.simple.JSONArray;
import server.models.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("message/")
public class MessageController {

    @SuppressWarnings("unchecked")
    private String getMessageList() {
        JSONArray messageList = new JSONArray();
        for (Message m: Message.messages) {
            messageList.add(m.toJSON());
        }
        return messageList.toString();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listMessages() {
        return getMessageList();
    }

    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String newMessage(@FormParam("messageText") String messageText,
                             @FormParam("messageAuthor") String messageAuthor ) {

        int messageId = Message.nextId();
        String messageDate = new Date().toString();

        Message.messages.add(new Message(messageId, messageText, messageDate, messageAuthor));

        return getMessageList();


    }
}

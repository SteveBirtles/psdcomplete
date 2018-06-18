package server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Console;
import server.models.Message;
import server.models.services.MessageService;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
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
    public String listMessages(@CookieParam("sessionToken") Cookie sessionCookie) {

        if (UserService.validateSessionCookie(sessionCookie) == null) {
            return "Error: Invalid user session token";
        }

        Console.log("/message/list - Getting all messages from database");
        String status = MessageService.selectAllInto(Message.messages);
        if (status.equals("OK")) {
            return getMessageList();
        } else {
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }

    }

    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String newMessage(@FormParam("messageText") String messageText,
                             @CookieParam("sessionToken") Cookie sessionCookie) {

        String currentUsername = UserService.validateSessionCookie(sessionCookie);
        if (currentUsername == null) return "Error: Invalid user session token";

        Console.log("/message/new - Posted by " + currentUsername);

        MessageService.selectAllInto(Message.messages);

        int messageId = Message.nextId();
        String messageDate = new Date().toString();

        Message newMessage = new Message(messageId, messageText, messageDate, currentUsername);
        return MessageService.insert(newMessage);
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteMessage(@FormParam("messageId") int messageId,
                                @CookieParam("sessionToken") Cookie sessionCookie) {

        String currentUsername = UserService.validateSessionCookie(sessionCookie);
        if (currentUsername == null) return "Error: Invalid user session token";

        Console.log("/message/delete - Message " + messageId);
        Message message = MessageService.selectById(messageId);
        if (message == null) {
            return "That message doesn't appear to exist";
        } else {
            if (!message.getAuthor().equals(currentUsername)) {
                return "That message doesn't belong to you!";
            }
            return MessageService.deleteById(messageId);
        }
    }

    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String editMessage(@FormParam("messageId") int messageId,
                              @FormParam("messageText") String messageText,
                              @CookieParam("sessionToken") Cookie sessionCookie) {

        String currentUsername = UserService.validateSessionCookie(sessionCookie);
        if (currentUsername == null) return "Error: Invalid user session token";
        Console.log("/message/edit - Message " + messageId);
        Message message = MessageService.selectById(messageId);
        String messageDate = new Date().toString();
        if (message == null) {
            return "That message doesn't appear to exist";
        } else {
            if (!message.getAuthor().equals(currentUsername)) {
                return "That message doesn't belong to you!";
            }
            message.setText(messageText);
            message.setPostDate(messageDate);
            return MessageService.update(message);
        }
    }


}

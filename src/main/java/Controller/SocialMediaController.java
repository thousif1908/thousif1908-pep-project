package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import Model.Account;
import Model.Message;
import DAO.SocialMediaDAO;
import Service.SocialMediaService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final SocialMediaService service;

    public SocialMediaController()
    {
        this.service = new SocialMediaService(new SocialMediaDAO());
    }
    private void handleRegister(Context ctx)
    {
        Account account = ctx.bodyAsClass(Account.class);
        Account created = service.register(account);
        if(created != null)
        {
            ctx.json(created);
        } else {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }
    public void handleLogin(Context ctx)
    {
        Account credentials = ctx.bodyAsClass(Account.class);
        Account existing = service.login(credentials);
        if(existing != null)
        {
            ctx.json(existing);
        } else {
            ctx.status(HttpStatus.UNAUTHORIZED);
        }
    }
    public void handlePostMessage(Context ctx)
    {
        Message message = ctx.bodyAsClass(Message.class);
        Message created = service.postMessage(message);
        if(created != null){
            ctx.json(created);
        } else
        {
           ctx.status(HttpStatus.BAD_REQUEST); 
        }
    }
    private void handleGetAllMessages(Context ctx)
    {
        List<Message> messages = service.getAllMessages();
        ctx.json(messages);
    }
    public void handleGetMessageById(Context ctx)
    {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = service.getMessageById(id);
        if(message != null)
        {
            ctx.json(message);
        } else 
        {
            ctx.status(200);
        }
    }
    public void handleDeleteMessage(Context ctx)
    {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message deleted = service.deleteMessage(id);
        if(deleted != null)
        {
            ctx.json(deleted);
        } else {
            ctx.status(200);
        }
    }
    public void handleUpdateMessage(Context ctx)
    {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message incoming = ctx.bodyAsClass(Message.class);
        Message updated = service.updateMessage(id, incoming.getMessage_text());
        if(updated != null)
        {
            ctx.json(updated);
        } else{
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    } 

    public void handleGetMessagesByUser(Context ctx)
    {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = service.getMessagesByUser(accountId);
        ctx.json(messages);
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
        app.post("/messages", this::handlePostMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context)
    {
        context.json("sample text");
    }

}
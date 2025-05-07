package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;
import java.util.List;

public class SocialMediaService
{
    private  SocialMediaDAO dao;

    public SocialMediaService(SocialMediaDAO dao)
    {
        this.dao = dao;
    }

    public Account register(Account account) 
    {
        if(account.getUsername() == null || account.getUsername().isBlank()) return null;
        if(account.getPassword() == null || account.getPassword().length() < 4) return null;
        if(dao.isUsernameTaken(account.getUsername())) return null;
        return dao.createAccount(account);
    }

    public Account login(Account credentials)    
    {
        return dao.validateLogin(credentials.getUsername(), credentials.getPassword());
    }

    public Message postMessage(Message message) 
    {
        if(message.getMessage_text() == null || message.getMessage_text().isBlank()) return null;
        if(message.getMessage_text().length() > 255) return null;
        if(!dao.doesUserExist(message.getPosted_by())) return null;
        return dao.createMessage(message);
    }
    

    public List<Message> getAllMessages()
    {
        return dao.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return dao.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId)
    {
        return dao.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, String newText)
    {
        if(newText == null || newText.trim().isEmpty() || newText.length() > 255)
        {
            return null;
        }
        Message existingMessage = dao.getMessageById(messageId);
        if(existingMessage == null)
        {
            return null;
        }
        boolean updated = dao.updateMessageText(messageId, newText) != null;
        if(updated)
        {
            return dao.getMessageById(messageId);
        }
        return null;
    }

    public List<Message> getMessagesByUser(int accountId)
    {
        return dao.getMessagesByUserId(accountId);
    }
}
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adria
 */
public class Conversation {
    volatile List<String> conversation = new ArrayList<>();
    
    
    public List<String> getMessages() {
        return conversation;
    }
    
    public void storeMsg(String msg) {
        conversation.add(msg);
    }
}

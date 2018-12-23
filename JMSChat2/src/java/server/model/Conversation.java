package server.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    volatile List<String> conversation = new ArrayList<>();
    
    
    public List<String> getMessages() {
        return conversation;
    }
    
    public void storeMsg(String msg) {
        conversation.add(msg);
    }
}

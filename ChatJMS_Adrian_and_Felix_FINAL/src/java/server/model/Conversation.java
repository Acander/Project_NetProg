package server.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private List<String> conversation = new ArrayList<>();
    
    
    public synchronized List<String> getMessages() {
        return conversation;
    }
    
    public synchronized void storeMsg(String msg) {
        conversation.add(msg);
    }
}

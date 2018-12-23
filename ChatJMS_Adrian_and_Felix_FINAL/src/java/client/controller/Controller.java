package client.controller;

import client.net.ChatConnection;
import client.net.OutputHandler;
import client.net.ConnectionException;
import java.util.concurrent.CompletableFuture;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

public class Controller {
    
        private final ChatConnection chatConnection;
        
        public Controller(ConnectionFactory cf, TopicConnectionFactory tcf, Queue clientQueue, Topic topic){
            this.chatConnection = new ChatConnection(cf, tcf, clientQueue, topic);
        }

        public void joinChat(OutputHandler outputHandler, String username) throws ConnectionException{          
            chatConnection.joinChat(outputHandler, username);
        }
        
        public void leaveChat() throws Exception{
               chatConnection.leaveChat();
        }
        
        public void sendMessage(String message){
           CompletableFuture.runAsync(() -> {
                chatConnection.sendMessage(message);
         });
    }

}
        

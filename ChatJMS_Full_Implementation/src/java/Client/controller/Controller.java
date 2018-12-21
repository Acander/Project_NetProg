package Client.controller;

import Client.net.ChatConnection;
import Client.net.OutputHandler;
import java.util.concurrent.CompletableFuture;
import javax.jms.JMSException;

public class Controller {
    
        private final ChatConnection chatConnection = new ChatConnection();

        public void joinChat(OutputHandler outputHandler) {
            CompletableFuture.runAsync(() -> {
                try {
                    chatConnection.joinChat(outputHandler);
                } catch (JMSException ex) {
                    ex.printStackTrace();
                    ex.getMessage();
                }
       });
        }
        
        public void sendMessage(String message){
           CompletableFuture.runAsync(() -> {
                 chatConnection.sendMessage(message);
         });
    }

}
        

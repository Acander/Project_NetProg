package client.view;

import client.controller.Controller;
import client.net.OutputHandler;
import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

public class View {
    
    private Controller controller;
    private String username;
    private volatile boolean chatting = false;
  
    public void startUp(ConnectionFactory cf, TopicConnectionFactory tcf, Queue clientQueue, Topic topic){
        this.controller = new Controller(cf, tcf, clientQueue, topic);
        InputReader inputReader = new InputReader();
        inputReader.start();
    }
    
   private class InputReader extends Thread{
        public void run(){
            Scanner scan = new Scanner(System.in);
            System.out.println("Welcome to the chat application! Please enter your username");
            username = scan.nextLine();
            System.out.println("Nice to see you "+username+", to exit program type: QUIT, to join chat type JOIN");
            while(true){
                try{
                    String input = scan.nextLine();
                    if(input.equals("JOIN")){
                        System.out.println("joining chat... to leave the chat type: LEAVE_CHAT");
                        controller.joinChat(new ConsoleOutput(), username);
                        String message;
                        while (chatting) {
                            if ((message = scan.nextLine()) != null) {
                                if(message.equals("LEAVE_CHAT")){
                                    controller.leaveChat();     
                                    System.out.println("You left the chat..., to join type: JOIN, to exit program type: QUIT");                                 
                                    controller.sendMessage("User "+username+" disconnected from chat");
                                    chatting = false;
                                    break;
                                 }
                                controller.sendMessage(username+": "+message);
                            }
                        }
                    }else if(input.equals("QUIT")){
                        System.out.print("Quting program");
                        quit();
                    }else{
                        System.err.println("Invalid input!");
                    }
                }catch (Exception e){
                        System.err.println("Failed to establish connection with GlassFish JMS server. Try again with JOIN or exit program with QUIT");
                        chatting = false;
                    }
                }
            }
        }
        
        private void quit(){
            System.exit(1);
        }
   
     private class ConsoleOutput implements OutputHandler{
         
        @Override
        public void handleMessage(String msg){
               System.out.println(msg);
        }
        
        @Override
        public void reportMessageSendingFailure(String report){
            System.err.println(report);
        }
       
        @Override
        public void handleConnectionMessage(String msg){
            if(msg.equals("connectionOK")){
                chatting = true;
            }else if(msg.equals("listenerFailure")){
                System.err.println("Disconnected. Failed to recieve chat message, try to  reconnect with JOIN or exit with QUIT");
                chatting = false;
            }
        }
    }
}

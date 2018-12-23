package Client.view;

import Client.controller.Controller;
import Client.net.OutputHandler;
import java.util.Scanner;

/**
 * This class will handle all user interaction with the application
 */
public class View {
    
    private Controller controller = new Controller();
    private String username;
    
    private final String CMD_JOIN = "JOIN";
    private final String CMD_QUIT = "QUIT";  
    private final String CMD_LEAVE = "LEAVE";
    
    private boolean chatting = false;
    
    public void startUp(){
        InputReader inputReader = new InputReader();
        inputReader.start();
    }
    
   private class InputReader extends Thread{
        public void run(){
            Scanner scan = new Scanner(System.in);
            try{
                    
                    while(true) {
                        userRegistration(scan);
                        
                        String input = scan.nextLine();
                        
                        if(input.equals(CMD_JOIN)){
                            System.out.println("joining chat...");
                            controller.joinChat(new ConsoleOutput());
                            
                            String message;
                            while (true) {
                                message = scan.nextLine();
                                if (msgNotNull(message)) {
                                    
                                    if(message.equals(CMD_LEAVE)){
                                        if(chatting) {
                                            announceLeaving();
                                            controller.terminateParticipation();
                                        }
                                        break;
                                    }
                                    else if(message.equals(CMD_QUIT)) {
                                        announceLeaving();
                                        quit();
                                    }
                                    else {
                                        if (chatting) {
                                            controller.sendMessage(username+": "+message);
                                        } else {
                                            System.err.println("Failed to send message. Please try again");
                                        }
                                    }
                                }
                            }
                        } else if (input.equals(CMD_QUIT)) {
                            quit();
                        } else {
                            System.err.println("Invalid input!");
                        }
                    }
            }catch (Exception e){
                //e.printStackTrace();
                System.out.println("Failed to connect. Please try again in a moment.");
            }
        }
    }
   
   private void announceLeaving() {
       controller.sendMessage("User "+username+" disconnected from chat");
   }
   
   private void userRegistration(Scanner scan) {
       System.out.println("Welcome to the chat application! Please enter your username");
       username = scan.nextLine();
       System.out.println("Nice to see you "+username+", to leave chat type: QUIT, to join chat type JOIN");
   }
   
   private boolean msgNotNull(String msg) {
       return msg != null;
   }
   
   private void quit() {
       System.out.println("Quting program...");
       System.exit(1);
   }
     
     private class ConsoleOutput implements OutputHandler{
         
        private final String NO_MESSAGE_ERROR = "A message could not be received";
         
        @Override
        public void handleMessage(String msg){
                System.out.println(msg);
        }

        @Override
        public void noMessageError() {
            System.err.println(NO_MESSAGE_ERROR);
        }
        
        @Override
        public void reportMessageSendingFailure(String report){
            System.err.println(report);
        }
        
        @Override
        public void connectionMessage(){
            chatting = true;
        }
    }
}

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
  
    public void startUp(){
        InputReader inputReader = new InputReader();
        inputReader.start();
    }
    
   private class InputReader extends Thread{
        public void run(){
            Scanner scan = new Scanner(System.in);
            try{
                System.out.println("Welcome to the chat application! Please enter your username");
                username = scan.nextLine();
                System.out.println("Nice to see you "+username+", to leave chat type: QUIT, to join chat type JOIN");
                //controller.startListener(new ConsoleOutput());
                if(scan.nextLine().equals("JOIN")){
                    System.out.println("joining chat...");
                    controller.joinChat(new ConsoleOutput());
                    String message;
                    while (true) {
                    //System.out.print("Say: ");
                        if ((message = scan.nextLine()) != null) {
                            if(message.equals("QUIT")){
                                controller.sendMessage("User "+username+" disconnected from chat");
                                break;
                            }
                            controller.sendMessage(username+": "+message);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
     
     private class ConsoleOutput implements OutputHandler{
         
         @Override
        public void handleMessage(String msg){
               System.out.println(msg);
        }
    }
}

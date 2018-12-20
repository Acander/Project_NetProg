package chatjms;

import javax.jms.Queue;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

/**
 *
 * @author adria
 */
public class Main {

    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue queue;
    
    static Scanner scan = new Scanner(System.in);
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
        
        System.out.println("Sending message to JMS - ");
        System.out.println("Welcomde to chat, please enter username");
        String username = scan.nextLine();
        
        while(true) {
            String message = scan.nextLine();
            System.out.print("say: ");
            if(message.equals("break")) {
                break;
            }
            jmsProducer.send((Destination) queue, username+": "+message);
        }
        jmsProducer.send((Destination) queue, "User "+ username +" disconnected from chat");
        System.out.println("Message Send Sucessfull");
    }
    
}

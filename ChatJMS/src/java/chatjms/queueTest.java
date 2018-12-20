package chatjms;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.Queue;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

/**
 *
 * @author adria
 */
public class queueTest {

    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue clientQueue;
    
    @Resource(mappedName = "jms/confirmQueue")
    private static Queue confirmQueue;
    
    @Resource(mappedName = "jms/sendQueue")
    private static Queue msgQueue;
    
    static Scanner scan = new Scanner(System.in);
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
        JMSConsumer jmsConsumer;
        jmsConsumer = jmsContext.createConsumer(msgQueue);
        
        System.out.println("Sending message to JMS - ");
        System.out.println("Welcomde to chat, please enter username");
        String username = scan.nextLine();
        
        while(true) {
            String message = scan.nextLine();
            System.out.print("say: ");
            if(message.equals("break")) {
                break;
            }
            jmsProducer.send((Destination) clientQueue, message);
           while(true) {
                String msg = jmsConsumer.receiveBody(String.class);
                if(msg.equals("break")) {
                    jmsProducer.send((Destination) confirmQueue, "done");
                    break;
                }
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                System.out.println(timeStamp+", "+msg);
            }
        }
        jmsProducer.send((Destination) clientQueue, "User "+ username +" disconnected from chat");
        System.out.println("Message Send Sucessfull");
    }
    
}

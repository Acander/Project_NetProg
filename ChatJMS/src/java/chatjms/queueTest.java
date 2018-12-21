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
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TemporaryQueue;

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
    public static void main(String[] args) throws JMSException {
        JMSContext jmsContext = connectionFactory.createContext();
        JMSConsumer jmsConsumer;
        //jmsConsumer = jmsContext.createConsumer(msgQueue);
        //JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(queue);
        //jmsConsumer = jmsContext.createConsumer(queue);
        
        System.out.println("Sending message to JMS - ");
        
        while(true) {
            TemporaryQueue queue = jmsContext.createTemporaryQueue();
            JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(queue);
            jmsConsumer = jmsContext.createConsumer(queue);
            String message = scan.nextLine();
            if(message.equals("break")) {
                break;
            }
            jmsProducer.send((Destination) clientQueue, message);
            while(true) {
                String msg = jmsConsumer.receiveBody(String.class);
                if(msg.equals("done")) {
                    //jmsProducer.send((Destination) confirmQueue, "done");;
                    System.out.println("All messages collected!");
                    break;
                }
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                System.out.println(timeStamp+", "+msg);
            }
        }
        //jmsProducer.send((Destination) clientQueue, "User "+ username +" disconnected from chat");
        //System.out.println("Message Send Sucessfull");
    }
    
}

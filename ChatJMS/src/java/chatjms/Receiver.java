package chatjms;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;

public class Receiver {
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue queue;
    
    public static void main(String[] args) {
        JMSContext jmsContext = connectionFactory.createContext();
        JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
        
        System.out.println("Receiving Messages - ");
        while(true){
            String message = jmsConsumer.receiveBody(String.class);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            System.out.println(timeStamp+", "+message);
        }
    }
}

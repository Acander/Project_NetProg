package Client.startup;

import Client.net.ChatConnection;
import Client.view.View;
import javax.annotation.Resource;
//import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
public class Main {
    
    /**
     * TODO, figure out a way to connect to the Glassfish JMS resources from within the net layer, 
     * This is a bad quick fix that leif will not approve off.
     * */
    
    
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private static TopicConnectionFactory tcf;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue clientQueue;
    
    /*@Resource(mappedName = "jsm/chatQueue2")
    private static Queue queue2;
    
    @Resource(mappedName = "jsm/chatQueue3")
    private static Queue queue3;*/
    
    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;
    
    /**
     * Method is called when the client start the applications
     * @param args , currently to arguments
     */
    public static void main(String[] args){
        
        ChatConnection.connectionFactory = connectionFactory;
        ChatConnection.clientQueue = clientQueue;
        ChatConnection.topic = topic;
        ChatConnection.tcf = tcf;
        /*ChatConnection.queue2 = Main.queue2;
        ChatConnection.queue3 = Main.queue3;*/
        
        View view = new View();
        view.startUp();
    }
    
}

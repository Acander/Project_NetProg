package startup;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import net.ChatRecorder;
import net.ClientHandler;

/**
 *
 * @author adria
 */
public class Main {
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue queue;
    
    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private static TopicConnectionFactory topicConnectionFactory;
    
    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;
    
    public static void main(String[] args) throws JMSException {
        ClientHandler clientHandler = new ClientHandler(connectionFactory, queue);
        new ChatRecorder(clientHandler, topicConnectionFactory, topic);
        clientHandler.startClientHandler();
    }
}

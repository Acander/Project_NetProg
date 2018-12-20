package chatjms;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**
 *
 * @author adria
 */
public class TopicProducer {
    
    
    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private static TopicConnectionFactory topicConnectionFactory;
    
    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;
    
    public static void main(String[] args) throws JMSException {
        //JMSContext jmsContext = topicConnectionFactory.createContext();
        TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
        TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        topicConnection.start();
        TopicProducer topicProducer = new TopicProducer();
        topicProducer.sendMessage("message from adde", topicSession, topic);
        
    }
    
    public void sendMessage(String text, TopicSession topicSession, Topic topic) throws JMSException {
        TopicPublisher topicPublisher = topicSession.createPublisher(topic);
        TextMessage textMessage = topicSession.createTextMessage(text);
        topicPublisher.publish(textMessage);
        topicPublisher.close();
    }
}

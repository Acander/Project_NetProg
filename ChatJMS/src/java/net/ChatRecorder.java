package net;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

/**
 *
 * @author adria
 */
public class ChatRecorder implements MessageListener {
    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private TopicConnectionFactory topicConnectionFactory;
    
    @Resource(mappedName = "jms/chatTopic")
    private Topic topic;
    
    private JMSContext jmsContext;
    private ClientHandler clientHandler;
    
    public ChatRecorder(ClientHandler clientHandler) throws JMSException {
        this.clientHandler = clientHandler;
        getInitialContext();
        TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
        TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        topicSession.createSubscriber(topic).setMessageListener(this);
        topicConnection.start();
    }

    /**
     *
     * @param message
     * @throws JMSException
     */
    @Override
    public void onMessage(Message message) {
        try {
            String msg = message.getBody(String.class);
            System.out.println(msg);
            clientHandler.storeMsg(msg);
        } catch (JMSException ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }
    
    private void getInitialContext() {
        jmsContext = topicConnectionFactory.createContext();
    }
    
    
    
}

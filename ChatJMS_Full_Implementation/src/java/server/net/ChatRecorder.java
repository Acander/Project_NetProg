package server.net;

import server.net.ClientHandler;
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
    /*@Resource(mappedName = "jms/chatTopicConnectionFactory")
    private TopicConnectionFactory topicConnectionFactory;
    
    @Resource(mappedName = "jsm/chatTopic")
    private Topic topic;*/
    
    //private JMSContext jmsContext;
    private ClientHandler clientHandler;
    //private final Topic topic;
    //private final TopicConnectionFactory topicConnectionFactory;
    
    public ChatRecorder(ClientHandler clientHandler, TopicConnectionFactory tcf, Topic topic) throws JMSException {
        this.clientHandler = clientHandler;
        //jmsContext = tcf.createContext();
        TopicConnection topicConnection = tcf.createTopicConnection();
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
    
    /*private void getInitialContext() {
        jmsContext = topicConnectionFactory.createContext();
    }*/
    
    
    
}

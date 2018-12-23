package server.net;

import server.net.ClientHandler;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

public class ChatRecorder implements MessageListener {
    
    private ClientHandler clientHandler;
    
    public ChatRecorder(ClientHandler clientHandler, TopicConnectionFactory tcf, Topic topic) throws FailedToInitializeChatRecorderException {
        this.clientHandler = clientHandler;
        initializeTopicListener(tcf, topic);
    }
    
    private void initializeTopicListener(TopicConnectionFactory tcf, Topic topic) throws FailedToInitializeChatRecorderException {
        try {
            TopicConnection topicConnection = tcf.createTopicConnection();
            TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            topicSession.createSubscriber(topic).setMessageListener(this);
            topicConnection.start();
        } catch (JMSException jmse) {
            throw new FailedToInitializeChatRecorderException(jmse);
        }
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
}
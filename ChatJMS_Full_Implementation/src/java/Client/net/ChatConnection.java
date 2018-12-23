package Client.net;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

public class ChatConnection {

    public static ConnectionFactory connectionFactory;
    public static TopicConnectionFactory tcf;
    public static Queue clientQueue;
    public static Topic topic;
    
    private Listener listener;

    /**
     * This method is responsible for starting the listener and joining the chat
     *
     * @param outputHandler observer interface to send chat messages to queue
     */
    public void joinChat(OutputHandler outputHandler) throws JMSException {
        JMSContext jmsContext = connectionFactory.createContext();
        TemporaryQueue queue = jmsContext.createTemporaryQueue();
        JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(queue);
        JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
        jmsProducer.send((Destination) clientQueue, "###");
        
        while (true) {
            String msg = jmsConsumer.receiveBody(String.class);
            if (msg.equals("done")) {
                System.out.println("All messages collected!");
                break;
            }
            outputHandler.handleMessage(msg);
        }
        closeContext(jmsContext);
        startListener(outputHandler);
    }

    public void sendMessage(String message) {
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
        jmsProducer.send(topic, message);
        closeContext(jmsContext);
    }
    
    private void closeContext(JMSContext jmsContext) {
        jmsContext.close();
    }

    private void startListener(OutputHandler outputHandler) throws JMSException {
        listener = new Listener(outputHandler);
    }
    
    public void closeConnection() throws JMSException {
        listener.stopTopicListener();
    }

    private class Listener implements MessageListener {

        private final OutputHandler outputHandler;
        
        private TopicConnection topicConnection;
        private TopicSession topicSession;

        public Listener(OutputHandler outputHandler) throws JMSException {
            this.outputHandler = outputHandler;
            initializeTopicListener();
            outputHandler.connectionMessage();
        }
        
        private void initializeTopicListener() throws JMSException{
            topicConnection = tcf.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            topicSession.createSubscriber(topic).setMessageListener(this);
            topicConnection.start();
        }
        
        public void stopTopicListener() throws JMSException {
            topicSession.close();
            topicConnection.close();
        }

        @Override
        public void onMessage(Message message) {
            try {
                outputHandler.handleMessage(message.getBody(String.class));
            } catch (JMSException ex) {
                outputHandler.noMessageError();
            }
        }
    }
}

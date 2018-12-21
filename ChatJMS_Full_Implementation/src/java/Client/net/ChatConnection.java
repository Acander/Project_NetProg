package Client.net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    /*public static Queue queue2;
    public static Queue queue3;*/
    public static Topic topic;

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
                //jmsProducer.send((Destination) confirmQueue, "done");;
                System.out.println("All messages collected!");
                break;
            }
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            outputHandler.handleMessage(msg);
        }
        startListener(outputHandler);
    }

    public void sendMessage(String message) {
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
        jmsProducer.send(topic, message);
    }

    private void startListener(OutputHandler outputHandler) throws JMSException {
        new Listener(outputHandler);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener implements MessageListener {

        private final OutputHandler outputHandler;

        public Listener(OutputHandler outputHandler) throws JMSException {
            this.outputHandler = outputHandler;
            TopicConnection topicConnection = tcf.createTopicConnection();
            TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            try {
                topicSession.createSubscriber(topic).setMessageListener(this);
            } catch (JMSException ex) {
                Logger.getLogger(ChatConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            topicConnection.start();
        }

        @Override
        public void onMessage(Message message) {
            try {
                outputHandler.handleMessage(message.getBody(String.class));
            } catch (JMSException ex) {
                System.out.println("No message in chat connection");
            }
        }
    }
}

package client.net;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static ConnectionFactory connectionFactory;
    private static TopicConnectionFactory tcf;
    private static Queue clientQueue;
    private static Topic topic;
    private OutputHandler outputHandler;
    private Listener listener;
  
    public ChatConnection(ConnectionFactory cf, TopicConnectionFactory tcf, Queue clientQueue, Topic topic){
        this.connectionFactory = cf;
        this.tcf = tcf;
        this.clientQueue = clientQueue;
        this.topic = topic;
    }

    public void joinChat(OutputHandler outputHandler, String username) throws ConnectionException{
            this.outputHandler = outputHandler;
            try{
                JMSContext jmsContext = connectionFactory.createContext();
                TemporaryQueue queue = jmsContext.createTemporaryQueue();
                JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(queue);
                JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
                jmsProducer.send((Destination) clientQueue, "###");
                while (true) {
                    String msg = jmsConsumer.receiveBody(String.class, 5000);
                    if (msg == null) {
                        outputHandler.handleMessage("FAILED TO RETRIEVE CHAT HISTORY");
                        break;
                    }else if(msg.equals("done")){
                        break;
                    }else{
                        outputHandler.handleMessage(msg);
                    }
                }
                sendMessage("User "+username+" joined the chat");
                outputHandler.handleMessage("Live-->");
                outputHandler.handleConnectionMessage("connectionOK");
                closeContext(jmsContext);
                startListener(outputHandler);
            }catch(Exception e){
                throw new ConnectionException(e.getMessage());
            }
    }
    
    private void closeContext(JMSContext jmsContext){
        jmsContext.close();
    }

    public void sendMessage(String message){
        try{
            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer jmsProducer = jmsContext.createProducer();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            jmsProducer.send(topic, timeStamp+"->"+message);
            closeContext(jmsContext);
        }catch(Exception e){
            outputHandler.reportMessageSendingFailure("Failed to send message");
        }
    }
    
    public void leaveChat() throws Exception{
        listener.stopTopicListener();
    }

    private void startListener(OutputHandler outputHandler) throws Exception {
            listener = new Listener(outputHandler);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener implements MessageListener{

        private final OutputHandler outputHandler;
        private TopicConnection topicConnection;
        private TopicSession topicSession;

        public Listener(OutputHandler outputHandler) throws Exception {
            this.outputHandler = outputHandler;
            try {
                topicConnection = tcf.createTopicConnection();
                topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
                topicSession.createSubscriber(topic).setMessageListener(this);
                topicConnection.start();
            } catch (JMSException ex) {
                throw new Exception("ListenerConnectionError");
            }          
        }
        
        public void stopTopicListener() throws Exception{
            topicSession.close();
            topicConnection.close();
        }

        @Override
        public void onMessage(Message message) {
            try {
                outputHandler.handleMessage(message.getBody(String.class));
            } catch (JMSException ex) {
                 outputHandler.handleConnectionMessage("listenerFailure");
            }
        }
    }
}

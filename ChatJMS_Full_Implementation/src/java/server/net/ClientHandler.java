package server.net;

import java.util.List;
import model.Conversation;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

/**
 *
 * @author adria
 */
public class ClientHandler {
    
    private Conversation conversation = new Conversation();
    
    JMSContext jmsContext;
    JMSConsumer jmsConsumer;
    JMSProducer jmsProducer;
    Queue clientQueue;
    
    public ClientHandler(ConnectionFactory connectionFactory, Queue clientQueue) {
        jmsContext = connectionFactory.createContext();
        jmsConsumer = jmsContext.createConsumer(clientQueue);
        jmsProducer = jmsContext.createProducer();
        this.clientQueue = clientQueue;
    }
    
    public void storeMsg(String msg) {
        conversation.storeMsg(msg);
    }
    
    public void startClientHandler() throws FailedToStartClientHandlerException {
        try {
            listenForNewUsers();
        } catch (InterruptedException | JMSException e) {
            throw new FailedToStartClientHandlerException(e);
        }
    }
    
    private void listenForNewUsers() throws InterruptedException, JMSException {
        System.out.println("Starting to handle clients!");
        while (true) {
            Message msg = jmsConsumer.receive();
            if (newConnectingClient(msg)) {
                Queue msgQueue = getClientPrivateQueue(msg);
                System.out.println("Message caught " + msg.getBody(String.class));
                sendAllEntriesToClient(msgQueue);
                
            }
        }
    }
    
    private boolean newConnectingClient(Message msg) throws JMSException {
        return msg.getBody(String.class).equals("###");
    }
    
    private Queue getClientPrivateQueue(Message msg) throws JMSException {
        return (Queue) msg.getJMSReplyTo();
    }
    
    private void sendAllEntriesToClient(Queue msgQueue) {
        List<String> messages = conversation.getMessages();
        for (int i = 0; i < messages.size(); i++) {
                    sendMsg(messages.get(i), msgQueue);
                }
                sendConfirmationMsg(msgQueue);
    }
    
    private void sendMsg(String item, Queue msgQueue) {
        System.out.println("Sending: " + item);
        jmsProducer.send((Destination) msgQueue, item);
    }
    
    private void sendConfirmationMsg(Queue msgQueue) {
        jmsProducer.send((Destination) msgQueue, "done");
        System.out.println("Sending a break call****************");
    }
    
}

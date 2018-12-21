/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    /*@Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue queue;*/
    
    private Conversation conversation = new Conversation();
    JMSContext jmsContext;
    JMSConsumer jmsPrimaryConsumer;
    JMSConsumer jmsSecondaryConsumer;
    //JMSConsumer jmsClearConsumer;
    JMSProducer jmsProducer;
    Queue clientQueue;
    //Queue msgQueue;
    //Queue confirmQueue;
    
    public ClientHandler(ConnectionFactory connectionFactory, Queue clientQueue, Queue msgQueue, Queue confirmQueue) {
        jmsContext = connectionFactory.createContext();
        jmsPrimaryConsumer = jmsContext.createConsumer(clientQueue);
        jmsProducer = jmsContext.createProducer();
        //jmsSecondaryConsumer = jmsContext.createConsumer();
        //jmsClearConsumer = jmsContext.createConsumer(msgQueue);
        this.clientQueue = clientQueue;
        //this.msgQueue = msgQueue;
        //this.confirmQueue = confirmQueue;
    }
    
    public void storeMsg(String msg) {
        conversation.storeMsg(msg);
    }
    
    public void startClientHandler() throws InterruptedException, JMSException {
        listenForNewUsers();
    }
    
    private void listenForNewUsers() throws InterruptedException, JMSException {
        System.out.println("Starting to handle clients!");
        while (true) {
            Message msg = jmsPrimaryConsumer.receive();
            if (msg.getBody(String.class).equals("###")) {
                Queue msgQueue = (Queue) msg.getJMSReplyTo();
                System.out.println("Message caught " + msg.getBody(String.class));
                List<String> messages = conversation.getMessages();
                for (int i = 0; i < messages.size(); i++) {
                    System.out.println("Sending: " + messages.get(i));
                    jmsProducer.send((Destination) msgQueue, messages.get(i));
                }
                jmsProducer.send((Destination) msgQueue, "done");
                System.out.println("Sending a break call****************");
                //jmsSecondaryConsumer.receiveBody(String.class);
                //System.out.println("receving confirmation of empty queue-------------");
                
            }
            
        }
    }
    
}

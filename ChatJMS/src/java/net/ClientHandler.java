/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.util.List;
import javax.annotation.Resource;
import model.Conversation;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
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
    JMSProducer jmsProducer;
    Queue clientQueue;
    Queue msgQueue;
    Queue confirmQueue;
    
    public ClientHandler(ConnectionFactory connectionFactory, Queue clientQueue, Queue msgQueue, Queue confirmQueue) {
        jmsContext = connectionFactory.createContext();
        jmsPrimaryConsumer = jmsContext.createConsumer(clientQueue);
        jmsSecondaryConsumer = jmsContext.createConsumer(confirmQueue);
        jmsProducer = jmsContext.createProducer();
        this.clientQueue = clientQueue;
        this.msgQueue = msgQueue;
        this.confirmQueue = confirmQueue;
    }
    
    public void storeMsg(String msg) {
        conversation.storeMsg(msg);
    }
    
    public void startClientHandler() throws InterruptedException {
        listenForNewUsers();
    }
    
    private void listenForNewUsers() throws InterruptedException {
        while (true) {
            String message = jmsPrimaryConsumer.receiveBody(String.class);
            System.out.println(message);
            if (message.equals("###")) {
                System.out.println(message);
                List<String> messages = conversation.getMessages();
                for (int i = 0; i < messages.size(); i++) {
                    System.out.println("Sending: " + messages.get(i));
                    jmsProducer.send((Destination) msgQueue, messages.get(i));
                }
                jmsProducer.send((Destination) msgQueue, "done");
                jmsSecondaryConsumer.receiveBody(String.class);
                
            }
            
        }
    }
    
}

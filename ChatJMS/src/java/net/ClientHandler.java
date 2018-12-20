/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

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
    JMSConsumer jmsConsumer;
    JMSProducer jmsProducer;
    Queue queue;
    
    public ClientHandler(ConnectionFactory connectionFactory, Queue queue) {
        jmsContext = connectionFactory.createContext();
        jmsConsumer = jmsContext.createConsumer(queue);
        jmsProducer = jmsContext.createProducer();
    }
    
    public void storeMsg(String msg) {
        conversation.storeMsg(msg);
    }
    
    public void startClientHandler() {
        listenForNewUsers();
    }
    
    private void listenForNewUsers() {
        while (true) {
            String message = jmsConsumer.receiveBody(String.class);
            if (message.equals("###")) {
                System.out.println(message);
                for (String msg : conversation.getMessages()) {
                    jmsProducer.send((Destination) queue, message);
                }
            }
            
        }
    }
    
}

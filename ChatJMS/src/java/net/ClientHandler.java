/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import javax.annotation.Resource;
import model.Conversation;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

/**
 *
 * @author adria
 */
public class ClientHandler {
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue queue;
    
    private Conversation conversation = new Conversation();
    JMSContext jmsContext = connectionFactory.createContext();
    JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
    JMSProducer jmsProducer = jmsContext.createProducer();
    
    public void startClientHandler() {
        listenForNewUsers();
    }
    
    private void listenForNewUsers() {
        while (true) {
            String message = jmsConsumer.receiveBody(String.class);
            System.out.println(message);
            for(String nextMsg : conversation.getMessages) {
                
            }
        }
    }
}

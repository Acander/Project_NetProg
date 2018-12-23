package server.startup;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import server.net.ChatRecorder;
import server.net.ClientHandler;
import server.net.FailedToInitializeChatRecorderException;
import server.net.FailedToStartClientHandlerException;

public class Main {
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/chatQueue")
    private static Queue clientQueue;
    
    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private static TopicConnectionFactory topicConnectionFactory;
    
    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;
    
    public static void main(String[] args) throws FailedToInitializeChatRecorderException, FailedToStartClientHandlerException {
        ClientHandler clientHandler = new ClientHandler(connectionFactory, clientQueue);
        new ChatRecorder(clientHandler, topicConnectionFactory, topic);
        clientHandler.startClientHandler();
    }
}

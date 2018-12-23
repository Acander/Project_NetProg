package client.startup;

import client.net.ChatConnection;
import client.view.View;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

public class Main {

    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private static TopicConnectionFactory tcf;

    @Resource(mappedName = "jms/chatQueue")
    private static Queue clientQueue;

    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;

    /**
     * Method is called when the client start the applications
     *
     * @param args , currently to arguments
     */
    public static void main(String[] args) {

        View view = new View();
        view.startUp(connectionFactory, tcf, clientQueue, topic);
    }

}

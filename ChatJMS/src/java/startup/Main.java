package startup;

import javax.jms.JMSException;
import net.ChatRecorder;
import net.ClientHandler;

/**
 *
 * @author adria
 */
public class Main {
    
    public static void main(String[] args) throws JMSException {
        ClientHandler clientHandler = new ClientHandler();
        new ChatRecorder(clientHandler);
        clientHandler.startClientHandler();
    }
}

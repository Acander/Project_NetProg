package Client.net;

public interface OutputHandler {
   
    /**
     * Handles an entry into the chat.
     * @param message 
     */
    void handleMessage(String message);

    /**
     * Handles the situation where a message can not be handled properly.
     */
    void noMessageError();
    
    public void reportMessageSendingFailure(String report);
    
    public void connectionMessage();
}

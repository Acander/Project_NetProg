/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

/**
 *
 * @author Felix
 */
public class FailedToStartClientHandlerException extends Exception{
    
    public FailedToStartClientHandlerException(Throwable cause){
        super(cause);
    }
    
}

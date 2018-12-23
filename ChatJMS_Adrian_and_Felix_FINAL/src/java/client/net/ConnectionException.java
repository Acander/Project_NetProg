/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.net;

/**
 *
 * @author Felix
 */
public class ConnectionException extends Exception{
    
    public ConnectionException(String reason){
        super(reason);
    }
    
}

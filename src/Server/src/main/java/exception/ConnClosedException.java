/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Aron
 */
public class ConnClosedException extends Exception{
    public ConnClosedException(String message){
        super(message);
    }
}

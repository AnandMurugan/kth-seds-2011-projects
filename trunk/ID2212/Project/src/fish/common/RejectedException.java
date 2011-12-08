/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

/**
 * Simple exception that should be thrown if protocol communication is violated
 * 
 * @author Igor
 */
public class RejectedException extends Exception {
    /**
     * Simple constructor
     * 
     * @param message Exception message
     */
    public RejectedException(String message) {
        super(message);
    }
}

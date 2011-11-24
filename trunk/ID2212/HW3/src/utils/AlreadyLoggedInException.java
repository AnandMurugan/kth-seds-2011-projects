/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Igor
 */
public class AlreadyLoggedInException extends RejectedException {
    int id;

    public AlreadyLoggedInException(String reason, int id) {
        super(reason);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

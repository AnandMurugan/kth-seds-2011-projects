/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.model;

/**
 *
 * @author Igor
 */
public class NotUserFoundException extends Exception {
    private String userName;

    public NotUserFoundException(String userName) {
        super("User not found");
        this.userName = userName;
    }

    public String getUser() {
        return userName;
    }
}

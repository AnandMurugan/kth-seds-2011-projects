/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import Common.RemoteClient;

/**
 *
 * @author julio
 */
public interface OwnershipModificable {
    public void setOwner(RemoteClient newOwner);
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author julio
 */
public interface MarketItem extends Serializable {
    public long getId();

    public String getName();

    public float getPrice();

    public String getOwner();

    public void setOwner(String owner);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package market;

import java.io.Serializable;

/**
 *
 * @author julio
 */
public interface MarketItem extends Serializable {
    public long getMarketItemId();

    public String getName();

    public float getPrice();

    public String getOwner();

    public void setOwner(String owner);

    public void setMarketItemId(long id);
}

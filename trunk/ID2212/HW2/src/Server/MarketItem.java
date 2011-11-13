/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.IMarketItem;
import Common.RemoteClient;
import java.io.Serializable;

/**
 *
 * @author julio
 */
public class MarketItem implements Serializable, OwnershipModificable, IMarketItem {
    private Integer marketId;
    private String name;
    private Integer price;
    private RemoteClient owner;
        
    public MarketItem(Integer marketId, String name, Integer price){
        this.marketId = marketId;
        this.name = name;
        this.price = price;
    }
    
    @Override
    public Integer getMarketId(){
        return marketId;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public Integer getPrice(){
        return price;
    }

    @Override
    public void setOwner(RemoteClient newOwner) {
        this.owner = newOwner;
    }
}

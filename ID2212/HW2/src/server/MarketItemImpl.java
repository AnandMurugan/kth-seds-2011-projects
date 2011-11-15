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
public class MarketItemImpl implements Serializable, OwnershipModificable, MarketItem {
    private Integer marketId;
    private String name;
    private Integer price;
    private MarketClientCallbackable owner;

    public MarketItemImpl(Integer marketId, String name, Integer price) {
        this.marketId = marketId;
        this.name = name;
        this.price = price;
    }

    @Override
    public Integer getMarketId() {
        return marketId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public void setOwner(MarketClient newOwner) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

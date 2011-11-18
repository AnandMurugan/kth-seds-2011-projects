/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author julio
 */
public class MarketItemImpl implements MarketItem {
    private long marketItemId;
    private String name;
    private float price;
    private String owner;

    public MarketItemImpl(String name, float price, String owner) {
        this.marketItemId = -1;
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    @Override
    public long getMarketItemId() {
        return marketItemId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void setMarketItemId(long id) {
        this.marketItemId = id;
    }
}

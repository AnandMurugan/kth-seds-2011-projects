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
    private static long idCounter = 1;
    private long id;
    private String name;
    private float price;
    private String owner;

    public MarketItemImpl(String name, float price, String owner) {
        this.id = idCounter++;
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    @Override
    public long getId() {
        return id;
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
}

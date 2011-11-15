/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Igor
 */
public interface Trader {
    public void postItem(MarketItem item);

    public void postWish(MarketItem wish);

    public void buyItem(MarketItem item);

    public boolean register(Marketpalce market);

    public boolean unregister();
}

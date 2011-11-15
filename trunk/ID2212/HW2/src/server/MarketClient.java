/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Igor
 */
public interface MarketClient {
    public void postItem(MarketItem item);

    public void postWish(MarketItem wish);

    public boolean register(Market market);

    public boolean unregister();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.List;

/**
 *
 * @author Igor
 */
public interface Trader {
    public void postItem(MarketItem item);

    public void postWish(MarketItem wish);

    public void buyItem(MarketItem item);

    public List<MarketItem> getItems();

    public boolean register(Marketplace market);

    public boolean unregister();
}

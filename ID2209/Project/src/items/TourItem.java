/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.io.Serializable;

/**
 *
 * @author Igor
 */
public class TourItem implements Serializable {
    protected String id;
    protected String title;

    public TourItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

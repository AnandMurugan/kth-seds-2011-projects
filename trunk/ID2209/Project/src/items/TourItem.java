/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

/**
 *
 * @author Igor
 */
public class TourItem {
    private String id;
    private String name;

    public TourItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

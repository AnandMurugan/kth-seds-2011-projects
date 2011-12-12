/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

/**
 *
 * @author Igor
 */
public class MuseumItem extends TourItem {
    private String[] subject;
    private String[] objectType;
    private String[] material;

    public MuseumItem(String[] subject, String[] objectType, String[] material, String id, String name) {
        super(id, name);
        this.subject = subject;
        this.objectType = objectType;
        this.material = material;
    }

    public String[] getMaterial() {
        return material;
    }

    public String[] getObjectType() {
        return objectType;
    }

    public String[] getSubject() {
        return subject;
    }
}

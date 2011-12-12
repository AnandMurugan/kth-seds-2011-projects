/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.util.Arrays;

/**
 *
 * @author Igor
 */
public class MuseumItem extends TourItem {
    private String[] subject;
    private String[] objectType;
    private String[] material;

    public MuseumItem(String id, String name, String[] subject, String[] objectType, String[] material) {
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

    @Override
    public String toString() {
        return "MuseumItem{" + "id=" + id + ", title=" + title + ", subject=" + Arrays.deepToString(subject) + ", objectType=" + Arrays.deepToString(objectType) + ", material=" + Arrays.deepToString(material) + '}';
    }
}

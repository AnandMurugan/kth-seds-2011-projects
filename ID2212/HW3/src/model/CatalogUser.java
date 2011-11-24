/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author julio
 */
@Entity
public class CatalogUser implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int password;
    @OneToMany(mappedBy = "owner")
    private List<CatalogFile> myCatalogFiles;

    public CatalogUser() {
    }

    public CatalogUser(int id, String name, int password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPassword() {
        return password;
    }

    public List<CatalogFile> getMyCatalogFiles() {
        return myCatalogFiles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setMyCatalogFiles(List<CatalogFile> myCatalogFiles) {
        this.myCatalogFiles = myCatalogFiles;
    }
}

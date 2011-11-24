/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author julio
 */
@NamedQueries({
    @NamedQuery(name = CatalogUser.GET_USER_BY_ID_QUERY,
    query = "SELECT u "
    + "FROM CatalogUser u "
    + "WHERE u.id = :id"),
    @NamedQuery(name = CatalogUser.GET_USER_BY_NAME_QUERY,
    query = "SELECT u "
    + "FROM CatalogUser u "
    + "WHERE u.name = :name")
//    @NamedQuery(name = CatalogUser.DELETE_USER_QUERY,
//    query = "DELETE "
//    + "FROM CatalogUser u "
//    + "WHERE u.id = :id")
})
@Entity
public class CatalogUser implements Serializable {
    public static final String GET_USER_BY_ID_QUERY = "CatalogUser_getUserById";
    public static final String GET_USER_BY_NAME_QUERY = "CatalogUser_getUserByName";
    //public static final String DELETE_USER_QUERY = "CatalogUser_deleteUser";
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private int password;

    /*Constructors*/
    public CatalogUser() {
    }

    public CatalogUser(String name, int password) {
        this.name = name;
        this.password = password;
    }

    /*Getters and setters*/
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPassword() {
        return password;
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
}

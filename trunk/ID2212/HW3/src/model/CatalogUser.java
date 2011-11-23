/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author julio
 */
public class CatalogUser {
    private String userName;
    private String password;
    
    public CatalogUser(String name, String pwd){
        userName = name;
        this.password = pwd;
    }
    
    
    public String getUserName(){
        return userName;
    }
    
    public String getPassword(){
        return password;
    }
}

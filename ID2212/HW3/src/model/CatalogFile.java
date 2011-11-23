/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author julio
 */


public class CatalogFile {
    
    private String name;
    private int size;
    private CatalogUser owner;
    private AccessPermission permission;
    private long id;
    private String path;
    
    public long getId(){
        return id;
    }
    
    public String getName(){
        return name;
    } 
    
    public int getSize(){
        return size;
    } 
    
    public CatalogUser getOwner(){
        return owner;
    }
    
    public AccessPermission getAccessPermission(){
        return permission;
    }
}

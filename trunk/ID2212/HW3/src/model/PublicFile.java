/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author julio
 */
public class PublicFile extends CatalogFile{
    private Date lastModified; 
    private PublicPermission publicPermission;
    
    public Date getLastModifiedDate(){
        return lastModified;
    }
    
    public PublicPermission getPublicPermission(){
        return publicPermission;
    }
}

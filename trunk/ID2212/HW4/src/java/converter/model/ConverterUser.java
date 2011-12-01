/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.model;

import java.io.Serializable;
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
    @NamedQuery(name = ConverterUser.GET_USER_BY_NAME_PASSWORD_REQUEST,
    query = "SELECT u "
    + "FROM ConverterUser u "
    + "WHERE u.userName = :userName AND u.password =:password")
})
@Entity
public class ConverterUser implements ConverterUserDTO, Serializable {
    public static final String GET_USER_BY_NAME_PASSWORD_REQUEST = "ConverterUser_getUserByNameAndPassword";
    private static final long serialVersionUID = 16247164401L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConverterUser)) {
            return false;
        }
        ConverterUser other = (ConverterUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "converter.model.CurrencyUser[ id=" + id + " ]";
    }
    
}

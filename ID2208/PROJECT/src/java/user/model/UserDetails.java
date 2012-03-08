/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Anand
 */
@Entity
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String luserName;
    private String lpassword;
    private String lusergroup;
    private String fullName;
    private boolean loggedIn;
    
    public UserDetails() {
    }

    public UserDetails(String userName, String password, String fullName, String group, boolean banned) throws NoSuchAlgorithmException {
        this.luserName = userName;
        this.lusergroup = group;
        this.fullName = fullName;

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes(), 0, password.length());
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        this.lpassword = new String(sb);

    }

    public String getLuserName() {
        return luserName;
    }

    public void setLuserName(String userName) {
        this.luserName = userName;
    }

    public void setLpassword(String password) {
        this.lpassword = password;
    }

    public String getLusergroup() {
        return lusergroup;
    }

    public void setLusergroup(String lusergroup) {
        this.lusergroup = lusergroup;
    }

    public String getLpassword() {
        return lpassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLoggedIn(boolean logged){
       this.loggedIn = logged;
   }
   
   public boolean getLoggedIn(){
       return this.loggedIn;
   }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (luserName != null ? luserName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserDetails)) {
            return false;
        }
        UserDetails other = (UserDetails) object;
        if ((this.luserName == null && other.luserName != null) || (this.luserName != null && !this.luserName.equals(other.luserName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "user.model.UserDetails[ luserName=" + luserName + " ]";
    }
}

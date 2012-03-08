/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.view;

import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import user.controller.UserFacade;

/**
 *
 * @author julio
 */
@Stateful
@Named(value = "login")
@SessionScoped
@Model
public class Login implements Serializable {
    /** Creates a new instance of Login */
    private String userName;
    private String password;
    private boolean isLoggedIn = false;
    private String userRole;
    @EJB
    private UserFacade userFacade;

    public Login() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String logout() {

        if (userFacade.logOut(userName)) {
            isLoggedIn = false;
            this.userName = "";
            this.password = "";
            return "logout";
        }

        return "";
    }

    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public void loginUser() {
        if (userFacade.logIn(userName, password)) {
            isLoggedIn = true;
            userRole = userFacade.getUserRole(userName);
        }
    }
}

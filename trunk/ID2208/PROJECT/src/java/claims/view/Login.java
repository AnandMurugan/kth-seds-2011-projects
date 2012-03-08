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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

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

    public boolean renderPage(String page) {
        if (userRole.equalsIgnoreCase("ADMIN") && page.equals("adminMain")) {
            return true;
        }
        if (userRole.equalsIgnoreCase("JUNIOR") && page.equals("officerMain")) {
            return true;
        }
        if (userRole.equalsIgnoreCase("SENIOR") && page.equals("officerMain")) {
            return true;
        }
        if (userRole.equalsIgnoreCase("CUSTOMER") && page.equals("customerMain")) {
            return true;
        }
         if (userRole.equalsIgnoreCase("GARAGE") && page.equals("garageMain")) {
            return true;
        }
        return false;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user.view;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import user.controller.RegistrationException;
import user.controller.UserFacade;
import user.model.UserDetails;

/**
 *
 * @author Anand
 */
@Named(value = "customerMng")
@RequestScoped
public class CustomerManager {

    @EJB
    private UserFacade userFacade;
    private String userName;
    private String password;
    private String fullName;
    private List<UserDetails> usrLst = new ArrayList<UserDetails>();
    Exception registrationError;

    /**
     * Creates a new instance of CustomerManager
     */
    public CustomerManager() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public List<UserDetails> getUsrLst() {
        return usrLst;
    }

    public boolean isUsrLstNotEmpty() {
        refreshPage();
        if (usrLst.size() > 0) {
            return true;
        }
        return false;
    }

    public String registerCustomer() {
        try {
            userFacade.registerCustomer(userName, password, fullName);
            registrationError = null;
            return "registered";
        } catch (RegistrationException e) {
            registrationError = e;
            return "not registered";
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace(System.err);
        registrationError = e;
    }

    public boolean checkSuccess() {
        return registrationError == null;
    }

    public Exception getException() {
        return registrationError;
    }

    private void refreshPage() {
        usrLst = userFacade.getUsrLst();
    }
}

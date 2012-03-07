/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user.view;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import user.controller.UserFacade;

/**
 *
 * @author Anand
 */
@ManagedBean
@RequestScoped
@Named(value = "authBackingBean")
public class UserHelper {

    @EJB
    private UserFacade userFacade;

    public String logout() {
        String result = "/index?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        //HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        context.getExternalContext().invalidateSession();
        System.out.println(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
        return result;
    }

    public boolean loggedIn() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
            return false;
        }
        return true;
    }

    public boolean renderPage(String page) {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
            return true;
        } else {
            String userName = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            String role = userFacade.getUserRole(userName);
            if (role.equals("ADMIN") && page.equals("shopMain")) {
                return false;
            }
            if (role.equals("CUSTOMER") && page.equals("adminMain")) {
                return false;
            }
        }
        return true;
    }
}
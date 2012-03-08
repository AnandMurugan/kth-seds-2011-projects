/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user.controller;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import user.model.UserDetails;

/**
 *
 * @author Anand
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class UserFacade {
    @PersistenceContext(unitName = "ClaimsInsurancePU")
    private EntityManager em;

    public UserFacade() {
    }

    public void registerCustomer(String userName, String password, String fullName) throws RegistrationException {
        UserDetails user = em.find(UserDetails.class, userName);

        if (user != null) {
            throw new RegistrationException("userName already exists");
        }
        try {
            user = new UserDetails(userName, password, fullName, "CUSTOMER", false);
            em.persist(user);
        } catch (Exception e) {
            throw new RegistrationException("registration not possible");
        }
    }

    public List<UserDetails> getUsrLst() {
        List<UserDetails> usrLst = new ArrayList<UserDetails>();
        usrLst = (List) em.createNamedQuery("RetrieveUsers").getResultList();
        return usrLst;
    }

    public String getUserRole(String userName) {
        UserDetails user = em.find(UserDetails.class, userName);
        if (user != null) {
            return user.getLusergroup();
        }
        return "not a user";
    }

    public boolean logIn(String userName, String pwd) {
        UserDetails user = em.find(UserDetails.class, userName);
        if (user != null && user.getLpassword().equals(pwd)) {
            user.setLoggedIn(true);
            return true;
        }
        return false;
    }

    public boolean logOut(String userName) {
        UserDetails user = em.find(UserDetails.class, userName);
        if (user != null) {
            user.setLoggedIn(false);
            return true;
        }
        return false;
    }
}

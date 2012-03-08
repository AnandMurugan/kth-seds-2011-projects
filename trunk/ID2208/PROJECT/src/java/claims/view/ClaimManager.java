/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.view;

import claims.controller.ClaimsFacade;
import claims.model.Claim;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Anand
 */
@Named(value = "claimManager")
@RequestScoped
public class ClaimManager implements Serializable {

    @EJB
    private ClaimsFacade claimsFacade;
    
    private String id;
    private String carRegNo;
    private String owner;
    private String carValue;
    private String status_desc;
    private String type;
    private String userName;
    private String userGroup;

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    
    private List<Claim> claimsList = new ArrayList<Claim>();

    public List<Claim> getClaimsList() {
        return claimsList;
    }

    public void setClaimsList(List<Claim> claimsList) {
        this.claimsList = claimsList;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public void setCarValue(String carValue) {
        this.carValue = carValue;
    }

    public void setClaimId(String claimId) {
        this.id = claimId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setStatus(String status) {
        this.status_desc = status;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isListNotEmpty() {
        refreshPage();
        if (claimsList.size() > 0) {
            return true;
        }
        return false;
    }
    
    private void refreshPage() {
        //System.out.println("[]usergroup"+userGroup);
        setClaimsList(claimsFacade.getClaimsList(userName, userGroup));
    }
    
    /** Creates a new instance of ClaimManager */
    public ClaimManager() {
    }
}

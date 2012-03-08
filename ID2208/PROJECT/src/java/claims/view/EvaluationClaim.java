/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.view;

import claims.controller.ClaimsFacade;
import claims.model.Claim;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author julio
 */
@Named(value = "evaluationClaim")
@RequestScoped
public class EvaluationClaim {

    @EJB
    ClaimsFacade claimFacade;
    private String id;
    private Claim claim;
    private String carRegNo;
    private Double carValue;
    private String status;
    private String carModel;
    private String owner;
    private String type;
    private String userName;
    private String userGroup;
    List<String> claimIdLst = new ArrayList<String>();

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getClaimIdLst() {
        claimIdLst = claimFacade.getClaimIdList(userName, userGroup);
        if (claimIdLst.size() > 0) {
            refreshPage();
        }
        return claimIdLst;
    }

    private void refreshPage() {
        if (claimIdLst.size() > 0) {
            id = claimIdLst.get(0).toString();
            retrieveClaimDetails();
        }
    }

    public String approve() {
        claim = claimFacade.getClaim(id);
        claimFacade.approveClaim(claim);
        return "modified";
    }

    public String delete() {
        claim = claimFacade.getClaim(id);
        claimFacade.deleteClaim(claim);
        return "modified";
    }

    public String reject() {
        claim = claimFacade.getClaim(id);
        claimFacade.rejectClaim(claim);
        return "modified";
    }

    public String notifyClaimant() {
        claim = claimFacade.getClaim(id);
        claimFacade.notifyClaimant(claim);
        return "modified";
    }

    public void refreshClaimDetails(AjaxBehaviorEvent e) {
        retrieveClaimDetails();
    }

    private void retrieveClaimDetails() {
        try {
            claim = claimFacade.getClaim(id);
            this.carModel = claim.getCar_model();
            this.carRegNo = claim.getCarRegNo();
            this.carValue = claim.getCarValue();
            this.owner = claim.getOwner();
            this.type = claim.getType();
            this.status = claim.getStatus_desc();
        } catch (Exception e1) {
            handleException(e1);
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace(System.err);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public Double getCarValue() {
        return carValue;
    }

    public void setCarValue(Double carValue) {
        this.carValue = carValue;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAdmin() {
        if (userGroup == null) {
            userGroup = claimFacade.getUsrGrp();
        }
        if (userGroup!=null && userGroup.equalsIgnoreCase("ADMIN")) {
            return true;
        }
        return false;
    }

    /** Creates a new instance of EvaluationClaim */
    public EvaluationClaim() {
    }
}

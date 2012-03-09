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
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Anand
 */
@Named(value = "updateIncompleteClaim")
@RequestScoped
public class UpdateIncompleteClaim {

    @EJB
    ClaimsFacade claimFacade;
    private Claim claim;

    /** Creates a new instance of updateIncompleteClaim */
    public UpdateIncompleteClaim() {
    }
    List<String> claimIdLst = new ArrayList<String>();
    private String id;
    private String userName;
    private String userGroup;
    private String carRegNo;
    private Double carValue;
    private String carModel;
    private Integer carYear;

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

    public Integer getCarYear() {
        return carYear;
    }

    public void setCarYear(Integer carYear) {
        this.carYear = carYear;
    }

    public void setClaimIdLst(List<String> claimIdLst) {
        this.claimIdLst = claimIdLst;
    }

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

    public String update() {
        claim.setCarValue(carValue);
        claim.setCarRegNo(carRegNo);
        claim.setCar_model(carModel);
        claim.setCar_year(carYear);
        claimFacade.updateClaim(claim);
        return "updated";
    }

    public List<String> getClaimIdLst() {
        claimIdLst = claimFacade.getIncompleteClaimIdList(userName, userGroup);
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

    public void refreshClaimDetails(AjaxBehaviorEvent e) {
        retrieveClaimDetails();
    }

    private void retrieveClaimDetails() {
        claim = claimFacade.getClaim(id);
        this.carModel = claim.getCar_model();
        this.carRegNo = claim.getCarRegNo();
        this.carValue = claim.getCarValue();
        this.carYear = claim.getCar_year();
    }
}

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
@Named(value = "garageManager")
@RequestScoped
public class GarageManager {

    /** Creates a new instance of GarageManager */
    public GarageManager() {
    }
    @EJB
    ClaimsFacade claimFacade;
    private Claim claim;
    List<String> claimIdLst = new ArrayList<String>();
    private String id;
    private String userName;
    private String userGroup;
    private String carRegNo;
    private Double carRepairCost;
    private String carStatusDesc;
    private Integer carYear;

    public String getId() {
        return id;
    }

    public Double getCarRepairCost() {
        return carRepairCost;
    }

    public void setCarRepairCost(Double carRepairCost) {
        this.carRepairCost = carRepairCost;
    }

    public String getCarStatusDesc() {
        return carStatusDesc;
    }

    public void setCarStatusDesc(String carStatusDesc) {
        this.carStatusDesc = carStatusDesc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
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
        if(carStatusDesc!=null && carStatusDesc.startsWith("full")){
            claim.setCar_status(2);
        }else{
            claim.setCar_status(1);
        }
        claim.setStatus_code(5);
        claim.setCar_status_desc(carStatusDesc);
        claim.setCarRegNo(carRegNo);
        claim.setRepair_cost(carRepairCost);
        claim.setCar_year(carYear);
        claimFacade.update(claim);
        return "updated";
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

    public void refreshClaimDetails(AjaxBehaviorEvent e) {
        retrieveClaimDetails();
    }

    private void retrieveClaimDetails() {
        claim = claimFacade.getClaim(id);
        this.carRepairCost = claim.getRepair_Cost();
        this.carRegNo = claim.getCarRegNo();
        this.carStatusDesc = claim.getCar_status_desc();
        this.carYear = claim.getCar_year();
    }
}

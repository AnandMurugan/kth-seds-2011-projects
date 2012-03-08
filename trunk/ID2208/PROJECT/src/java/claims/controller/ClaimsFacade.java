/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.controller;

import claims.model.Claim;
import client.ClaimsClient;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Anand
 */
@Stateless
@LocalBean
public class ClaimsFacade {

    private ClaimsClient client;
    private final Double factor = 100000.00;
    private List<Claim> claimLst;

    public ClaimsFacade() {
        client = new ClaimsClient();
    }

    public Claim getClaim(String claimId) {
        return client.find_XML(Claim.class, claimId);
    }

    public void submitClaim(String carRegNo) {
        Claim claimObj = new Claim(carRegNo);
        claimObj.setType("simple");
        claimObj.setStatus_code(0);
        claimObj.setStatus_desc("initiate claim");
        client.create_XML(claimObj);
    }

    public void submitClaim(String carRegNo, Double carValue, String carModel, String owner, Integer carYear) {
        Claim claimObj = new Claim(carRegNo, carValue);
        claimObj.setCar_model(carModel);
        claimObj.setOwner(owner);
        claimObj.setCar_year(carYear);
        if (carValue != null) {
            if (carValue < factor) {
                claimObj.setType("simple");
            } else {
                claimObj.setType("complex");
            }
        } else {
            claimObj.setType("simple");
        }
        claimObj.setStatus_code(0);
        claimObj.setStatus_desc("initiate claim");
        client.create_XML(claimObj);
    }

    public List<Claim> getClaimsList() {
        return client.findAll_XML(Claim.class);
    }

    public List<String> getClaimIdList() {
        List<String> claimIdLst = new ArrayList<String>();
        claimLst = client.findAll_XML(Claim.class);
        if (claimLst != null && claimLst.size() > 0) {
            for (Claim claim : claimLst) {
                claimIdLst.add(claim.getId().toString());
            }
        }
        return claimIdLst;
    }

    public void approveClaim(Claim claim) {
        claim.setStatus_code(4);
        claim.setStatus_desc("approved");
        client.edit_XML(claim);
    }

    public void rejectClaim(Claim claim) {
        claim.setStatus_code(3);
        claim.setStatus_desc("rejected");
        client.edit_XML(claim);
    }

    public void notifyClaimant(Claim claim) {
        Long id = claim.getId();
        claim.setStatus_code(1);
        claim.setStatus_desc("incomplete");
        client.edit_XML(claim);

    }
}

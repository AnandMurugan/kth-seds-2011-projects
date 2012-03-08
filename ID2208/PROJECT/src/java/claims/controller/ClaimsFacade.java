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
    private String usrNm;
    private String usrGrp;

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

    public List<Claim> getClaimsList(String userName, String userGroup) {
         if(userName!=null){
            usrNm = userName;
        }
        if(userGroup!=null){
            usrGrp = userGroup;
        }
        List<Claim> resultLst = new ArrayList<Claim>();
        List<Claim> tempLst = new ArrayList<Claim>();
        tempLst = client.findAll_XML(Claim.class);
        if (usrGrp.equalsIgnoreCase("CUSTOMER")) {
            for (Claim claim : tempLst) {
                if (claim.getOwner().equalsIgnoreCase(usrNm)) {
                    resultLst.add(claim);
                }
            }
        } else if (usrGrp.equalsIgnoreCase("junior")) {
            for (Claim claim : tempLst) {
                if (claim.getType().equalsIgnoreCase("simple")) {
                    resultLst.add(claim);
                }
            }
        } else if (usrGrp.equalsIgnoreCase("senior")) {
            for (Claim claim : tempLst) {
                if (claim.getType().equalsIgnoreCase("complex")) {
                    resultLst.add(claim);
                }
            }
        } else if (usrGrp.equalsIgnoreCase("garage")) {
            for (Claim claim : tempLst) {
                if (claim.getStatus_code() == 4) {
                    resultLst.add(claim);
                }
            }
        } else if (usrGrp.equalsIgnoreCase("admin")) {
            resultLst = tempLst;
        }
        return resultLst;
    }

    public List<String> getClaimIdList(String userName, String userGroup) {
        if(userName!=null){
            usrNm = userName;
        }
        if(userGroup!=null){
            usrGrp = userGroup;
        }
        List<String> claimIdLst = new ArrayList<String>();
        claimLst = client.findAll_XML(Claim.class);
        if (claimLst != null && claimLst.size() > 0) {
            if (usrGrp.equalsIgnoreCase("CUSTOMER")) {
                for (Claim claim : claimLst) {
                    if (claim.getOwner().equalsIgnoreCase(usrNm)) {
                        claimIdLst.add(claim.getId().toString());
                    }
                }
            } else if (usrGrp.equalsIgnoreCase("junior")) {
                for (Claim claim : claimLst) {
                    if (claim.getType().equalsIgnoreCase("simple")) {
                        claimIdLst.add(claim.getId().toString());
                    }
                }
            } else if (usrGrp.equalsIgnoreCase("senior")) {
                for (Claim claim : claimLst) {
                    if (claim.getType().equalsIgnoreCase("complex")) {
                        claimIdLst.add(claim.getId().toString());
                    }
                }
            } else if (usrGrp.equalsIgnoreCase("garage")) {
                for (Claim claim : claimLst) {
                    if (claim.getStatus_code() == 4) {
                        claimIdLst.add(claim.getId().toString());
                    }
                }
            } else if (usrGrp.equalsIgnoreCase("admin")) {
                for (Claim claim : claimLst) {
                    claimIdLst.add(claim.getId().toString());
                }
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

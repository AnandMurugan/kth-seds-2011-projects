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
import service.*;

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
    AuthorizationWebService pepClient;

    public String getUsrGrp() {
        return usrGrp;
    }

    public ClaimsFacade() {
        client = new ClaimsClient();
        pepClient = new AuthorizationWebService_Service().getAuthorizationWebServicePort();
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

    public void submitClaim(String carRegNo, Double carValue, String carModel, String owner, Integer carYear, Double damageCost) {
        Claim claimObj = new Claim(carRegNo, carValue);
        claimObj.setCar_model(carModel);
        claimObj.setOwner(owner);
        claimObj.setCar_year(carYear);
        claimObj.setDamageCost(damageCost);
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
        if (userName != null) {
            usrNm = userName;
        }
        if (userGroup != null) {
            usrGrp = userGroup;
        }
        List<Claim> resultLst = new ArrayList<Claim>();
        List<Claim> tempLst = new ArrayList<Claim>();

        // Authorize the request
        if(!authorizeReq("read")){
            return resultLst;
        }

        //GET resource
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
        if (userName != null) {
            usrNm = userName;
        }
        if (userGroup != null) {
            usrGrp = userGroup;
        }
        List<String> claimIdLst = new ArrayList<String>();
        // Authorize the request
        if(!authorizeReq("read")){
            return claimIdLst;
        }
        //GET resource
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
        claim.setCar_status(0);
        claim.setCar_status_desc("sent to garage");
        // Authorize the request
        if (!authorizeReq("update")) {
            return;
        }
        client.edit_XML(claim);
    }

    public void rejectClaim(Claim claim) {
        claim.setStatus_code(3);
        claim.setStatus_desc("rejected");
        // Authorize the request
        if (!authorizeReq("update")) {
            return;
        }
        client.edit_XML(claim);
    }

    public void notifyClaimant(Claim claim) {
        Long id = claim.getId();
        claim.setStatus_code(1);
        claim.setStatus_desc("incomplete");
        // Authorize the request
        if (!authorizeReq("update")) {
            return;
        }

        client.edit_XML(claim);
    }

    public void updateClaim(Claim claim) {
        claim.setStatus_code(2);
        claim.setStatus_desc("complete");
        // Authorize the request
        if (!authorizeReq("update")) {
            return;
        }
        client.edit_XML(claim);
    }

    public void update(Claim claim) {

        // Authorize the request
        if (!authorizeReq("update")) {
            return;
        }

        client.edit_XML(claim);
    }

    public List<String> getIncompleteClaimIdList(String userName, String userGroup) {
        if (userName != null) {
            usrNm = userName;
        }
        if (userGroup != null) {
            usrGrp = userGroup;
        }
        List<String> claimIdLst = new ArrayList<String>();

        // Authorize the request
        if (!authorizeReq("read")) {
            return claimIdLst;
        }

        claimLst = client.findAll_XML(Claim.class);
        if (claimLst != null && claimLst.size() > 0) {
            if (usrGrp.equalsIgnoreCase("CUSTOMER")) {
                for (Claim claim : claimLst) {
                    if (claim.getOwner().equalsIgnoreCase(usrNm) && claim.getStatus_code() == 1) {
                        claimIdLst.add(claim.getId().toString());
                    }
                }
            }
        }
        return claimIdLst;
    }

    public void deleteClaim(Claim claim) {
        // Authorize the request
        if (!authorizeReq("delete")) {
            return;
        }
        client.remove(claim.getId().toString());
    }

    public boolean authorizeReq(String operation) {
        if (usrNm != null) {
            AuthorizationRequest request = new AuthorizationRequest();
            request.setUserId(usrNm.toLowerCase() + "@insurance.se");
            request.setResourceId("http://localhost:8080/ClaimsInsurance/resources/claims");
            request.setActionId(operation);
            request.setGroupAdminId("admin@insurance.se");
            if (usrGrp.equalsIgnoreCase("junior") || usrGrp.equalsIgnoreCase("senior")) {
                request.setGroupId("Officers");
            } else {
                request.setGroupId(usrGrp.toLowerCase());
            }
            AuthorizationResponse response = pepClient.evaluate(request);
            if (response != null) {
                System.out.println("response - " + response.getDecision());
                if ((response.getDecision().startsWith("Permit"))) {
                    return true;
                }
            }
        } else {
            System.out.println("[UserName] is NULL");
        }
        return false;
    }
    // Authorize the request
}

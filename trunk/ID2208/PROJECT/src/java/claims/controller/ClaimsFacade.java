/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.controller;

import claims.model.Claim;
import client.ClaimsClient;
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
    
    public ClaimsFacade() {
        client = new ClaimsClient();
    }

    public Claim getClaim(String claimId){
        return client.find_XML(Claim.class, claimId);
    }
    
    public void submitClaim(String carRegNo) {
        Claim claimObj = new Claim(carRegNo);
        client.create_XML(claimObj);
    }

    public void submitClaim(String carRegNo, Double carValue, String carModel, String owner, Integer carYear) {
        Claim claimObj = new Claim(carRegNo, carValue);
        claimObj.setCar_model(carModel);
        claimObj.setOwner(owner);
        claimObj.setCar_year(carYear);
        client.create_XML(claimObj);
    }

    public List<Claim> getClaimsList() {
        return client.findAll_XML(Claim.class);
    }
}

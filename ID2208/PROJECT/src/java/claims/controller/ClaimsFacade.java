/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.controller;

import claims.model.Claim;
import client.ClaimsClient;
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

    public void submitClaim(String carRegNo) {
        Claim claimObj = new Claim(carRegNo);
        client.create_XML(claimObj);
    }

    public void submitClaim(String carRegNo, Double carValue) {
        Claim claimObj = new Claim(carRegNo, carValue);
        client.create_XML(claimObj);

    }
}

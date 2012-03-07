/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import bean.Claim;
import com.sun.jersey.api.client.ClientResponse;

/**
 *
 * @author Anand
 */
public class TestClient {
    
    public static void main(String[] args){
        Claim claim = new Claim("SWE50 7890", 100000.00);
        ClaimsClient client = new ClaimsClient();
        System.out.println("===== Create claim =====");
        ClientResponse res = client.putClaim(claim);
        
        //System.out.println("===== All Claims =====");
        //GenericType<JAXBElement<Claim>> generic = new GenericType<JAXBElement<Claim>>() {
        //};
        //JAXBElement<Claim> jaxbContact =  client.getClaim(claim.getId());
        //claim = client.getClaim(claim.getId().toString());

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xacml.client;

import service.AuthorizationRequest;
import service.AuthorizationResponse;
import service.PDPAuthorizationService;
import service.PDPAuthorizationService_Service;

/**
 *
 * @author julio
 */
public class TestPDP {
    public static void main(String args[]){
        PDPAuthorizationService pdpClient = new  PDPAuthorizationService_Service().getPDPAuthorizationServicePort();
        	ObjectFactory objFactory = new ObjectFactory();
                //Create a new request
		AuthorizationRequest request = objFactory.createAuthorizationRequest();
                /*request.setUserId("shahab@kth.se");
		request.setGroupId("Developers");
		request.setGroupAdminId("admin@kth.se");
		request.setResourceId("http://server.example.com/code/docs/developer-guide.html");
		request.setActionId("read");	*/
                
		request.setUserId("junior@insurance.se");
		request.setGroupId("Officers");
		request.setGroupAdminId("admin@insurance.se");
		request.setResourceId("http://localhost:8080/ClaimsInsurance/resources/claims");
		request.setActionId("delete");		
		
		//Evaluate the request
                AuthorizationResponse response = pdpClient.evaluate( request );
		
		//Print out the result
		System.out.println("StatusCode: " + response.getStatusCode());
		System.out.println("ResourceID: " + response.getResourceID());
		System.out.println("Decision  : " + response.getDecision());
		//System.out.println("Permit    : " + response.isPermit());		
    }
}

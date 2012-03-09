/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author julio
 */
@WebService(serviceName = "PEPService")
public class PEPService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "evaluateSubject")
    public AuthorizationResponse evaluateSubject(AuthorizationRequest request) {
        AuthorizationResponse response = null;
        PDPAuthorizationService pdpClient = new  PDPAuthorizationService_Service().getPDPAuthorizationServicePort();
        
        if (request != null) {
            response = pdpClient.evaluate(request);
        }
        
        return response;
    }
}

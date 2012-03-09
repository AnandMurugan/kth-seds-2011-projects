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
@WebService(serviceName = "PDPAuthorizationService")
public class PDPAuthorizationService {
    
    /** Evaluates an authorization request */
    @WebMethod(operationName = "evaluate")
    public 
    AuthorizationResponse evaluate(AuthorizationRequest request) {
        PDP pdp = new PDP("C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2208 Programming Web Services\\Project\\XacmlServer\\policy");
        AuthorizationResponse result = null;
        if (request != null) {
            result = pdp.evaluate(request);
            return result;
        }

        request = new AuthorizationRequest();
        request.setUserID("shahab@kth.se");
        request.setGroupID("Developers");
        request.setGroupAdminID("admin@kth.se");
        request.setResourceID("http://server.example.com/code/docs/developer-guide.html");
        request.setActionID("read");

        result = pdp.evaluate(request);


        //pdp.evaluate(request);
        return result;
    }
}

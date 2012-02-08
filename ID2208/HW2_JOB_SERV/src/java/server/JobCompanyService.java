/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import employment.client.EmploymentOfficeService;
import employment.client.EmploymentOfficeService_Service;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author julio
 */
@WebService(serviceName = "JobCompanyService")
public class JobCompanyService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "getProfile")
    public String getProfile(@WebParam(name = "profile") String profile) {
        EmploymentOfficeService service = new EmploymentOfficeService_Service().getEmploymentOfficeServicePort();
        String test = service.hello("prueba");
        return "Generated Profile: " + profile + test + " !";
    }
}

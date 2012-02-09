/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author julio
 */
@WebService(serviceName = "EmploymentService")
@HandlerChain(file = "EmploymentService_handler.xml")
public class EmploymentService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "getEmploymentRecords")
    public String getEmploymentRecords(@WebParam(name = "personalNumber") String personalNumber) {
        return "record of " + personalNumber + " !";
    }
    
}

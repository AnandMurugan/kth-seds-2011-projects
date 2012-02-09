/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author julio
 */
@WebService(serviceName = "CompaniesDBService")
@HandlerChain(file = "CompaniesDBService_handler.xml")
public class CompaniesDBService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "getCompaniesXml")
    public String getCompaniesXml() {
        return "done !";
    }
}

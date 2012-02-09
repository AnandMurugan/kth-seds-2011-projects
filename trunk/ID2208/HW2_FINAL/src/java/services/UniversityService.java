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
@WebService(serviceName = "UniversityService")
@HandlerChain(file = "UniversityService_handler.xml")
public class UniversityService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "getTranscript")
    public String getTranscript(@WebParam(name = "personalNumber") String personalNumber) {
        return "Transcript sent" + personalNumber + " !";
    }
}

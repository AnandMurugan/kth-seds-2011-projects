/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import employment.client.EmploymentOfficeService;
import employment.client.EmploymentOfficeService_Service;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author julio
 */
@WebService(serviceName = "JobCompanyService")
@HandlerChain(file = "JobCompanyService_handler.xml")
public class JobCompanyService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "getProfile")
    public String getProfile(@WebParam(name = "personalNumber") String personalNumber) {
        EmploymentOfficeService service = new EmploymentOfficeService_Service().getEmploymentOfficeServicePort();
        String test = service.hello("prueba");
        return personalNumber;
    }

}

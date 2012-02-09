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
@WebService(serviceName = "RecruitingCompanyService")
@HandlerChain(file = "RecruitingCompanyService_handler.xml")
public class RecruitingCompanyService {
    /** This is a sample web service operation */
    @WebMethod(operationName = "getAdvertisedJobs")
    public String getAdvertisedJobs(@WebParam(name = "keywords") String keywords) {
        return keywords;
    }
}

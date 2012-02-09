
package employment.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebService(name = "EmploymentService", targetNamespace = "http://employment.service/employment")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface EmploymentService {


    /**
     * 
     * @param personalNumber
     * @return
     *     returns employment.client.GetEmploymentRecordsResponse.Return
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEmploymentRecords", targetNamespace = "http://employment.service/employment", className = "employment.client.GetEmploymentRecords")
    @ResponseWrapper(localName = "getEmploymentRecordsResponse", targetNamespace = "http://employment.service/employment", className = "employment.client.GetEmploymentRecordsResponse")
    @Action(input = "http://employment.service/employment/EmploymentService/getEmploymentRecordsRequest", output = "http://employment.service/employment/EmploymentService/getEmploymentRecordsResponse")
    public employment.client.GetEmploymentRecordsResponse.Return getEmploymentRecords(
        @WebParam(name = "personalNumber", targetNamespace = "")
        String personalNumber);

}

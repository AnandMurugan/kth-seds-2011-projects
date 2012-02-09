/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package employment.service;

import employmentpo.Records;
import employmentpo.ObjectFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Julio
 */
@WebService(serviceName = "EmploymentService",
portName = "EmploymentWebServicePort",
targetNamespace = "http://employment.service/employment")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class EmploymentService {
    //private static final String filePath = "D:/NetBeansProjects/PWS/WSP_HW2_Services/src/java/xml/transcript.xml";
    private static final String filePath = "src/xml/employmentRecordDB.xml";
    private static final String JAXB_EMPLOYMENT_CONTEXT = "employmentpo";

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "getEmploymentRecords")
    public Records.Record getEmploymentRecords(@WebParam(name = "personalNumber") String personalNumber) {
        Records.Record record = null;
        try {
            System.out.println("current directory: " + new File(".").getCanonicalPath());
            JAXBContext jcontext = JAXBContext.newInstance("employmentpo");
            ObjectFactory objFactory = new ObjectFactory();
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            //unmarshaller.setValidating(true);
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

            Records records = (Records) unmarshaller.unmarshal(new File(filePath));
            List<Records.Record> employeeRecords = records.getRecord();
            System.out.println("Number of records: " + employeeRecords.size());
            for (int i = 0; i < employeeRecords.size(); i++) {
                if (employeeRecords.get(i).getPersonalNumber().equals(personalNumber)) {
                    //System.out.println(employeeRecords.get(i).toString());
                    return employeeRecords.get(i);
                }
            }

        } catch (JAXBException | IOException ex) {
            Logger.getLogger(EmploymentService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
}

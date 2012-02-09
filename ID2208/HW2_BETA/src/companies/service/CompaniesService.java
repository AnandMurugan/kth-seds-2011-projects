/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package companies.service;

import companypo.Companies;

import companypo.ObjectFactory;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.DOMParser;

/**
 *
 * @author Julio
 */
@WebService(serviceName = "CompaniesService",
portName = "CompaniesWebServicePort",
targetNamespace = "http://companies.service/companies")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class CompaniesService {
    //private static final String filePath = "D:/NetBeansProjects/PWS/WSP_HW2_Services/src/java/xml/transcript.xml";
    private static final String filePath = "src/xml/companiesDatabase.xml";
    private static final String JAXB_COMPANIES_CONTEXT = "companypo";

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "getCompanies")
    public Companies getCompanies() {
        try {
            //System.out.println("current directory: " + new File(".").getCanonicalPath());
            JAXBContext jcontext = JAXBContext.newInstance(JAXB_COMPANIES_CONTEXT);
            ObjectFactory objFactory = new ObjectFactory();
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            //unmarshaller.setValidating(true);
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

            Companies companies = (Companies) unmarshaller.unmarshal(new File(filePath));
            List<Companies.CompanyInfo> companyInfoList = companies.getCompanyInfo();
            for (Companies.CompanyInfo info : companyInfoList) {
                System.out.println("The company name is: " + info.getName());
            }
            return companies;
            //List<Companies.CompanyInfo> companyInfoList = companies.getCompanyInfo();
            //System.out.println("Number of records: " + companyInfoList.size());
            //return companyInfoList;

        } catch (JAXBException ex) {
            Logger.getLogger(CompaniesService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @WebMethod(operationName = "getCompaniesXml")
    public Element getCompaniesXml() {

        try {
            String schemaLanguage = "http://www.w3.org/2001/XMLSchema";
            String schemaPath = "src/schemas/companyInfoXmlSchema.xsd";
            Document profileXmlDoc = DOMParser.getDOMDocument(schemaLanguage, schemaPath, filePath);

            return profileXmlDoc.getDocumentElement();
        } catch (Exception ex) {
            Logger.getLogger(CompaniesService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

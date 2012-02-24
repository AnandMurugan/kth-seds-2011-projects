/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import clients.CompaniesClient;
import clients.EmploymentClient;
import clients.TranscriptClient;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import utils.ApplicantProfileGenerator;
import utils.Util;

/**
 * REST Web Service
 *
 * @author julio
 */
@Path("Profile")
public class ProfileResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of ProfileResource */
    public ProfileResource() {
    }

    /**
     * Retrieves representation of an instance of services.ProfileResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        String res = "<Result>No result</Result>";
        
        try {
            // schema paths:
            String profileSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\applicantProfileXmlSchema.xsd";
            String companyInfoSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\companyInfoXmlSchema.xsd";
            String employmentRecordSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\employmentRecordXmlSchema.xsd";
            String resumeSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\resume.xsd";
            String transcriptSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\transcriptSchema.xsd";
            
            //XSLT file path
            String xsltFilePath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\applicantprofileTransform.xsl";
            // xml file paths:
            
            String companiesXmlFilePath = "companies.xml";
            String employmentXmlFilePath = "employment.xml";
            String transcriptXmlFilePath = "transcript.xml";
            String profileXmlFilePath = "profile.xml";
            String resumeXmlFilePath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\resume.xml";
            
            //TODO return proper representation object
            TranscriptClient transcriptClient = new TranscriptClient("881201-1234");
            Util.writeFile(transcriptClient.getXml(), transcriptXmlFilePath);
            
            CompaniesClient companiesClient = new CompaniesClient();
            Util.writeFile(companiesClient.getXml(), companiesXmlFilePath);
            
            EmploymentClient employmentClient = new EmploymentClient();
            Util.writeFile(employmentClient.getXml(), employmentXmlFilePath);
            
            ApplicantProfileGenerator generator = new ApplicantProfileGenerator(
                    profileSchemaPath,
                    companyInfoSchemaPath, 
                    employmentRecordSchemaPath, 
                    resumeSchemaPath, 
                    transcriptSchemaPath);
            
            
            generator.Generate(companiesXmlFilePath,
                    employmentXmlFilePath,
                    resumeXmlFilePath,
                    transcriptXmlFilePath,
                    profileXmlFilePath,
                    xsltFilePath,
                    resumeXmlFilePath,
                    companyInfoSchemaPath);
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(profileXmlFilePath));
            StringWriter stw = new StringWriter(); 
            Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
            serializer.transform(new DOMSource(doc), new StreamResult(stw)); 
            res = stw.toString(); 
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ex.printStackTrace();
        }
        
        return res;
    }

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public String generateProfile(String resumeXML) {
        String res = "<Result>no result</Result>";
        
        
        try {
            // schema paths:
            String profileSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\applicantProfileXmlSchema.xsd";
            String companyInfoSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\companyInfoXmlSchema.xsd";
            String employmentRecordSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\employmentRecordXmlSchema.xsd";
            String resumeSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\resume.xsd";
            String transcriptSchemaPath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\transcriptSchema.xsd";
            
            //XSLT file path
            String xsltFilePath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\applicantprofileTransform.xsl";
            // xml file paths:
            
            String companiesXmlFilePath = "companies.xml";
            String employmentXmlFilePath = "employment.xml";
            String transcriptXmlFilePath = "transcript.xml";
            String profileXmlFilePath = "profile.xml";
            String resumeXmlFilePath = "resume.xml";
            
            //TODO return proper representation object
            
            Util.writeFile(resumeXML, resumeXmlFilePath);
            
            TranscriptClient transcriptClient = new TranscriptClient("881201-1234");
            Util.writeFile(transcriptClient.getXml(), transcriptXmlFilePath);
            
            CompaniesClient companiesClient = new CompaniesClient();
            Util.writeFile(companiesClient.getXml(), companiesXmlFilePath);
            
            EmploymentClient employmentClient = new EmploymentClient();
            Util.writeFile(employmentClient.getXml(), employmentXmlFilePath);
            
            ApplicantProfileGenerator generator = new ApplicantProfileGenerator(
                    profileSchemaPath,
                    companyInfoSchemaPath, 
                    employmentRecordSchemaPath, 
                    resumeSchemaPath, 
                    transcriptSchemaPath);
            
            
            generator.Generate(companiesXmlFilePath,
                    employmentXmlFilePath,
                    resumeXmlFilePath,
                    transcriptXmlFilePath,
                    profileXmlFilePath,
                    xsltFilePath,
                    resumeXmlFilePath,
                    companyInfoSchemaPath);
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(profileXmlFilePath));
            StringWriter stw = new StringWriter(); 
            Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
            serializer.transform(new DOMSource(doc), new StreamResult(stw)); 
            res = stw.toString(); 
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ex.printStackTrace();
        }
        
        return res;
    }
}

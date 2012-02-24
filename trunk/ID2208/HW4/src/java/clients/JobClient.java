/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import services.TranscriptResource;

/**
 *
 * @author julio
 */
public class JobClient {
    public static void main(String[] args) {
        ProfileClient profileClient = new ProfileClient();
        CompaniesClient companiesClient = new CompaniesClient();
        EmploymentClient employmentClient = new EmploymentClient();

        // Resume to upload
        String resumeXML = "";
        String resumeFilePath = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\resume.xml";
        try {
            String xmlFile = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\companiesDatabase.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
            StringWriter stw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stw));
            resumeXML = stw.toString();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            Logger.getLogger(TranscriptResource.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (!resumeXML.isEmpty()) {

            TranscriptClient transClient = new TranscriptClient("881201-1234");
            System.out.println("COMPANIES XML: " + companiesClient.getXml());
            System.out.println("EMPLOYMENT XML: " + employmentClient.getXml());
            System.out.println("transcript XML: " + transClient.getXml());
            System.out.println("PROFILE XML: " + profileClient.generateProfile(new File(resumeFilePath)));
        } else {
            System.out.println("Error when parsing the Resume xml file.");
        }
    }
}

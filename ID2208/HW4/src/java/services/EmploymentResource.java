/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
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

/**
 * REST Web Service
 *
 * @author julio
 */
@Path("employment")
public class EmploymentResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of EmploymentResource */
    public EmploymentResource() {
    }

    /**
     * Retrieves representation of an instance of services.EmploymentResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
         String res = "<Result>No Result</Result>";
        try {
            //TODO return proper representation object
            String xmlFile = "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2203 Distributed Systems, Advanced Course\\Homeworks\\HW2\\PWS_HW4\\src\\java\\xml\\employmentRecordDB.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
            StringWriter stw = new StringWriter(); 
            Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
            serializer.transform(new DOMSource(doc), new StreamResult(stw)); 
            res = stw.toString(); 
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            Logger.getLogger(TranscriptResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }
}

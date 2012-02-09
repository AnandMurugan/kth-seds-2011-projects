/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package university.service;

import java.io.File;
import java.io.IOException;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import transcript.ObjectFactory;
import transcript.Transcript;

/**
 *
 * @author Anand
 */
@WebService(serviceName = "UniversityWS",
portName = "UniversityWebServicePort",
targetNamespace = "http://university.service/university")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class UniversityWS {

    //private static final String filePath = "D:/NetBeansProjects/PWS/WSP_HW2_Services/src/java/xml/transcript.xml";
    private static final String filePath = "src/xml/transcript.xml";

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "retrieveTranscript")
    public Transcript retrieveTranscript(@WebParam(name = "personalNumber") String txt) {
        Transcript record = null;
        try {
            JAXBContext jcontext = JAXBContext.newInstance("transcript");
            ObjectFactory objFactory = new ObjectFactory();
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            record = (Transcript) unmarshaller.unmarshal(new File(filePath));
            System.out.println(record.toString());
            return record;
        } catch (JAXBException ex) {
            Logger.getLogger(UniversityWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
}

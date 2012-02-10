/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.clients;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import utils.ApplicantProfileGenerator;
import org.w3c.dom.Element;

/**
 *
 * @author julio
 */
public class EmploymentServiceClient {
    String personalNumber;
    public EmploymentServiceClient(String personalNumber) {
        this.personalNumber = personalNumber;
    }
    
    public static void main(String[] args) {
        try {
            EmploymentServiceClient client = new EmploymentServiceClient("881201-1234");
            
            Element transcriptElem = client.getTranscript();
            Element companiesElem = client.getCompaniesDB();
            Element employementRecordsElem = client.getEmploymentRecord();
            
            Source transcriptSource = new DOMSource(transcriptElem);
            Source companiesSource = new DOMSource(companiesElem);
            Source employmentSource = new DOMSource(employementRecordsElem);
            // Write the DOM document to the file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(transcriptSource, new StreamResult(new File("src/xml/transcript.xml")));
            transformer.transform(companiesSource, new StreamResult(new File("src/xml/companies.xml")));
            transformer.transform(employmentSource, new StreamResult(new File("src/xml/employment.xml")));
            
            ApplicantProfileGenerator generator = new ApplicantProfileGenerator();
            String applicantFilePath = "src/xml/userProfile.xml";
            generator.Generate("src/xml/companies.xml", "src/xml/employment.xml","src/xml/resume.xml", "src/xml/transcript.xml", applicantFilePath);        
           
        } catch (Exception ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Element getTranscript() {
        try {
            //get insance of SOAP factory
            MessageFactory factory = MessageFactory.newInstance();
            // get empty soap message
            SOAPMessage message = factory.createMessage();
            // get Part piece of the message
            SOAPPart soapPart = message.getSOAPPart();
            // get Envelop piece of the message
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // access the empty header of the message
            SOAPHeader header = envelope.getHeader();

            SOAPBody body = envelope.getBody();
            QName bodyName = new QName("http://services/",
                    "getTranscript", "ns2");
            SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
            QName name = new QName("personalNumber");
            SOAPElement symbol = bodyElement.addChildElement(name);
            symbol.addTextNode("1247");

            SOAPConnectionFactory soapConnectionFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnectionFactory.createConnection();
            message.writeTo(System.out);
            System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            java.net.URL endpoint = new URL("http://localhost:8080/WSP_HW2_FINAL/UniversityService");
            SOAPMessage response = connection.call(message, endpoint);
            System.out.println("Response message: ");
            response.writeTo(System.out);
            System.out.println();

            SOAPEnvelope responseEnvelope = response.getSOAPPart().getEnvelope();
            SOAPBody soapBody = responseEnvelope.getBody();
            return (Element) responseEnvelope.getBody().getChildNodes().item(1);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (SOAPException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public Element getEmploymentRecord() {
        try {
            //get insance of SOAP factory
            MessageFactory factory = MessageFactory.newInstance();
            // get empty soap message
            SOAPMessage message = factory.createMessage();
            // get Part piece of the message
            SOAPPart soapPart = message.getSOAPPart();
            // get Envelop piece of the message
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // access the empty header of the message
            SOAPHeader header = envelope.getHeader();

            SOAPBody body = envelope.getBody();
            QName bodyName = new QName("http://services/",
                    "getEmploymentRecords", "ns2");
            SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
            QName name = new QName("personalNumber");
            SOAPElement symbol = bodyElement.addChildElement(name);
            symbol.addTextNode("1247");

            SOAPConnectionFactory soapConnectionFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnectionFactory.createConnection();
            message.writeTo(System.out);
            System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            java.net.URL endpoint = new URL("http://localhost:8080/WSP_HW2_FINAL/EmploymentService");
            SOAPMessage response = connection.call(message, endpoint);
            System.out.println("Response message: ");
            response.writeTo(System.out);
            System.out.println();

            SOAPEnvelope responseEnvelope = response.getSOAPPart().getEnvelope();
            SOAPBody soapBody = responseEnvelope.getBody();
            return (Element) responseEnvelope.getBody().getChildNodes().item(1);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (SOAPException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public Element getCompaniesDB() {
        try {
            //get insance of SOAP factory
            MessageFactory factory = MessageFactory.newInstance();
            // get empty soap message
            SOAPMessage message = factory.createMessage();
            // get Part piece of the message
            SOAPPart soapPart = message.getSOAPPart();
            // get Envelop piece of the message
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // access the empty header of the message
            SOAPHeader header = envelope.getHeader();

            SOAPBody body = envelope.getBody();
            QName bodyName = new QName("http://services/",
                    "getCompaniesXml", "ns2");
            SOAPBodyElement bodyElement = body.addBodyElement(bodyName);

            SOAPConnectionFactory soapConnectionFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnectionFactory.createConnection();
            message.writeTo(System.out);
            System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            java.net.URL endpoint = new URL("http://localhost:8080/WSP_HW2_FINAL/CompaniesDBService");
            SOAPMessage response = connection.call(message, endpoint);
            System.out.println("Response message: ");
            response.writeTo(System.out);
            System.out.println();

            SOAPEnvelope responseEnvelope = response.getSOAPPart().getEnvelope();
            SOAPBody soapBody = responseEnvelope.getBody();
            return (Element) responseEnvelope.getBody().getChildNodes().item(1);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (SOAPException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}

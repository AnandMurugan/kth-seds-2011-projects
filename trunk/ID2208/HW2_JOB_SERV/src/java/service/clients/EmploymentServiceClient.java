/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.registry.JAXRException;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import publish.services.BusinessQuery;
import publish.services.RegisterService;
import utils.ApplicantProfileGenerator;

/**
 *
 * @author julio
 */
public class EmploymentServiceClient {

    String personalNumber;
    private static String resource_home = "D:/NetBeansProjects/PWS/WSP_HW2_JOB_SERV/src/java";
    private final String keyword = "Automobile"; //Automobile, IT

    public EmploymentServiceClient(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    private static void registerServices() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(resource_home + "/publish.properties"));
            RegisterService register = new RegisterService(properties);
            register.publishUniversityService();
            register.publishEmploymentService();
            register.publishRecruitmentService();
            register.publishCompanyDBService();
        } catch (IOException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        try {
            Element transcriptElem, companiesElem, employementRecordsElem, advJobsElem;
            //Source transcriptSource = new DOMSource;
            Source transcriptSource = null;
            Source companiesSource = null;
            Source employmentSource = null;
            Source advertisedJobs = null;
            EmploymentServiceClient client = new EmploymentServiceClient("881201-1234");
            
            //registerServices();

            String Url;
            BusinessQuery queryObj = new BusinessQuery();
            Url = queryObj.executeQuery("%Univ%");
            //System.out.println("url:"+Url);
            if (Url != null) {
                //System.out.println("University service found");
                transcriptElem = client.getTranscript(Url);
                transcriptSource = new DOMSource(transcriptElem);
            }

            Url = queryObj.executeQuery("%Company%");
            //System.out.println("url:"+Url);
            if (Url != null) {
                //System.out.println("Company service found");
                companiesElem = client.getCompaniesDB(Url);
                companiesSource = new DOMSource(companiesElem);
            }

            Url = queryObj.executeQuery("%Employment%");
            //System.out.println("url:"+Url);
            if (Url != null) {
                //System.out.println("Employment service found");
                employementRecordsElem = client.getEmploymentRecord(Url);
                employmentSource = new DOMSource(employementRecordsElem);
            }
            Url = queryObj.findByClassification("ntis-gov:naics", "Recruitment services", "517221");
            //System.out.println("url:"+Url);
            if (Url != null) {
                //System.out.println("Employment service found");
                advJobsElem = client.getJobsAdvertised(Url);
                advertisedJobs = new DOMSource(advJobsElem);
            }

            // Write the DOM document to the file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(transcriptSource, new StreamResult(new File("src/java/xml/transcript.xml")));
            transformer.transform(companiesSource, new StreamResult(new File("src/java/xml/companies.xml")));
            transformer.transform(employmentSource, new StreamResult(new File("src/java/xml/employment.xml")));
            transformer.transform(advertisedJobs, new StreamResult(new File("src/java/xml/jobsMatchingKeyword.xml")));
            //String applicantFilePath = "src/java/xml/userProfile.xml";
            //String xsltFilePath = "src/java/xml/applicantprofileTransform.xsl";
            //String resumeFilePath = "src/java/xml/resume.xml";
            String profileSchemaFilepath = resource_home + "/schemas/applicantProfileXmlSchema.xsd";
            String companyInfoSchemaPath = resource_home + "/schemas/companyInfoXmlSchema.xsd";
            String employmentRecordSchemaPath = resource_home + "/schemas/employmentRecordXmlSchema.xsd";
            String resumeSchemaPath = resource_home + "/schemas/resume.xsd";
            String transcriptSchemaPath = resource_home + "/schemas/transcriptSchema.xsd";

            ApplicantProfileGenerator generator = new ApplicantProfileGenerator(profileSchemaFilepath, companyInfoSchemaPath, employmentRecordSchemaPath, resumeSchemaPath, transcriptSchemaPath);
            String applicantFilePath = resource_home + "/xml/userProfile.xml";
            String advJobsFilePath = resource_home + "/xml/jobsMatchingKeyword.xml";
            String xsltFilePath = resource_home + "/xml/applicantprofileTransform.xsl";
            String resumeFilePath = resource_home + "/xml/resume.xml";
            generator.Generate(resource_home + "/xml/companies.xml", resource_home + "/xml/employment.xml", resource_home + "/xml/resume.xml", resource_home + "/xml/transcript.xml", applicantFilePath, xsltFilePath, resumeFilePath);
            boolean match = false;
            match = client.matchAdvJobsWithProfile(applicantFilePath, advJobsFilePath);
            if (match) {
                System.out.println("<<<<<<<<<<<<Applicant profile MATCHES the advertised jobs.>>>>>>>>>>>>");
            } else {
                System.out.println("<<<<<<<<<<<<Applicant profile DOES NOT MATCH the advertised jobs.>>>>>>>>>>>>");
            }

        } catch (JAXRException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerFactoryConfigurationError | TransformerException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Element getTranscript(String Url) {
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
            //System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            //System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            //System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            java.net.URL endpoint = new URL(Url);
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

    public Element getEmploymentRecord(String Url) {
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
            //System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            //System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            //System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            //java.net.URL endpoint = new URL("http://localhost:8080/WSP_HW2_FINAL/EmploymentService");
            java.net.URL endpoint = new URL(Url);
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

    public Element getCompaniesDB(String Url) {
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
            //System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            //System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            //System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            //java.net.URL endpoint = new URL("http://localhost:8080/WSP_HW2_FINAL/CompaniesDBService");
            java.net.URL endpoint = new URL(Url);
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

    private Element getJobsAdvertised(String Url) {
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
                    "getAdvertisedJobs", "ns2");
            SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
            QName name = new QName("keywords");
            SOAPElement symbol = bodyElement.addChildElement(name);
            symbol.addTextNode(keyword);

            SOAPConnectionFactory soapConnectionFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnectionFactory.createConnection();
            message.writeTo(System.out);
            //System.out.println("Request message1: " + message.getSOAPHeader().getTextContent());
            //System.out.println("Request message2: " + message.getSOAPBody().getTextContent());
            //System.out.println("Request message3: " + message.getSOAPPart().getEnvelope().getTextContent());
            java.net.URL endpoint = new URL(Url);
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

    private boolean matchAdvJobsWithProfile(String applicantFilePath, String advJobsFilePath) {
        String category = null;
        try {
            Document document = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // to ignore cosmetic whitespace between elements.
            factory.setIgnoringElementContentWhitespace(true);
            //Get a DocumentBuilder (parser) object
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(applicantFilePath));
            NodeList nodes = document.getElementsByTagName("ns1:industry");
            if (nodes.getLength() == 1) {
                category = nodes.item(0).getTextContent();
            }
            if (category != null) {
                document = builder.parse(new File(advJobsFilePath));
                NodeList nodeLst2 = document.getElementsByTagName("category");
                if (nodeLst2.getLength() > 0) {
                    String temp = nodeLst2.item(0).getTextContent();
                    if (category.startsWith(temp)) {
                        return true;
                    }
                }
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import service.clients.EmploymentServiceClient;
import utils.ApplicantProfileGenerator;

/**
 *
 * @author julio
 */
public class ProfileMessageHandler implements SOAPHandler<SOAPMessageContext> {
    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            try {

                String personalNumber = messageContext.getMessage().getSOAPBody().getTextContent();
                EmploymentServiceClient client = new EmploymentServiceClient(personalNumber);

                // Call the other services by sending a soap request message
                Element transcriptElem = client.getTranscript();
                Element companiesElem = client.getCompaniesDB();
                Element employementRecordsElem = client.getEmploymentRecord();

                Source transcriptSource = new DOMSource(transcriptElem);
                Source companiesSource = new DOMSource(companiesElem);
                Source employmentSource = new DOMSource(employementRecordsElem);
                // Write the DOM documents to files
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(transcriptSource, new StreamResult(new File("xml/transcript.xml")));
                transformer.transform(companiesSource, new StreamResult(new File("xml/companies.xml")));
                transformer.transform(employmentSource, new StreamResult(new File("xml/employment.xml")));

                System.out.println("Current path: " + (new File(".")).getCanonicalPath());

                ApplicantProfileGenerator generator = new ApplicantProfileGenerator();
                String profileFilePath = "xml/userProfile.xml";
                String xsltFilePath = "xml/applicantprofileTransform.xsl";
                String resumeFilePath = "xml/resume.xml";
                String companyInfoSchemaPath = "xml/companyInfoXmlSchema.xsd";
                generator.Generate("xml/companies.xml", "xml/employment.xml", "xml/resume.xml", "xml/transcript.xml", profileFilePath, xsltFilePath, resumeFilePath, companyInfoSchemaPath);

                // Attach the profile to the body
                SOAPBody body = messageContext.getMessage().getSOAPBody();
                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                dfactory.setNamespaceAware(true);
                dfactory.setValidating(true);
                DocumentBuilder builder = dfactory.newDocumentBuilder();
                Document document = builder.parse(profileFilePath);
                SOAPBodyElement docElement = body.addDocument(document);

            } catch (Exception ex) {
                Logger.getLogger(ProfileMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("\nInbound message:");
        }
        return true;
    }

    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    public void close(MessageContext context) {
    }
}

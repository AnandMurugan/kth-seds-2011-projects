/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.clients;

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

/**
 *
 * @author julio
 */
public class JobServiceClient {
    public static void main(String[] args) {
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
                    "getProfile", "ns2");
            SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
            QName name = new QName("personalNumber");
            SOAPElement symbol = bodyElement.addChildElement(name);
            symbol.addTextNode("881201-1234");
            
            SOAPConnectionFactory soapConnectionFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnectionFactory.createConnection();
            message.writeTo(System.out);
            java.net.URL endpoint = new URL("http://localhost:8080/WSP_HW2_JOB_SERV/JobCompanyService");
            SOAPMessage response = connection.call(message, endpoint);
            System.out.println("Response message: ");
            response.writeTo(System.out);
            System.out.println();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (SOAPException ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmploymentServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

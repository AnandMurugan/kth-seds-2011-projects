/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mypack;

import java.io.File;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author julio
 */
public class ApplicantProfileXmlModel {
    Document profileXmlDoc;
    String schemaPath = "";
    String schemaLanguage = "http://www.w3.org/2001/XMLSchema";
    String xmlFilePath = "";
    String personNumber;

    public ApplicantProfileXmlModel(String schemaPath, String xmlFilePath) {
        this.schemaPath = schemaPath;
        this.xmlFilePath = xmlFilePath;
        this.profileXmlDoc = DOMParser.getDOMDocument(this.schemaLanguage, this.schemaPath, this.xmlFilePath);
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:personalNumber");
        if (nodes.getLength() == 1) {
            this.personNumber = nodes.item(0).getTextContent();
        }

    }

    public String getPersonNumber() {
        return this.personNumber;
    }

    public void setCompanyInfo(String companyName, String description, String email, String website, String phone, String businessType) {
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) // if there are company info nodes
        {
            String currentCompanyName;
            for (int i = 0; i < nodes.getLength(); i++) {
                currentCompanyName = nodes.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {

                    Node companyProfileNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:company_profile");
                    if (companyProfileNode != null) {
                        // set description
                        Node descriptionNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "");
                        descriptionNode.setTextContent(description);
                        // set phone
                        Node phoneNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "");
                        phoneNode.setTextContent(phone);
                        // set email
                        Node emailNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "");
                        emailNode.setTextContent(email);
                        // set website
                        Node websiteNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "");
                        websiteNode.setTextContent(website);
                        // set business type
                        Node businessTypeNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "");
                        businessTypeNode.setTextContent(businessType);
                    }
                }
            }
        }
    }

    public void setDescription(String companyName, String description) {
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) // if there are company info nodes
        {
            String currentCompanyName;
            for (int i = 0; i < nodes.getLength(); i++) {
                currentCompanyName = nodes.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {

                    Node companyProfileNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:company_profile");
                    if (companyProfileNode != null) {
                        // set description
                        Node descriptionNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "ns1:description");
                        descriptionNode.setTextContent(description);
                    }
                }
            }
        }
    }

    public void setPhone(String companyName, String phone) {
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) // if there are company info nodes
        {
            String currentCompanyName;
            for (int i = 0; i < nodes.getLength(); i++) {
                currentCompanyName = nodes.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {

                    Node companyProfileNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:company_profile");
                    if (companyProfileNode != null) {
                        // set phone
                        Node phoneNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "ns1:phone");
                        phoneNode.setTextContent(phone);
                    }
                }
            }
        }
    }

    public void setWebsite(String companyName, String website) {
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) // if there are company info nodes
        {
            String currentCompanyName;
            for (int i = 0; i < nodes.getLength(); i++) {
                currentCompanyName = nodes.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {

                    Node companyProfileNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:company_profile");
                    if (companyProfileNode != null) {
                        // set website
                        Node websiteNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "ns1:website");
                        websiteNode.setTextContent(website);
                    }
                }
            }
        }
    }

    public void setEmail(String companyName, String email) {
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) // if there are company info nodes
        {
            String currentCompanyName;
            for (int i = 0; i < nodes.getLength(); i++) {
                currentCompanyName = nodes.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {

                    Node companyProfileNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:company_profile");
                    if (companyProfileNode != null) {
                        // set email
                        Node emailNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "ns1:email");
                        emailNode.setTextContent(email);
                    }
                }
            }
        }
    }

    public void setBusinessType(String companyName, String businessType) {
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) // if there are company info nodes
        {
            String currentCompanyName;
            for (int i = 0; i < nodes.getLength(); i++) {
                currentCompanyName = nodes.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {

                    Node companyProfileNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:company_profile");
                    if (companyProfileNode != null) {
                        // set business type
                        Node businessTypeNode = DOMParser.findNode(companyProfileNode.getChildNodes(), "ns1:businessType");
                        businessTypeNode.setTextContent(businessType);
                    }
                }
            }
        }
    }

    public void addPositionDetails(String companyName,
            String position,
            String startDate,
            String endDate,
            int hoursWeek,
            String location,
            String reasonExit) {
        NodeList positionNode = profileXmlDoc.getElementsByTagName("ns1:positions");

        if (positionNode.getLength() == 1) // if there are company info nodes
        {
            // this will list all positions
            NodeList positionsList = positionNode.item(0).getChildNodes();

            for (int i = 0; i < positionsList.getLength(); i++) {
                if (positionsList.item(i).getFirstChild().getTextContent().equals(position)) {
                    // update entrance date
                    Node entranceDateNode = DOMParser.findNode(positionsList.item(i).getChildNodes(), "ns2:entranceDate");
                    if (entranceDateNode != null) {
                        entranceDateNode.setTextContent(startDate);
                    }
                    // update exit date
                    Node exitDateNode = DOMParser.findNode(positionsList.item(i).getChildNodes(), "ns2:exitDate");
                    if (exitDateNode != null) {
                        exitDateNode.setTextContent(endDate);
                    }
                    // update hours week
                    Node hoursWeekNode = DOMParser.findNode(positionsList.item(i).getChildNodes(), "ns2:hoursPerWeek");
                    if (hoursWeekNode != null) {
                        hoursWeekNode.setTextContent(String.valueOf(hoursWeek));
                    }
                    // update location
                    Node locationNode = DOMParser.findNode(positionsList.item(i).getChildNodes(), "ns2:location");
                    if (locationNode != null) {
                        locationNode.setTextContent(location);
                    }
                    // update exit reason
                    Node exitReasonNode = DOMParser.findNode(positionsList.item(i).getChildNodes(), "ns2:exitReason");
                    if (exitReasonNode != null) {
                        exitReasonNode.setTextContent(reasonExit);
                    }
                    break;
                }
            }

            String currentCompanyName;
            for (int i = 0; i < positionNode.getLength(); i++) {
                currentCompanyName = positionNode.item(i).getFirstChild().getFirstChild().getTextContent();
                if (currentCompanyName.equals(companyName)) {
                }
            }
        }
    }

    public void setEmploymentRecord(String companyName, Node positions) {
    }

    public void saveXmlFile(String filePath) {
        try {
            Source source = new DOMSource(profileXmlDoc);

            // Prepare the output file
            File file = new File(filePath);
            Result result = new StreamResult(file);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void overriteXmlFile() {
        saveXmlFile(this.xmlFilePath);
    }
}
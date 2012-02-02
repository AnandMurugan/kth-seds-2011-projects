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

    public ApplicantProfileXmlModel(String schemaPath, String xmlFilePath) {
        this.schemaPath = schemaPath;
        this.xmlFilePath = xmlFilePath;
        profileXmlDoc = DOMParser.getDOMDocument(this.schemaLanguage, this.schemaPath, this.xmlFilePath);
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
                        Node descriptionNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"");
                        descriptionNode.setTextContent(description);
                        // set phone
                        Node phoneNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"");
                        phoneNode.setTextContent(phone);
                        // set email
                        Node emailNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"");
                        emailNode.setTextContent(email);
                        // set website
                        Node websiteNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"");
                        websiteNode.setTextContent(website);
                        // set business type
                        Node businessTypeNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"");
                        businessTypeNode.setTextContent(businessType);
                    }
                }
            }
        }
    }

    public void setDescription(String companyName, String description){
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
                        Node descriptionNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"ns1:description");
                        descriptionNode.setTextContent(description);
                    }
                }
            }
        }
    }
    
    public void setPhone(String companyName, String phone){
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
                        Node phoneNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"ns1:phone");
                        phoneNode.setTextContent(phone);
                    }
                }
            }
        }
    }
    
    public void setWebsite(String companyName, String website){
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
                        Node websiteNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"ns1:website");
                        websiteNode.setTextContent(website);
                    }
                }
            }
        }
    }
    
    public void setEmail(String companyName, String email){
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
                        Node emailNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"ns1:email");
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
                        Node businessTypeNode = DOMParser.findNode(companyProfileNode.getChildNodes(),"ns1:businessType");
                        businessTypeNode.setTextContent(businessType);
                    }
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

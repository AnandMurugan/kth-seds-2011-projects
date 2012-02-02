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

    public void setCompanyInfo(String companyName, String description, String email, String website) {
        // check if there is already a node created with the company info
        NodeList nodes = profileXmlDoc.getElementsByTagName("ns1:company");

        if (nodes.getLength() > 0) {
            System.out.println("Matched!!");
        } else {
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

                    Node businessTypeNode = DOMParser.findNode(nodes.item(i).getChildNodes(), "ns1:businessType");
                    if (businessTypeNode != null) {
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

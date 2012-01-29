/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author julio
 */
public class DOMParser {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // to make the parser a validating parse
            factory.setValidating(true);
            //To parse a XML document with a namespace,
            factory.setNamespaceAware(true);

            // to ignore cosmetic whitespace between elements.
            factory.setIgnoringElementContentWhitespace(true);
            factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            //specifies the XML schema document to be used for validation.
            factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    "Transcript.xsd");

            //Get a DocumentBuilder (parser) object
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(
		                              new File("xml/Transcript.xml"));
		      //Instantiate an object of this class
		      ///
		      DOMParser domParser = new DOMParser();
            
        } catch (SAXException ex) {
            Logger.getLogger(DOMParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DOMParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DOMParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

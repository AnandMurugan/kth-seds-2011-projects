package mypack;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser {
    String schemaPath;
    String schemaLanguage;

    public DOMParser() {
    }

    public DOMParser(String aSchemaPath, String aSchemaLanguage) {
        this.schemaLanguage = aSchemaLanguage;
        this.schemaPath = aSchemaPath;
    }

    public static Document getDOMDocument(String schemaLanguage, String schemaFilePath, String aXmlFilePath) {
        Document document = null;
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // to make the parser a validating parse
            factory.setValidating(true);
            //To parse a XML document with a namespace,
            factory.setNamespaceAware(true);

            // to ignore cosmetic whitespace between elements.
            factory.setIgnoringElementContentWhitespace(true);
            factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    schemaLanguage);
            //specifies the XML schema document to be used for validation.
            factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    schemaFilePath);

            //Get a DocumentBuilder (parser) object

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            //Parse the XML input file to create a
            // Document object that represents the
            // input XML file.
            ///
            document = builder.parse(
                    new File(aXmlFilePath));
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return document;
    }

    public static void main(String[] args) {
        try {
            //Get a factory object for DocumentBuilder objects
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();


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

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            //Parse the XML input file to create a
            // Document object that represents the
            // input XML file.
            ///
            Document document = builder.parse(
                    new File("xml/Transcript.xml"));

            //Instantiate an object of this class
            ///
            DOMParser domParser = new DOMParser();

            //Process the DOM tree, beginning with the
            // Document node to produce the output.
            // Invocation of processDocumentNode starts
            // a recursive process that processes the
            // entire DOM tree.
            ///                      
            // domParser.processNode(document);
            // OR 
            domParser.processTranscript(document.getFirstChild());

        } catch (Exception e) {

            e.printStackTrace(System.err);
        }//end catch
    }

    public static Node findNode(NodeList nodes, String nodeName) {
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName().equals(nodeName)) {
                return nodes.item(i);
            }
        }
        return null;
    }

    String getNodeValue(NodeList nodes, String nodeName) {
        Node result = findNode(nodes, nodeName);
        if (result != null) {
            return result.getFirstChild().getNodeValue();
        }
        return null;
    }

    void processNode(Node root) {

        try {
            if (root == null) {
                System.err.println("Node is null !!!");
                return;
            }

            int type = root.getNodeType();

            switch (type) {
                case Node.TEXT_NODE: {
                    System.out.print("Text Node: <" + root.getNodeValue() + ">\n");

                    break;
                }//end case Node.TEXT_NODE

                case Node.ATTRIBUTE_NODE: {
                    System.out.print("Attribute Node: <" + root.getNodeName() + " = " + root.getNodeValue() + " >\n");
                    break;
                }

                case Node.ELEMENT_NODE: {
                    NodeList children = root.getChildNodes();
                    System.out.print("Element Node: <" + root.getNodeName() + "> has " + children.getLength() + " child elements.\n");


                    for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
                        processNode(child);
                    }
                    break;
                }//end case ELEMENT_NODE

                case Node.DOCUMENT_NODE: {
                    NodeList children = root.getChildNodes();
                    System.out.print("Document Node: <" + root.getNodeName() + "> has " + children.getLength() + " child elements.\n");

                    for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
                        processNode(child);
                    }

                    break;
                }//end case DOCUMENT_NODE

                // there are even more cases to check
                default: {
                }//end default

            }//end switch

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }//end catch
    }

    void processTranscript(Node root) {
        try {
            if (root.getNodeName().equals("transcript")) {

                String student_name = getNodeValue(root.getChildNodes(), "name");
                String univ_name = getNodeValue(root.getChildNodes(), "university");
                String degree = getNodeValue(root.getChildNodes(), "degree");
                String year = getNodeValue(root.getChildNodes(), "year");
                NodeList courseList = root.getChildNodes();
                String[] courses = new String[courseList.getLength()];

                for (int i = 0; i < courseList.getLength(); i++) {
                    courses[i] = courseList.item(0).getNodeValue();
                }
                System.out.print(student_name + ", " + univ_name + " , " + degree + ", " + year + " ,");
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mypack;

//import javax.xml.parsers.SAXParser;
import java.io.File;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author julio
 */
public class SAXParser {
    static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) {

        try {
            // Create schema


            // Create Parser
            SAXParserFactory saxpf = SAXParserFactory.newInstance();
            saxpf.setNamespaceAware(true);
            saxpf.setValidating(true);
            javax.xml.parsers.SAXParser saxp = saxpf.newSAXParser();
            //saxp.setProperty("http://xml.org/sax/features/validation", true);
            // Ensure namespace processing is on (the default)
            //saxp.setProperty("http://xml.org/sax/features/namespaces", true);
            saxp.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");



            //specifies the XML schema document to be used for validation.
            saxp.setProperty(JAXP_SCHEMA_SOURCE,
                    new File("src/schemas/companyInfoXmlSchema.xsd"));

            //saxp.setProperty(null, saxpf);
            //saxp.parse("src/schemas/companyInfoXmlSchema.xml", new CompanyInfoParser());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static class CompanyInfoParser extends DefaultHandler {
        boolean isName;
        boolean isAddress;
        boolean isWebsite;
        boolean isDescription;
        boolean isBusinessType;
        boolean isEmail;
        boolean isPhone;
        boolean isYears;
        boolean isProduct;
        String currentCompany;
        ApplicantProfileXmlModel profileModel;

        public CompanyInfoParser(ApplicantProfileXmlModel profileModel) {
            this.profileModel = profileModel;
        }

        @Override
        public void startElement(String arg0, String localName, String qName, Attributes atts) throws SAXException {
            if (localName.equalsIgnoreCase("COMPANYINFO")) {
                int length = atts.getLength();
                if (length == 1) {
                    currentCompany = atts.getValue(0);
                }
            }

            if (localName.equalsIgnoreCase("NAME")) {
                isName = true;
            }

            if (localName.equalsIgnoreCase("ADDRESS")) {
                isAddress = true;
            }

            if (localName.equalsIgnoreCase("WEBSITE")) {
                isWebsite = true;
            }

            if (localName.equalsIgnoreCase("DESCRIPTION")) {
                isDescription = true;
            }

            if (localName.equalsIgnoreCase("BUSINESSTYPE")) {
                isBusinessType = true;
            }

            if (localName.equalsIgnoreCase("EMAIL")) {
                isEmail = true;
            }

            if (localName.equalsIgnoreCase("PHONE")) {
                isPhone = true;
            }

            if (localName.equalsIgnoreCase("YEARS")) {
                isYears = true;
            }

            if (localName.equalsIgnoreCase("PRODUCT")) {
                isProduct = true;
            }
        }

        @Override
        public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
            if (isName) {
                isName = false;
                currentCompany = new String(arg0, arg1, arg2);
            }

            if (isAddress) {
                isAddress = false;
            }

            if (isPhone) {
                isPhone = false;
                profileModel.setPhone(currentCompany, new String(arg0, arg1, arg2));
            }

            if (isBusinessType) {
                isBusinessType = false;
                profileModel.setBusinessType(currentCompany, new String(arg0, arg1, arg2));
            }

            if (isDescription) {
                isDescription = false;
                profileModel.setDescription(currentCompany, new String(arg0, arg1, arg2));
            }

            if (isEmail) {
                isEmail = false;
                profileModel.setEmail(currentCompany, new String(arg0, arg1, arg2));
            }

            if (isWebsite) {
                isWebsite = false;
                profileModel.setWebsite(currentCompany, new String(arg0, arg1, arg2));
            }
            
            if (isProduct) {
                isName = false;
            }

            if (isYears) {
                isYears = false;
            }

            //System.out.println("characters: " + new String(arg0, arg1, arg2) + " ");
        }

        @Override
        public void endElement(String arg0, String arg1, String arg2) throws SAXException {
            //System.out.println("endElement:<" + arg2 + ">");
        }

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void endDocument() throws SAXException {
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mypack;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import mypack.SAXParser.CompanyInfoParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author julio
 */
public class ApplicantProfileGenerator {
    String profileSchemaPath = "src/schemas/applicantProfileXmlSchema.xsd";
    String companyInfoSchemaPath = "src/schemas/companyInfoXmlSchema.xsd";
    String employmentRecordSchemaPath = "src/schemas/employmentRecordXmlSchema.xsd";
    String resumeSchemaPath = "src/schemas/resume.xsd";
    String transcriptSchemaPath = "src/schemas/transcriptSchema.xsd";
    static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    public ApplicantProfileGenerator() {
    }

    public ApplicantProfileGenerator(String applicantProfileSchemaPath,
            String companyInfoSchemaPath,
            String employmentRecordSchemaPath,
            String resumeSchemaPath,
            String transcriptSchemaPath) {
        this.profileSchemaPath = applicantProfileSchemaPath;
        this.companyInfoSchemaPath = companyInfoSchemaPath;
        this.employmentRecordSchemaPath = employmentRecordSchemaPath;
        this.resumeSchemaPath = resumeSchemaPath;
        this.transcriptSchemaPath = transcriptSchemaPath;
    }

    // TODO. define the return type of the generated application profile
    public void Generate(String companiesXmlPath, 
            String employmentRecordXmlPath, 
            String resumeXmlPath, 
            String transcriptXmlPath, 
            String profileXmlPath) {
        try {
            // Create Application Profile Model
            ApplicantProfileXmlModel profileModel = new ApplicantProfileXmlModel(this.profileSchemaPath, profileXmlPath);
                        
            //SAX Parsing
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
            saxp.setProperty(JAXP_SCHEMA_SOURCE, new File("src/schemas/companyInfoXmlSchema.xsd"));
            saxp.parse("src/xml/companiesDatabase.xml", new CompanyInfoParser(profileModel));
            profileModel.overriteXmlFile();
            
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }



    }

    public static void main(String[] args) {
        // TODO. get the input xml files from the command line params
        ApplicantProfileGenerator generator = new ApplicantProfileGenerator();
        generator.Generate("src/xml/companiesDatabase.xml", "", "", "", "src/xml/applicantProfile.xml");

    }
}

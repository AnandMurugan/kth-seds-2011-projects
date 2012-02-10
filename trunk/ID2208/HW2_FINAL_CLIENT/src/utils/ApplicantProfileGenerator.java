/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import employment.po.ExtendedPositionInfoType;
import employment.po.ObjectFactory;
import employment.po.Records;
import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import utils.SAXParser.CompanyInfoParser;

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
            //XSLT transformation
            XSLTTransformer obj = new XSLTTransformer();
            obj.transform();

            // Create Application Profile Model
            ApplicantProfileXmlModel profileModel = new ApplicantProfileXmlModel(this.profileSchemaPath, profileXmlPath);

            //SAX Parsing
            // Create Parser
            SAXParserFactory saxpf = SAXParserFactory.newInstance();
            saxpf.setNamespaceAware(true);
            saxpf.setValidating(true);
            javax.xml.parsers.SAXParser saxp = saxpf.newSAXParser();
            saxp.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            //specifies the XML schema document to be used for validation.
            saxp.setProperty(JAXP_SCHEMA_SOURCE, new File("src/schemas/companyInfoXmlSchema.xsd"));
            saxp.parse(companiesXmlPath, new CompanyInfoParser(profileModel));
            // JAXB parsing
            parseEmpRecordJAXB("employment.po", employmentRecordXmlPath, profileModel);
            profileModel.overriteXmlFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void parseEmpRecordJAXB(String context, String filePath, ApplicantProfileXmlModel profileModel) {
        try {
            JAXBContext jcontext = JAXBContext.newInstance(context);
            ObjectFactory objFactory = new ObjectFactory();

            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            //unmarshaller.setValidating(true);

            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    new Boolean(true));

            //JAXBElement recordsElem = (JAXBElement) unmarshaller.unmarshal(new File(filePath));
            Records records = (Records) unmarshaller.unmarshal(new File(filePath));
            List<Records.Record> employeeRecords = records.getRecord();

            for (int i = 0; i < employeeRecords.size(); i++) {
                if (employeeRecords.get(i).getPersonalNumber().equals(profileModel.getPersonNumber())) {
                    List<Records.Record.EmploymentRecord> empRecords = employeeRecords.get(i).getEmploymentRecord();
                    for (int j = 0; j < empRecords.size(); j++) {
                        String companyName = empRecords.get(i).getCompanyInfo().getName();
                        List<ExtendedPositionInfoType> positionList = empRecords.get(i).getPositions().getPosition();

                        for (int k = 0; k < positionList.size(); k++) {
                            String position = positionList.get(k).getPositionTitle();
                            String startDate = positionList.get(k).getEntranceDate().toString();
                            String endDate = positionList.get(k).getExitDate().toString();
                            int hoursWeek = (int) positionList.get(k).getHoursPerWeek().intValue();
                            String location = positionList.get(k).getLocation();
                            String reasonExit = positionList.get(k).getExitReason();
                            profileModel.addPositionDetails(companyName, position, startDate, endDate, hoursWeek, location, reasonExit);
                        }
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String applicantFilePath = "src/xml/userProfile.xml";

        if (args.length == 1) {
            applicantFilePath = args[0];
        }

        ApplicantProfileGenerator generator = new ApplicantProfileGenerator();
        generator.Generate("src/xml/companiesDatabase.xml", "src/xml/employmentRecordDB.xml", "src/xml/resume.xml", "src/xml/transcript.xml", applicantFilePath);

    }
}

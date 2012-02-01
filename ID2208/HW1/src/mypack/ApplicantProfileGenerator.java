/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mypack;

/**
 *
 * @author julio
 */
public class ApplicantProfileGenerator {
    String applicantProfileSchemaPath = "applicantProfileXmlSchema.xsd";
    String companyInfoSchemaPath = "companyInfoXmlSchema.xsd";
    String employmentRecordSchemaPath = "employmentRecordXmlSchema.xsd";
    String resumeSchemaPath = "resume.xsd";
    String transcriptSchemaPath = "transcriptSchema.xsd";
    
    public ApplicantProfileGenerator(){
    
    }
    
    public ApplicantProfileGenerator(String applicantProfileSchemaPath,
            String companyInfoSchemaPath,
            String employmentRecordSchemaPath,
            String resumeSchemaPath,
            String transcriptSchemaPath){
        this.applicantProfileSchemaPath = applicantProfileSchemaPath;
        this.companyInfoSchemaPath = companyInfoSchemaPath;
        this.employmentRecordSchemaPath = employmentRecordSchemaPath;
        this.resumeSchemaPath = resumeSchemaPath;
        this.transcriptSchemaPath = transcriptSchemaPath;
    }
    
    // TODO. define the return type of the generated application profile
    public void Generate(String companiesXmlPath, String employmentRecordsPath, String resumeXmlPath, String transcriptXmlPath){
        // Call each parser to process the correspondant file
    }
    
    public static void main(String[] args){
        // TODO. get the input xml files from the command line params
        ApplicantProfileGenerator generator = new ApplicantProfileGenerator();
        generator.Generate(null, null, null, null);
    
    }
}

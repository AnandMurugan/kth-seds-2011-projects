package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTTransformer {

    private String xsltFilePath = "src/xml/applicantprofileTransform.xsl";
    private String resumeFilePath = "src/xml/resume.xml";
    private String profileFilePath = "src/xml/userProfile.xml";
    
    public XSLTTransformer(){
        
    }
    
    public XSLTTransformer(String xsltFilePath, String resumeFilePath, String profileFilePath){
        this.xsltFilePath = xsltFilePath;
        this.resumeFilePath = resumeFilePath;
        this.profileFilePath = profileFilePath;
    }
    
    public void transform() {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Source xslSource = new StreamSource(xsltFilePath);
            //Source xslSource = new StreamSource( "src/xml/applicantprofileWithGPA.xsl" );
            Transformer transformer = tFactory.newTransformer(xslSource);
            transformer.transform(new StreamSource(resumeFilePath), new StreamResult(new FileOutputStream(profileFilePath)));
            //transformer.transform( new StreamSource( "src/xml/transcript.xml" ),new StreamResult( new FileOutputStream( "src/xml/userProfile.xml" )));
        } catch (TransformerFactoryConfigurationError | FileNotFoundException | TransformerException ex) {
        }
    }
}

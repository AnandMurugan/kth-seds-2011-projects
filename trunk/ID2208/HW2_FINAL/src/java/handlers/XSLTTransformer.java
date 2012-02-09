/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

/**
 *
 * @author Anand
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTTransformer {

    public void transform(String arg0) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Source xslSource = new StreamSource("src/xml/SearchJobs.xsl");
            //Source xslSource = new StreamSource( "src/xml/applicantprofileWithGPA.xsl" );
            Transformer transformer = tFactory.newTransformer(xslSource);
            transformer.setParameter("criteria", arg0);
            transformer.transform(new StreamSource("src/xml/jobs.xml"), new StreamResult(new FileOutputStream("src/xml/jobsOut.xml")));
            //transformer.transform( new StreamSource( "src/xml/transcript.xml" ),new StreamResult( new FileOutputStream( "src/xml/userProfile.xml" )));
        } catch (TransformerFactoryConfigurationError | FileNotFoundException | TransformerException ex) {
        }
    }
}

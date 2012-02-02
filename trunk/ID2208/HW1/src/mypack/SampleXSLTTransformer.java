package mypack;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class SampleXSLTTransformer {

	public static void main(String[] args) {
		try {
			TransformerFactory  tFactory =  TransformerFactory.newInstance();
			Source xslSource = new StreamSource( "src/xml/applicantprofileTransform.xsl" );
                        //Source xslSource = new StreamSource( "src/xml/applicantprofileWithGPA.xsl" );
			Transformer transformer = tFactory.newTransformer( xslSource );
			transformer.transform( new StreamSource( "src/xml/resume.xml" ),new StreamResult( new FileOutputStream( "src/xml/test_output.xml" )));
                        //transformer.transform( new StreamSource( "src/xml/transcript.xml" ),new StreamResult( new FileOutputStream( "src/xml/test_output.xml" )));
		}catch(TransformerFactoryConfigurationError | FileNotFoundException | TransformerException ex) {}
	}
}

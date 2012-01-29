package mypack;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class SampleXSLTTransformer {

	public static void main(String[] args) {
		try {
			TransformerFactory  tFactory =  TransformerFactory.newInstance();
			Source xslSource = new StreamSource( "xml/Coffee_NoLoop.xsl" );
			Transformer transformer = tFactory.newTransformer( xslSource );
			transformer.transform( new StreamSource( "xml/Coffee.xml" ),new StreamResult( new FileOutputStream( "xml/test_output.xml" )));
		}catch(Exception ex) {
			 ex.printStackTrace();
		}
	}
}

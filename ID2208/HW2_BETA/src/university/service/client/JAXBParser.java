/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package university.service.client;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Anand
 */
public class JAXBParser {

    //private static final String filePath = "D:/NetBeansProjects/PWS/WSP_HW2_Services/src/java/xml/transcript.xml";
    private static final String filePath = "src/xml/transcript.xml";
    public static void main(String[] args) {
        try {
            JAXBContext jcontext = JAXBContext.newInstance("transcript");
            ObjectFactory objFactory = new ObjectFactory();
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            Transcript record = (Transcript) unmarshaller.unmarshal(new File(filePath));
            System.out.println(record.toString());
        } catch (JAXBException ex) {
            Logger.getLogger(UniversityWS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

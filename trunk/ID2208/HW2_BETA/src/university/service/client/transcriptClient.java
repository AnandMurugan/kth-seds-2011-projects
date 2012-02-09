/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package university.service.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import university.service.client.RetrieveTranscriptResponse.Return;

/**
 *
 * @author Anand
 */
public class transcriptClient {

    final QName qName = new QName(
            "http://university.service/university", "UniversityWS");

    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Specify the URL of the OrderProcess Web Service");
//            System.exit(-1);
//        }
        URL url = getWSDLURL("http://localhost:8080/WSP_HW2_WebServices/university");
        transcriptClient client = new transcriptClient();
        client.processTranscript(url);
    }

    private static URL getWSDLURL(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return url;
    }

    public void processTranscript(URL url) {

        UniversityWS_Service universityService = new UniversityWS_Service(url, qName);

        System.out.println("Service is" + universityService);

        String personalNumber = "881201-1234";

        UniversityWS port = universityService.getUniversityWebServicePort();
        Return transcriptResponse = port.retrieveTranscript(personalNumber);

        System.out.println("transcriptResponse name is " + transcriptResponse.getTranscriptRecord().toString());

    }
}

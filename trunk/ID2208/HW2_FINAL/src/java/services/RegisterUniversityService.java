/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved. SUN
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.*;
import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

public class RegisterUniversityService {

    String httpProxyHost = "";
    String httpProxyPort = "";
    String httpsProxyHost = "";
    String httpsProxyPort = "";
    String regUrli = "";
    String regUrlp = "";
    String username = "testuser";
    String password = "testuser";
    Properties connProps = new Properties();
    private static final String QUERY_URL = "query.url";
    private static final String PUBLISH_URL = "publish.url";
    private static final String USER_NAME = "user.name";
    private static final String USER_PASSWORD = "user.password";
    private static final String PROXY_HOST = "http.proxy.host";
    private static final String PROXY_PORT = "http.proxy.port";

    public static void main(String[] args) {

        try {
            RegisterUniversityService bqt = new RegisterUniversityService();

            //Get publish.properties
            Properties properties = new Properties();
            properties.load(new FileInputStream("D:/NetBeansProjects/PWS/WSP_HW2_FINAL/src/java/publish.properties"));

            bqt.publishService(properties);
        } catch (JAXRException e) {
            System.out.println("FAILED" + e.getMessage());
        } catch (IOException ioe) {
            System.out.println("Can not open properties file");
        }
    }

    public void publishService(Properties properties)
            throws JAXRException {

        try {
            assignUserProperties(properties);
            setConnectionProperties();

            ConnectionFactory factory =
                    ConnectionFactory.newInstance();
            factory.setProperties(connProps);
            Connection conn = factory.createConnection();

            RegistryService rs = conn.getRegistryService();
            BusinessQueryManager bqm = rs.getBusinessQueryManager();
            BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();

            PasswordAuthentication passwdAuth = new PasswordAuthentication(username, password.toCharArray());

            Set creds = new HashSet();
            creds.add(passwdAuth);
            conn.setCredentials(creds);

            Collection orgs = new ArrayList();

            Organization org = blm.createOrganization(blm.createInternationalString("University"));
            org.setDescription(blm.createInternationalString("University Web Service for transcripts"));

            Service service = blm.createService(blm.createInternationalString("University Service"));
            service.setDescription(blm.createInternationalString("Services of KTH University"));

            User user = blm.createUser();
            PersonName personName = blm.createPersonName("President, KTH");

            org.setPrimaryContact(user);

            TelephoneNumber telephoneNumber = blm.createTelephoneNumber();
            telephoneNumber.setNumber("781-333-3333");
            telephoneNumber.setType(null);

            PostalAddress address = blm.createPostalAddress("16453", "Kista", "Stockholm", "DC", "Sweden", "02140", "");
            Collection postalAddresses = new ArrayList();
            postalAddresses.add(address);

            Collection emailAddresses = new ArrayList();
            EmailAddress emailAddress = blm.createEmailAddress("mail@kth.se");
            emailAddresses.add(emailAddress);

            Collection numbers = new ArrayList();
            numbers.add(telephoneNumber);

            user.setPersonName(personName);
            user.setPostalAddresses(postalAddresses);
            user.setEmailAddresses(emailAddresses);
            user.setTelephoneNumbers(numbers);

            //Concepts for NAICS and computer
            ClassificationScheme cScheme = blm.createClassificationScheme(
                    blm.createInternationalString("ntis-gov:naics"),
                    blm.createInternationalString(""));
            javax.xml.registry.infomodel.Key cKey = (javax.xml.registry.infomodel.Key) blm.createKey("uuid:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
            cScheme.setKey(cKey);

            Classification classification = (Classification) blm.createClassification(cScheme,
                    "Computer Systems Design and Related Services", "5415");

            org.addClassification(classification);

            ClassificationScheme cScheme1 = blm.createClassificationScheme(
                    blm.createInternationalString("D-U-N-S"),
                    blm.createInternationalString(""));
            javax.xml.registry.infomodel.Key cKey1 =
                    (javax.xml.registry.infomodel.Key) blm.createKey("uuid:8609C81E-EE1F-4D5A-B202-3EB13AD01823");
            cScheme1.setKey(cKey1);

            ExternalIdentifier ei =
                    blm.createExternalIdentifier(cScheme1, "D-U-N-S number",
                    "08-146-6849");

            org.addExternalIdentifier(ei);
            org.addService(service);

            orgs.add(org);

            BulkResponse br = blm.saveOrganizations(orgs);
            if (br.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                System.out.println("Organization Saved");
            } else {
                System.err.println("One or more JAXRExceptions "
                        + "occurred during the save operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }

        } catch (JAXRException e) {
        }
    }

    private void assignUserProperties(Properties props) {

        String proxyHost = ((String) props.get(PROXY_HOST)).trim();
        String proxyPort = ((String) props.get(PROXY_PORT)).trim();
        String queryURL = ((String) props.get(QUERY_URL)).trim();
        String publishURL = ((String) props.get(PUBLISH_URL)).trim();
        String user = ((String) props.get(USER_NAME)).trim();
        String pw = ((String) props.get(USER_PASSWORD)).trim();

        if (proxyHost != null) {
            httpProxyHost = proxyHost;
            httpsProxyHost = proxyHost;
        }
        if (proxyPort != null) {
            httpProxyPort = proxyPort;
            httpsProxyPort = proxyPort;
        }

        if (queryURL != null) {
            regUrli = queryURL;
        }

        if (publishURL != null) {
            regUrlp = publishURL;
        }

        if (user != null) {
            username = user;
        }

        if (pw != null) {
            password = pw;
        }

    }

    private void setConnectionProperties() {
        connProps.setProperty("javax.xml.registry.queryManagerURL",
                regUrli);
        connProps.setProperty("javax.xml.registry.lifeCycleManagerURL",
                regUrlp);
        connProps.setProperty("javax.xml.registry.factoryClass",
                "com.sun.xml.registry.uddi.ConnectionFactoryImpl");
        connProps.setProperty("com.sun.xml.registry.http.proxyHost", httpProxyHost);
        connProps.setProperty("com.sun.xml.registry.http.proxyPort", httpProxyPort);
        connProps.setProperty("com.sun.xml.registry.https.proxyHost", httpsProxyHost);
        connProps.setProperty("com.sun.xml.registry.https.proxyPort", httpsProxyPort);
    }
}

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
            //create business entity
            Organization org = blm.createOrganization(blm.createInternationalString("University"));
            org.setDescription(blm.createInternationalString("University Web Service for transcripts"));
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
            ClassificationScheme cScheme = blm.createClassificationScheme(blm.createInternationalString("ntis-gov:naics"), blm.createInternationalString(""));
            javax.xml.registry.infomodel.Key cKey = (javax.xml.registry.infomodel.Key) blm.createKey("uuid:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
            cScheme.setKey(cKey);

            Classification classification = (Classification) blm.createClassification(cScheme, "University Services", "514211");

            org.addClassification(classification);

//            ClassificationScheme cScheme1 = blm.createClassificationScheme(blm.createInternationalString("D-U-N-S"), blm.createInternationalString(""));
//            javax.xml.registry.infomodel.Key cKey1 = (javax.xml.registry.infomodel.Key) blm.createKey("uuid:8609C81E-EE1F-4D5A-B202-3EB13AD01823");
//            cScheme1.setKey(cKey1);
//            ExternalIdentifier ei = blm.createExternalIdentifier(cScheme1, "D-U-N-S number", "08-146-6849");
//            org.addExternalIdentifier(ei);
            Service service = blm.createService(blm.createInternationalString("University Service"));
            service.setDescription(blm.createInternationalString("Services of KTH University"));

            Collection<ServiceBinding> serviceBindings = new ArrayList<ServiceBinding>();
            ServiceBinding binding = blm.createServiceBinding();
            InternationalString istr = blm.createInternationalString("Transcript Service Binding " + "Description");
            binding.setDescription(istr);
            binding.setValidateURI(false);
            binding.setAccessURI("http://localhost:8080/WSP_HW2_FINAL/UniversityService");
            serviceBindings.add(binding);
            // Add service bindings to service
            service.addServiceBindings(serviceBindings);
            // Add service to services, then add
            org.addService(service);
            orgs.add(org);
            //Concept for classification
            Concept specConcept = blm.createConcept(null, "TranscriptConcept", "");
            istr = blm.createInternationalString("Concept description for Transcript Service {}");
            specConcept.setDescription(istr);
            ExternalLink wsdlLink = blm.createExternalLink("http://localhost:8080/WSP_HW2_FINAL/UniversityService?WSDL", "Transcript service WSDL document");
            specConcept.addExternalLink(wsdlLink);
            String uuid_types = "uuid:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2";
            ClassificationScheme uddiOrgTypes = (ClassificationScheme) bqm.getRegistryObject(uuid_types, LifeCycleManager.CLASSIFICATION_SCHEME);
            Classification wsdlSpecClassification = blm.createClassification(uddiOrgTypes, "wsdlSpec", "wsdlSpec");
            // Define classifications
            Collection<Concept> concepts = new ArrayList<Concept>();
            concepts.add(specConcept);
            // Save Concept
            BulkResponse concResponse = blm.saveConcepts(concepts);
            String conceptKeyId = null;
            Collection concExceptions = concResponse.getExceptions();

            Key concKey = null;
            if (concExceptions == null) {
                System.out.println("WSDL Specification Concept saved");
                Collection<Key> keys = concResponse.getCollection();
                Iterator<Key> keyIter = keys.iterator();
                if (keyIter.hasNext()) {
                    concKey = keyIter.next();
                    conceptKeyId = concKey.getId();
                    System.out.println("Concept key is " + conceptKeyId);
                }
            }// Retrieve the concept from Registry
            Concept retSpecConcept = (Concept) bqm.getRegistryObject(
                    conceptKeyId, LifeCycleManager.CONCEPT);

            // Associate concept to Binding object
            SpecificationLink retSpeclLink = blm.createSpecificationLink();
            retSpeclLink.setSpecificationObject(retSpecConcept);
            binding.addSpecificationLink(retSpeclLink);

            //save organization
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
        connProps.setProperty("javax.xml.registry.queryManagerURL", regUrli);
        connProps.setProperty("javax.xml.registry.lifeCycleManagerURL", regUrlp);
        connProps.setProperty("javax.xml.registry.factoryClass", "com.sun.xml.registry.uddi.ConnectionFactoryImpl");
        connProps.setProperty("com.sun.xml.registry.http.proxyHost", httpProxyHost);
        connProps.setProperty("com.sun.xml.registry.http.proxyPort", httpProxyPort);
        connProps.setProperty("com.sun.xml.registry.https.proxyHost", httpsProxyHost);
        connProps.setProperty("com.sun.xml.registry.https.proxyPort", httpsProxyPort);
    }
}

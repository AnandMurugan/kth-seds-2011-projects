/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package publish.services;

import java.net.PasswordAuthentication;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

/**
 *
 * @author Anand
 */
public class RegisterService {

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
    private Properties properties;
    private BusinessLifeCycleManager blm;
    private BusinessQueryManager bqm;
    private RegistryService rs;
    private Connection conn;

    public RegisterService(Properties properties) {
        this.properties = properties;
        try {
            makeConnection();
        } catch (JAXRException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("JAXR exception" + ex.getMessage());
        }
    }

    private void assignUserProperties() {

        String proxyHost = ((String) properties.get(PROXY_HOST)).trim();
        String proxyPort = ((String) properties.get(PROXY_PORT)).trim();
        String queryURL = ((String) properties.get(QUERY_URL)).trim();
        String publishURL = ((String) properties.get(PUBLISH_URL)).trim();
        String user = ((String) properties.get(USER_NAME)).trim();
        String pw = ((String) properties.get(USER_PASSWORD)).trim();
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

    public void makeConnection() throws JAXRException {
        try {
            assignUserProperties();
            setConnectionProperties();

            ConnectionFactory factory = ConnectionFactory.newInstance();
            factory.setProperties(connProps);
            conn = factory.createConnection();

            rs = conn.getRegistryService();
            bqm = rs.getBusinessQueryManager();
            blm = rs.getBusinessLifeCycleManager();

            PasswordAuthentication passwdAuth = new PasswordAuthentication(username, password.toCharArray());

            Set creds = new HashSet();
            creds.add(passwdAuth);
            conn.setCredentials(creds);
        } catch (JAXRException e) {
            System.out.println("exception -" + e.getMessage());
        }
    }

    public void publishUniversityService() {
        try {

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
                System.err.println("One or more JAXRExceptions occurred during the save operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }

        } catch (JAXRException e) {
            System.out.println("exception -" + e.getMessage());
        }
    }

    public void publishCompanyDBService() {
        try {

            Collection orgs = new ArrayList();
            //create business entity
            Organization org = blm.createOrganization(blm.createInternationalString("CompanyDatabase"));
            org.setDescription(blm.createInternationalString("Company record web service"));
            User user = blm.createUser();
            PersonName personName = blm.createPersonName("John");

            org.setPrimaryContact(user);

            TelephoneNumber telephoneNumber = blm.createTelephoneNumber();
            telephoneNumber.setNumber("203-333-3333");
            telephoneNumber.setType(null);

            PostalAddress address = blm.createPostalAddress("SE-136 40", "HANINGE  ", "Stockholm", "DC", "Sweden", "02240", "");
            Collection postalAddresses = new ArrayList();
            postalAddresses.add(address);

            Collection emailAddresses = new ArrayList();
            EmailAddress emailAddress = blm.createEmailAddress("csc@csc.se");
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

            Classification classification = (Classification) blm.createClassification(cScheme, "Company database services", "516221");

            org.addClassification(classification);

            Service service = blm.createService(blm.createInternationalString("Company Database"));
            service.setDescription(blm.createInternationalString("Services of CSC Company database service"));

            Collection<ServiceBinding> serviceBindings = new ArrayList<ServiceBinding>();
            ServiceBinding binding = blm.createServiceBinding();
            InternationalString istr = blm.createInternationalString("Company database Service Binding " + "Description");
            binding.setDescription(istr);
            binding.setValidateURI(false);
            binding.setAccessURI("http://localhost:8080/WSP_HW2_FINAL/CompaniesDBService");
            serviceBindings.add(binding);
            // Add service bindings to service
            service.addServiceBindings(serviceBindings);
            // Add service to services, then add
            org.addService(service);
            orgs.add(org);
            //Concept for classification
            Concept specConcept = blm.createConcept(null, "CompanyDBConcept", "");
            istr = blm.createInternationalString("Concept description for Company database Service {}");
            specConcept.setDescription(istr);
            ExternalLink wsdlLink = blm.createExternalLink("http://localhost:8080/WSP_HW2_FINAL/CompaniesDBService?WSDL", "Company database service WSDL document");
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
                System.out.println("Company DB Organization Saved");
            } else {
                System.err.println("One or more JAXRExceptions occurred during the save operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }

        } catch (JAXRException e) {
            System.out.println("exception -" + e.getMessage());
        }
    }

    public void publishEmploymentService() {
        try {

            Collection orgs = new ArrayList();
            //create business entity
            Organization org = blm.createOrganization(blm.createInternationalString("Employment"));
            org.setDescription(blm.createInternationalString("Employment record web service"));
            User user = blm.createUser();
            PersonName personName = blm.createPersonName("Mark");

            org.setPrimaryContact(user);

            TelephoneNumber telephoneNumber = blm.createTelephoneNumber();
            telephoneNumber.setNumber("803-333-3333");
            telephoneNumber.setType(null);

            PostalAddress address = blm.createPostalAddress("SE-141 52", "HUDDINGE ", "Stockholm", "DC", "Sweden", "02140", "");
            Collection postalAddresses = new ArrayList();
            postalAddresses.add(address);

            Collection emailAddresses = new ArrayList();
            EmailAddress emailAddress = blm.createEmailAddress("abc@abc.se");
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

            Classification classification = (Classification) blm.createClassification(cScheme, "Employment record services", "515221");

            org.addClassification(classification);

            Service service = blm.createService(blm.createInternationalString("Employment record"));
            service.setDescription(blm.createInternationalString("Services of ABC Employment record service"));

            Collection<ServiceBinding> serviceBindings = new ArrayList<ServiceBinding>();
            ServiceBinding binding = blm.createServiceBinding();
            InternationalString istr = blm.createInternationalString("Employment Service Binding " + "Description");
            binding.setDescription(istr);
            binding.setValidateURI(false);
            binding.setAccessURI("http://localhost:8080/WSP_HW2_FINAL/EmploymentService");
            serviceBindings.add(binding);
            // Add service bindings to service
            service.addServiceBindings(serviceBindings);
            // Add service to services, then add
            org.addService(service);
            orgs.add(org);
            //Concept for classification
            Concept specConcept = blm.createConcept(null, "EmploymentConcept", "");
            istr = blm.createInternationalString("Concept description for Employment Service {}");
            specConcept.setDescription(istr);
            ExternalLink wsdlLink = blm.createExternalLink("http://localhost:8080/WSP_HW2_FINAL/EmploymentService?WSDL", "Employment service WSDL document");
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
                System.out.println("Employment record Organization Saved");
            } else {
                System.err.println("One or more JAXRExceptions occurred during the save operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }

        } catch (JAXRException e) {
            System.out.println("exception -" + e.getMessage());
        }
    }

    public void publishRecruitmentService() {
        try {

            Collection orgs = new ArrayList();
            //create business entity
            Organization org = blm.createOrganization(blm.createInternationalString("Recruitment"));
            org.setDescription(blm.createInternationalString("Recruitment web service"));
            User user = blm.createUser();
            PersonName personName = blm.createPersonName("Larry Page");

            org.setPrimaryContact(user);

            TelephoneNumber telephoneNumber = blm.createTelephoneNumber();
            telephoneNumber.setNumber("803-233-3232");
            telephoneNumber.setType(null);

            PostalAddress address = blm.createPostalAddress("23541-1919", "Blythewood", "Columbia", "SC", "USA", "02140", "");
            Collection postalAddresses = new ArrayList();
            postalAddresses.add(address);

            Collection emailAddresses = new ArrayList();
            EmailAddress emailAddress = blm.createEmailAddress("info@accenture.se");
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

            Classification classification = (Classification) blm.createClassification(cScheme, "Recruitment services", "515221");

            org.addClassification(classification);

            Service service = blm.createService(blm.createInternationalString("Recruitment Coimpany"));
            service.setDescription(blm.createInternationalString("Services of Accenture Rcruitment service"));

            Collection<ServiceBinding> serviceBindings = new ArrayList<ServiceBinding>();
            ServiceBinding binding = blm.createServiceBinding();
            InternationalString istr = blm.createInternationalString("Recruitment Service Binding " + "Description");
            binding.setDescription(istr);
            binding.setValidateURI(false);
            binding.setAccessURI("http://localhost:8080/WSP_HW2_FINAL/RecruitingCompanyService");
            serviceBindings.add(binding);
            // Add service bindings to service
            service.addServiceBindings(serviceBindings);
            // Add service to services, then add
            org.addService(service);
            orgs.add(org);
            //Concept for classification
            Concept specConcept = blm.createConcept(null, "RecruitmentConcept", "");
            istr = blm.createInternationalString("Concept description for Recruitment Service {}");
            specConcept.setDescription(istr);
            ExternalLink wsdlLink = blm.createExternalLink("http://localhost:8080/WSP_HW2_FINAL/RecruitingCompanyService?WSDL", "Recruitment service WSDL document");
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
                System.out.println("Recruitment Organization Saved");
            } else {
                System.err.println("One or more JAXRExceptions occurred during the save operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }

        } catch (JAXRException e) {
            System.out.println("exception -" + e.getMessage());
        }
    }
}

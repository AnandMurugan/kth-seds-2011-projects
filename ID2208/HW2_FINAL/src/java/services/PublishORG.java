/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.*;
import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

/**
 * The PublishORG class consists of a main method, a
 * makeConnection method, and an executePublish method.  
 * It creates an organization and publishes it to a registry.
 * 
 * To run this program, use the command 
 * 
 *     ant run-publish-org
 */

public class PublishORG {
  Connection connection = null;

  public PublishORG() {
  }

  public static void main(String[] args) {
    String publishURL = 
      "http://localhost:8080/RegistryServer/";
    String queryURL = 
      "http://localhost:8080/RegistryServer/";
    // Provide your user name and password    
    String username = "testuser";
    String password = "testuser";

    PublishORG po = new PublishORG();
    po.makeConnection(publishURL, queryURL);
    po.executePublish(username, password);
  }
    
  /**
   * Establishes a connection to a registry.
   * @param publishUrl   the URL of the publish registry
   * @param queryUrl   the URL of the query registry
   */
  public void makeConnection(String publishUrl, 
    String queryUrl) {
    /*
     * Define connection configuration properties. 
     * To publish, you need both the query URL and the 
     * publish URL.
     */
     Properties props = new Properties();
     props.setProperty(
       "javax.xml.registry.lifeCycleManagerURL", publishUrl);
     props.setProperty(
       "javax.xml.registry.queryManagerURL",queryUrl);
     props.setProperty("javax.xml.registry.factoryClass", 
       "com.sun.xml.registry.uddi.ConnectionFactoryImpl");

     try {
       // Create the connection, passing it the 
       // configuration properties
       ConnectionFactory factory = ConnectionFactory.newInstance();
       factory.setProperties(props);
       connection = factory.createConnection();
       System.out.println("Created connection to registry");
     } catch (Exception e) {
        e.printStackTrace();
        if (connection != null) {
          try {
            connection.close();
          } catch (JAXRException je) {}
        }
     }
  }
    
  /**
   * Creates an organization, its classification, and its
   * services, and saves it to the registry.
   */
  public void executePublish(String username, String password) {
    RegistryService rs;
    BusinessLifeCycleManager blcm;
    BusinessQueryManager bqm;

    try {
      rs = connection.getRegistryService();
      blcm = rs.getBusinessLifeCycleManager();
      bqm = rs.getBusinessQueryManager();
      System.out.println("Got registry service, query manager, and life cycle manager");

      // Get authorization from the registry
      PasswordAuthentication passwdAuth = new 
        PasswordAuthentication(username, password.toCharArray());
      Set credits = new HashSet();
      credits.add(passwdAuth);
      connection.setCredentials(credits);
      System.out.println("Established security credentials");

      // Create organization name and description
      // Replace this information with your organization information
      Organization org = blcm.createOrganization("javacourses.com");
      InternationalString s = 
	  blcm.createInternationalString("Java training and consulting services");
      org.setDescription(s);

      // Create primary contact, set name
      User primaryContact = blcm.createUser();
      PersonName pName = blcm.createPersonName("Qusay H. Mahmoud");
      primaryContact.setPersonName(pName);

      // Set primary contact phone number
      TelephoneNumber phoneNum = blcm.createTelephoneNumber();
      phoneNum.setNumber("(604) 285-2000");
      Collection phoneNums = new ArrayList();
      phoneNums.add(phoneNum);
      primaryContact.setTelephoneNumbers(phoneNums);

      // Set primary contact email address
      EmailAddress emailAddress = 
      blcm.createEmailAddress("qmahmoud@javacourses.com");
      Collection emailAddresses = new ArrayList();
      emailAddresses.add(emailAddress);
      primaryContact.setEmailAddresses(emailAddresses);

      // Set primary contact for organization
      org.setPrimaryContact(primaryContact);

      // Set classification scheme to NAICS
      ClassificationScheme cScheme = bqm.findClassificationSchemeByName(null, "ntis-gov:naics");

      // Create and add classification
      Classification classification = (Classification)
      blcm.createClassification(cScheme, "Computer Training", "61142");	
      Collection classifications = new ArrayList();
      classifications.add(classification);
      org.addClassifications(classifications);

      // Create services and service
      Collection services = new ArrayList();
      Service service = 
        blcm.createService("Buy a Java Course");
      InternationalString is = 
      blcm.createInternationalString("This service allows you to register for a Java course and download its manuals.");
      service.setDescription(is);

      // Create service bindings
      Collection serviceBindings = new ArrayList();
      ServiceBinding binding = blcm.createServiceBinding();
      is = blcm.createInternationalString("Service Binding " 
        + "Access this services using the given URL.");
      binding.setDescription(is);
      binding.setAccessURI("http://javacourses.com/register");
      serviceBindings.add(binding);

      // Add service bindings to service
      service.addServiceBindings(serviceBindings);

      // Add service to services, then add services to organization
      services.add(service);
      org.addServices(services);

      // Add organization and submit to registry
      // Retrieve key if successful
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse response = blcm.saveOrganizations(orgs);
      Collection exceptions = response.getExceptions();
      if (exceptions == null) {
        System.out.println("Organization saved");
        Collection keys = response.getCollection();
        Iterator keyIter = keys.iterator();
        if (keyIter.hasNext()) {
          javax.xml.registry.infomodel.Key orgKey = 
            (javax.xml.registry.infomodel.Key) keyIter.next();
          String id = orgKey.getId();
          System.out.println("Organization key is " + id);
          org.setKey(orgKey);
        }
      } else {
         Iterator excIter = exceptions.iterator();
         Exception exception = null;
         while (excIter.hasNext()) {
           exception = (Exception) excIter.next();
           System.err.println("Exception on save: " + 
             exception.toString());
         }
      }
    } catch (Exception e) {
       e.printStackTrace();
       if (connection != null) {
         try {
           connection.close();
         } catch (JAXRException je) {
            System.err.println("Connection close failed");
         }
       }
    }
  }
}
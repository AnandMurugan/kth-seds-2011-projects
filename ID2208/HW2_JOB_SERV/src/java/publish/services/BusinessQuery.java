/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package publish.services;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessQuery {
    // edit these if behind firewall, otherwise leave blank

    String httpProxyHost = "";
    String httpProxyPort = "";
    String regUrli = "";
    String regUrlp = "";
    Properties connProps = new Properties();
    private static final String QUERY_URL = "query.url";
    private static final String PUBLISH_URL = "publish.url";
    private static final String PROXY_HOST = "http.proxy.host";
    private static final String PROXY_PORT = "http.proxy.port";
    private static final String propertiesFilePath = "D:/NetBeansProjects/PWS/WSP_HW2_FINAL/src/java/publish.properties";
    private Properties properties;
    
    private void loadProperties(String filePath) {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(filePath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusinessQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            System.err.println("Can not open properties file");
        }
    }

    private void assignUserProperties(Properties props) {
        String temp;

        temp = ((String) props.get(QUERY_URL)).trim();
        if (temp != null) {
            regUrli = temp;
        }

        temp = ((String) props.get(PUBLISH_URL)).trim();
        if (temp != null) {
            regUrlp = temp;
        }

        temp = ((String) props.get(PROXY_HOST)).trim();
        if (temp != null) {
            httpProxyHost = temp;
        }

        temp = ((String) props.get(PROXY_PORT)).trim();
        if (temp != null) {
            httpProxyPort = temp;
        }
    }

    private void setConnectionProperties() {
        connProps.setProperty("javax.xml.registry.queryManagerURL", regUrli);
        connProps.setProperty("javax.xml.registry.lifeCycleManagerURL", regUrlp);
        connProps.setProperty("javax.xml.registry.factoryClass",
                "com.sun.xml.registry.uddi.ConnectionFactoryImpl");
        connProps.setProperty("com.sun.xml.registry.http.proxyHost", httpProxyHost);
        connProps.setProperty("com.sun.xml.registry.http.proxyPort", httpProxyPort);
    }

    private String getName(RegistryObject ro) throws JAXRException {
        try {
            return ro.getName().getValue();
        } catch (NullPointerException npe) {
            return "";
        }
    }

    private String getDescription(RegistryObject ro) throws JAXRException {
        try {
            return ro.getDescription().getValue();
        } catch (NullPointerException npe) {
            return "";
        }
    }

    public String executeQuery(String cname) throws JAXRException {
        Collection orgs = null;
        String wsdl = null;
        try {
            loadProperties(propertiesFilePath);
            assignUserProperties(properties);
            setConnectionProperties();

            ConnectionFactory factory = ConnectionFactory.newInstance();
            factory.setProperties(connProps);
            Connection conn = factory.createConnection();
            RegistryService rs = conn.getRegistryService();
            BusinessQueryManager bqm = rs.getBusinessQueryManager();


            ArrayList names = new ArrayList();
            names.add(cname);

            Collection fQualifiers = new ArrayList();
            fQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

            BulkResponse br = bqm.findOrganizations(fQualifiers,
                    names, null, null, null, null);

            if (br.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                System.out.println("Successfully queried the "
                        + "registry for organization matching the "
                        + "name pattern: \"" + cname + "\"");
                orgs = br.getCollection();
                System.out.println("Results found: " + orgs.size() + "\n");
                Iterator iter = orgs.iterator();
                while (iter.hasNext()) {
                    Organization org = (Organization) iter.next();
                    System.out.println("Organization Name: " + getName(org));
                    System.out.println("Organization Key: " + org.getKey().getId());
                    System.out.println("Organization Description: " + getDescription(org));

                    Collection services = org.getServices();
                    Iterator siter = services.iterator();
                    while (siter.hasNext()) {
                        Service service = (Service) siter.next();
                        System.out.println("\tService Name: " + getName(service));
                        System.out.println("\tService Key: " + service.getKey().getId());
                        System.out.println("\tService Description: " + getDescription(service));
                        Collection serviceBinding = service.getServiceBindings();
                        Iterator siterBinding = serviceBinding.iterator();
                        String URI = null;
                        while (siterBinding.hasNext()) {
                            ServiceBinding srvBinding = (ServiceBinding) siterBinding.next();
                            URI = srvBinding.getAccessURI();
                        }
                        if (URI != null) {
                            wsdl = URI;//+ "?WSDL";
                            //System.out.println("wsdl - " + wsdl);
                        }
                    }
                }
            } else {
                System.err.println("One or more JAXRExceptions occurred during the query operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }
        } catch (JAXRException e) {
            System.out.println("JAXR Exception" + e.getMessage());
        }
        return wsdl;
    }

    public String findByClassification(String cscheme, String keyName, String keyValue) throws JAXRException {
        Collection orgs = null;
        String wsdl = null;
        BusinessLifeCycleManager blm;
        try {
            loadProperties(propertiesFilePath);
            assignUserProperties(properties);
            setConnectionProperties();

            ConnectionFactory factory = ConnectionFactory.newInstance();
            factory.setProperties(connProps);
            Connection conn = factory.createConnection();
            RegistryService rs = conn.getRegistryService();
            BusinessQueryManager bqm = rs.getBusinessQueryManager();
            blm = rs.getBusinessLifeCycleManager();

            Collection findQualifiers = new ArrayList();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

            ClassificationScheme classificationScheme = bqm.findClassificationSchemeByName(findQualifiers, cscheme);
            Classification classification = blm.createClassification(classificationScheme, keyName, keyValue);


            Collection classifications = new ArrayList();
            classifications.add(classification);

            // Find using the Classification Info
            BulkResponse br = bqm.findOrganizations(findQualifiers, null, classifications, null, null, null);
            if (br.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                System.out.println("Successfully queried the "
                        + "registry for organization matching the "
                        + "classification : \"" + cscheme + "\"");
                orgs = br.getCollection();
                System.out.println("Results found: " + orgs.size() + "\n");
                Iterator iter = orgs.iterator();
                while (iter.hasNext()) {
                    Organization org = (Organization) iter.next();
                    System.out.println("Organization Name: " + getName(org));
                    System.out.println("Organization Key: " + org.getKey().getId());
                    System.out.println("Organization Description: " + getDescription(org));

                    Collection services = org.getServices();
                    Iterator siter = services.iterator();
                    while (siter.hasNext()) {
                        Service service = (Service) siter.next();
                        System.out.println("\tService Name: " + getName(service));
                        System.out.println("\tService Key: " + service.getKey().getId());
                        System.out.println("\tService Description: " + getDescription(service));
                        Collection serviceBinding = service.getServiceBindings();
                        Iterator siterBinding = serviceBinding.iterator();
                        String URI = null;
                        while (siterBinding.hasNext()) {
                            ServiceBinding srvBinding = (ServiceBinding) siterBinding.next();
                            URI = srvBinding.getAccessURI();
                            //System.out.println("\tService binding spec links: " + srvBinding.getDescription().getValue());
                        }
                        if (URI != null) {
                            wsdl = URI;//+ "?WSDL";
                            //System.out.println("wsdl - " + wsdl);
                        }
                    }
                }
            } else {
                System.err.println("One or more JAXRExceptions occurred during the query operation:");
                Collection exceptions = br.getExceptions();
                Iterator iter = exceptions.iterator();
                while (iter.hasNext()) {
                    Exception e = (Exception) iter.next();
                    System.err.println(e.toString());
                }
            }
        } catch (JAXRException e) {
            System.out.println("JAXR Exception" + e.getMessage());
        }
        return wsdl;
    }
}
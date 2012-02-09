/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package companies.publish;


import companies.service.CompaniesService;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Julio
 */
public class CompanyServicePublisher {
    public static void main(String[] args) {

        Endpoint.publish("http://localhost:8081/HW2_BETA/companies", new CompaniesService());

        System.out.println("The web service is published at http://localhost:8081/HW2_BETA/companies");

        System.out.println("To stop running the web service , terminate the java process");
    }
}

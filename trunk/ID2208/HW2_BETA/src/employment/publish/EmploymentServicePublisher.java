/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package employment.publish;

import employment.service.EmploymentService;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Anand
 */
public class EmploymentServicePublisher {

    public static void main(String[] args) {

        Endpoint.publish("http://localhost:8080/HW2_BETA/employment", new EmploymentService());

        System.out.println("The web service is published at http://localhost:8080/HW2_BETA/employment");

        System.out.println("To stop running the web service , terminate the java process");


    }
}

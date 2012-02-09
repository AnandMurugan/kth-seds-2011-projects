/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package university.service.publish;

import javax.xml.ws.Endpoint;
import university.service.UniversityWS;

/**
 *
 * @author Anand
 */
public class UniversityWebServicePublisher {

    public static void main(String[] args) {

        Endpoint.publish("http://localhost:8080/WSP_HW2_WebServices/university", new UniversityWS());

        System.out.println("The web service is published at http://localhost:8080/WSP_HW2_WebServices/university");

        System.out.println("To stop running the web service , terminate the java process");


    }
}

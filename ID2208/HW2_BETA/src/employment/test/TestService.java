/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package employment.test;

import employment.client.EmploymentService;
import employment.client.EmploymentService_Service;

/**
 *
 * @author julio
 */
public class TestService {
    public static void main(String[] args) {
        EmploymentService employmentService = new EmploymentService_Service().getEmploymentWebServicePort();
        employment.client.GetEmploymentRecordsResponse.Return record = employmentService.getEmploymentRecords("881201-1234");
        System.out.println("personal number: " + record.getPersonalNumber());
        System.out.println("number of records: " + record.getEmploymentRecord().size());
    }
}

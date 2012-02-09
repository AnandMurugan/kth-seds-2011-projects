/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package companies.test;

import companies.client.CompaniesService;
import companies.client.CompaniesService_Service;
import companies.client.GetCompaniesResponse.Return;
import java.util.List;

/**
 *
 * @author julio
 */
public class TestService {
    public static void main(String[] args) {
        CompaniesService companiesService = new CompaniesService_Service().getCompaniesWebServicePort();
        Return record = companiesService.getCompanies();
         System.out.println("number of companies: " + record.getCompanyInfo().size());
        
         List<Return.CompanyInfo> companyInfoList = record.getCompanyInfo();
        for(Return.CompanyInfo info: companyInfoList){
            System.out.println("The company name is: " + info.getName()); 
        }
    }
}

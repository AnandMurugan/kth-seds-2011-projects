package service;

/**
 * A test class to show a use case of PDP. 
 */
public class ToolPDP {
    public static void main(String[] args) {
        //Create a PDP deamon. The parameters are the port and the folder containing policies
        //PDPDeamon pdpD = new PDPDeamon(pdpPort, "C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2208 Programming Web Services\\Project\\XacmlServer\\policy");
        PDP pdp = new PDP("C:\\Users\\julio\\Documents\\Maestria KTH\\Courses\\Spring2012\\Period1\\ID2208 Programming Web Services\\Project\\XacmlServer\\policy");
        //Create and add a new policy to PDP
        ToolPDP tool = new ToolPDP();
        tool.addCustomerPolicy(pdp);
        tool.addOfficerPolicy(pdp);
        tool.addGaragePolicy(pdp);
    }

    private void addCustomerPolicy(PDP pdp) {
        Policy policy1 = new Policy();
        policy1.setPolicyID("CustomerReadPolicy");
        policy1.setDescription("Insurance claim resources authorization policy");
        policy1.addUserID("customer@insurance.se");
        policy1.addUserID("customer2@insurance.se");
        policy1.setGroupID("Customers");
        policy1.setGroupAdminID("admin@insurance.se");
        policy1.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy1.addResourceID("http://localhost:8080/ClaimsInsurance/resources/notifications");
        policy1.setActionID("read");
        policy1.setDecision(PDP.DECISION_PERMIT);
        policy1.generatePolicy();
        pdp.addPolicy(policy1);
        
        /*
        Policy policy2 = new Policy();
        policy2.setPolicyID("CustomerUpdatePolicy");
        policy2.setDescription("Insurance claim resources authorization policy");
        policy2.addUserID("customer@insurance.se");
        policy2.addUserID("customer2@insurance.se");
        policy2.setGroupID("Customers");
        policy2.setGroupAdminID("admin@insurance.se");
        policy2.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy2.setActionID("update");
        policy2.setDecision(PDP.DECISION_PERMIT);
        policy2.generatePolicy();
        pdp.addPolicy(policy2);
        
        Policy policy3 = new Policy();
        policy3.setPolicyID("CustomerAddPolicy");
        policy3.setDescription("Insurance claim resources authorization policy");
        policy3.addUserID("customer@insurance.se");
        policy3.addUserID("customer2@insurance.se");
        policy3.setGroupID("Customers");
        policy3.setGroupAdminID("admin@insurance.se");
        policy3.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy3.setActionID("add");
        policy3.setDecision(PDP.DECISION_PERMIT);
        policy3.generatePolicy();
        pdp.addPolicy(policy3);
        
        Policy policy4 = new Policy();
        policy4.setPolicyID("CustomerDeletePolicy");
        policy4.setDescription("Insurance claim resources authorization policy");
        policy4.addUserID("customer@insurance.se");
        policy4.addUserID("customer2@insurance.se");
        policy4.setGroupID("Customers");
        policy4.setGroupAdminID("admin@insurance.se");
        policy4.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy4.setActionID("delete");
        policy4.setDecision(PDP.DECISION_PERMIT);
        policy4.generatePolicy();
        pdp.addPolicy(policy4);
        */
    }
    
    private void addOfficerPolicy(PDP pdp){
        Policy policy1 = new Policy();
        policy1.setPolicyID("OfficerReadClaimsPolicy");
        policy1.setDescription("Insurance claim resources authorization policy");
        policy1.addUserID("junior@insurance.se");
        policy1.addUserID("senior@insurance.se");
        policy1.setGroupID("Officers");
        policy1.setGroupAdminID("admin@insurance.se");
        policy1.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy1.addResourceID("http://localhost:8080/ClaimsInsurance/resources/notifications");
        policy1.setActionID("read");
        policy1.setDecision(PDP.DECISION_PERMIT);
        policy1.generatePolicy();
        pdp.addPolicy(policy1);
     
        Policy policy2 = new Policy();
        policy2.setPolicyID("OfficerUpdateClaimsPolicy");
        policy2.setDescription("Insurance claim resources authorization policy");
        policy2.addUserID("junior@insurance.se");
        policy2.addUserID("senior@insurance.se");
        policy2.setGroupID("Officers");
        policy2.setGroupAdminID("admin@insurance.se");
        policy2.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy2.setActionID("udpate");
        policy2.setDecision(PDP.DECISION_PERMIT);
        policy2.generatePolicy();
        pdp.addPolicy(policy2);
        
    }
    
    private void addGaragePolicy(PDP pdp){
        Policy policy1 = new Policy();
        policy1.setPolicyID("GarageReadClaimsPolicy");
        policy1.setDescription("Insurance claim resources authorization policy");
        policy1.addUserID("garage@insurance.se");
        policy1.addUserID("garage2@insurance.se");
        policy1.setGroupID("Garages");
        policy1.setGroupAdminID("admin@insurance.se");
        policy1.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy1.addResourceID("http://localhost:8080/ClaimsInsurance/resources/notifications");
        policy1.setActionID("read");
        policy1.setDecision(PDP.DECISION_PERMIT);
        policy1.generatePolicy();
        pdp.addPolicy(policy1);
     
        Policy policy2 = new Policy();
        policy2.setPolicyID("GarageUpdateClaimsPolicy");
        policy2.setDescription("Insurance claim resources authorization policy");
        policy2.addUserID("garage1@insurance.se");
        policy2.addUserID("garage2@insurance.se");
        policy2.setGroupID("Garages");
        policy2.setGroupAdminID("admin@insurance.se");
        policy2.addResourceID("http://localhost:8080/ClaimsInsurance/resources/claims");
        policy2.setActionID("udpate");
        policy2.setDecision(PDP.DECISION_PERMIT);
        policy2.generatePolicy();
        pdp.addPolicy(policy2);
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.view;

import claims.controller.ClaimsFacade;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Anand
 */
@Named(value = "carDamageClaim")
@Dependent
public class CarDamageClaim {

    @EJB
    private ClaimsFacade claimsFacade;

    /** Creates a new instance of CarDamageClaim */
    public CarDamageClaim() {
    }
    private String carRegNo;
    private String carValue;
    private String carModel;
    Exception registrationError;
    //ClaimsClient client;

    /**
     * Creates a new instance of CustomerManager
     */
    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarMake(String carValue) {
        this.carValue = carValue;
    }

    public String getCarMake() {
        return carValue;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarModel() {
        return carModel;
    }

    public String submitClaim() {
        try {
            claimsFacade.submitClaim(carRegNo);
            //client.create(obj);
            System.out.println("[JSF Managed bean]car reg no:"+carRegNo);
            registrationError = null;
            return "registered";
        } catch (Exception e) {
            registrationError = e;
            return "not registered";
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace(System.err);
        registrationError = e;
    }

    public boolean checkSuccess() {
        return registrationError == null;
    }

    public Exception getException() {
        return registrationError;
    }
}

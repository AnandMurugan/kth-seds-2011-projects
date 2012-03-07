/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.view;

import claims.controller.ClaimsFacade;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Anand
 */
@Named(value = "carDamageClaim")
@RequestScoped
public class CarDamageClaim {

    @EJB
    private ClaimsFacade claimsFacade;

    /** Creates a new instance of CarDamageClaim */
    public CarDamageClaim() {
    }
    
    private String carRegNo;
    private Double carValue;
    private String carModel;
    Exception registrationError;

    private String owner;    
    private Integer carYear;
    
    
    
    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarValue(Double carValue) {
        this.carValue = carValue;
    }

    public Double getCarValue() {
        return carValue;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarModel() {
        return carModel;
    }

    public  String getOwner(){
        return owner;
    }
    
    public void setOwner(String owner){
        this.owner = owner;
    }
    
    public Integer getCarYear(){
        return this.carYear;
    }
    
    public void setCarYear(Integer carYear){
        this.carYear = carYear;
    }
    
    public String submitClaim() {
        try {
            claimsFacade.submitClaim(carRegNo, carValue, carModel, owner, carYear);
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

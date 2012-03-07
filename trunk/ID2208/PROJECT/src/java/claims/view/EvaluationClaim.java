/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.view;

import claims.controller.ClaimsFacade;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author julio
 */
@Named(value = "evaluationClaim")
@RequestScoped
public class EvaluationClaim {
    
    @EJB
    ClaimsFacade claimFacade;
    
    /** Creates a new instance of EvaluationClaim */
    public EvaluationClaim() {
        
    }
    
    
    
}

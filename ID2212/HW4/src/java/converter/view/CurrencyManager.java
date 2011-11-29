/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.view;

import converter.controller.CurrencyFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;

/**
 *
 * @author julio
 */
@Named(value = "currencyManager")
@ConversationScoped
public class CurrencyManager implements Serializable {
    private static final long serialVersionUID = 16247164405L;
    @EJB
    private CurrencyFacade currencyFacade;
    private float conversionResult;
    private float amountToConvert;
    private String originCurrency;
    private String destinyCurrency;
    private float dollarRate;
    
    /** Creates a new instance of CurrencyManager */
    public CurrencyManager() {
    }
    
    public void createCurrency(){
        
    }
            
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.view;

import converter.controller.ConverterFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;

/**
 *
 * @author julio
 */
@Named(value = "currencyManager")
@ConversationScoped
public class CurrencyManager implements Serializable {
    private static final long serialVersionUID = 16247164405L;
    @EJB
    private ConverterFacade converterFacade;
    private String symbol;
    private String country;
    private String currencyName;
    private boolean isLoggedIn;
    private String userName;
    private String password;
    private Exception transactionFailure;
    @Inject
    private Conversation conversation;

    /** Creates a new instance of CurrencyManager */
    public CurrencyManager() {
    }

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    public void createCurrency() {
        converterFacade.createCurrency(symbol, country, currencyName);
    }

    public void login() {
        try {
            startConversation();
            transactionFailure = null;
            isLoggedIn = converterFacade.login(userName, password);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    public String getSymbol(){
        return symbol;
    }
    
    public String getCountry(){
        return country;
    }
    
    public String getCurrencyName(){
        return currencyName;
    }
    
    
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
    
    public void setCountry(String country){
        this.country = country;
    }
    
    public void SetCurrencyname(String currencyName){
        this.currencyName = currencyName;
    }
    
    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        transactionFailure = e;
    }
}

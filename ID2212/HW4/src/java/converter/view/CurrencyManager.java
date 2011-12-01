/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.view;

import converter.controller.ConverterFacade;
import converter.model.CurrencyDTO;
import converter.model.ExchangeRate;
import converter.model.ExchangeRateNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
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
    private List<CurrencyDTO> currencies;
    private String toCurrencyStr;
    private String fromCurrencyStr;
    private float rate;
    private boolean loggedIn;
    private String userName;
    private String password;
    private Exception failure;
    @Inject
    private Conversation conversation;

    /** Creates a new instance of CurrencyManager */
    public CurrencyManager() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CurrencyDTO> getCurrencies() {
        currencies = converterFacade.getCurrencyList();
        return currencies;
    }

    public void setCurrencies(List<CurrencyDTO> currencies) {
        this.currencies = currencies;
    }

    public String getFromCurrencyStr() {
        return fromCurrencyStr;
    }

    public void setFromCurrencyStr(String fromCurrencyStr) {
        this.fromCurrencyStr = fromCurrencyStr;
    }

    public String getToCurrencyStr() {
        return toCurrencyStr;
    }

    public void setToCurrencyStr(String toCurrencyStr) {
        this.toCurrencyStr = toCurrencyStr;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
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
        try {
            converterFacade.createCurrency(symbol, country, currencyName);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void changeRate() {
        converterFacade.changeExchangeRate(getCurrencySymbol(fromCurrencyStr), getCurrencySymbol(toCurrencyStr), rate);
    }

    private String getCurrencySymbol(String value) {
        StringTokenizer st = new StringTokenizer(value, "()");
        st.nextToken();
        String symbol = st.nextToken().trim();

        return symbol;
    }

    public void login() {
        try {
            startConversation();
            failure = null;
            loggedIn = converterFacade.login(userName, password);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        failure = e;
    }

    public Exception getException() {
        return failure;
    }
}

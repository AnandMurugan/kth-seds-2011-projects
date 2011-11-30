/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.view;

import converter.controller.ConverterFacade;
import converter.model.CurrencyDTO;
import converter.model.ExchangeRateDTO;
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
@Named(value = "converter")
@ConversationScoped
public class ConverterManager implements Serializable {
    private static final long serialVersionUID = 16247164405L;
    @EJB
    private ConverterFacade converterFacade;
    @Inject
    private Conversation conversation;
    private String toCurrencyStr;
    private String fromCurrencyStr;
    private float amount;
    private float result;
    private List<CurrencyDTO> currencyList;
    private Exception conversionFailure;

    public ConverterManager() {
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public String getFromCurrencyStr() {
        return fromCurrencyStr;
    }

    public void setFromCurrencyStr(String fromCurrency) {
        this.fromCurrencyStr = fromCurrency;
    }

    public String getToCurrencyStr() {
        return toCurrencyStr;
    }

    public void setToCurrencyStr(String toCurrency) {
        this.toCurrencyStr = toCurrency;
    }

    public boolean isSuccess() {
        return conversionFailure == null;
    }

    public List<CurrencyDTO> getCurrencyList() {
        currencyList = converterFacade.getCurrencyList();
        return currencyList;
    }

    public void setCurrencyList(List<CurrencyDTO> currencyList) {
        this.currencyList = currencyList;
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

    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        conversionFailure = e;
    }

    public Exception getException() {
        return conversionFailure;
    }

    public void convert() {
        startConversation();
        try {
            ExchangeRateDTO rate = converterFacade.getExchangeRate(getCurrencySymbol(fromCurrencyStr), getCurrencySymbol(toCurrencyStr));
            result = amount * rate.getRate();
        } catch (ExchangeRateNotFoundException ex) {
            handleException(ex);
            result = Float.NaN;
        }
    }

    private String getCurrencySymbol(String value) {
        StringTokenizer st = new StringTokenizer(value, "()");
        st.nextToken();
        String symbol = st.nextToken().trim();

        return symbol;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.model;

/**
 *
 * @author Igor
 */
public class ExchangeRateNotFoundException extends Exception {
    private CurrencyDTO fromCurrency;
    private CurrencyDTO toCurrency;

    public ExchangeRateNotFoundException(CurrencyDTO fromCurrency, CurrencyDTO toCurrency) {
        super("Exchange rate not found");
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public CurrencyDTO getFromCurrency() {
        return fromCurrency;
    }

    public CurrencyDTO getToCurrency() {
        return toCurrency;
    }
}

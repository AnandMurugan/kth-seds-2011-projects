/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.Currency;
import converter.model.CurrencyDTO;
import converter.model.ExchangeRate;
import converter.model.ExchangeRateDTO;
import converter.model.ExchangeRateNotFoundException;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author julio
 */
@Stateless
@LocalBean
public class ConverterFacade {
    @PersistenceContext(unitName = "currencyPU")
    private EntityManager em;

    public List<CurrencyDTO> getCurrencyList() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ExchangeRateDTO getExchangeRate(CurrencyDTO fromCurrency, CurrencyDTO toCurrency) throws ExchangeRateNotFoundException {
        List<ExchangeRate> resList = em.createNamedQuery(ExchangeRate.GET_EXCHANGE_RATE_REQUEST, ExchangeRate.class).
                setParameter("from", fromCurrency).
                setParameter("from", fromCurrency).
                getResultList();
        if (resList.isEmpty()) {
            throw new ExchangeRateNotFoundException(fromCurrency, toCurrency);
        } else {
            return (ExchangeRateDTO) resList.get(0);
        }
    }

    public CurrencyDTO createCurrency(String symbol, String country, String name) {
        Currency newCurrency = new Currency(symbol, country, name);
        em.persist(newCurrency);
        return newCurrency;
    }

    public boolean login(String userId, String pwd) {
        boolean valid = false;
        if (userId.equals("test") && pwd.equals("test")) {
            valid = true;
        }
        /*if (found == null) {
        throw new EntityNotFoundException("No account with number " + acctNo);
        }*/
        return valid;
    }
}

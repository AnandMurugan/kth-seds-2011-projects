/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.Currency;
import converter.model.CurrencyDTO;
import converter.model.ExchangeRate;
import converter.model.ExchangeRateNotFoundException;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
        return em.createNamedQuery(Currency.GET_ALL_CURRENCIES_REQUEST, CurrencyDTO.class).
                getResultList();
    }

    public ExchangeRate getExchangeRate(String fromCurrencySymbolStr, String toCurrencySymbolStr) throws ExchangeRateNotFoundException {
        Currency toCurrency = em.createNamedQuery(Currency.GET_CURRENCY_BY_SYMBOL_REQUEST, Currency.class).
                setParameter("symbol", toCurrencySymbolStr).
                getSingleResult();

        Currency fromCurrency = em.createNamedQuery(Currency.GET_CURRENCY_BY_SYMBOL_REQUEST, Currency.class).
                setParameter("symbol", fromCurrencySymbolStr).
                getSingleResult();

        List<ExchangeRate> resList = em.createNamedQuery(ExchangeRate.GET_EXCHANGE_RATE_REQUEST, ExchangeRate.class).
                setParameter("from", fromCurrency).
                setParameter("to", toCurrency).
                getResultList();
        if (resList.isEmpty()) {
            throw new ExchangeRateNotFoundException(fromCurrency, toCurrency);
        } else {
            return resList.get(0);
        }
    }

    public float convert(String fromCurrencySymbolStr, String toCurrencySymbolStr, float amount) throws ExchangeRateNotFoundException {
        Currency toCurrency = em.createNamedQuery(Currency.GET_CURRENCY_BY_SYMBOL_REQUEST, Currency.class).
                setParameter("symbol", toCurrencySymbolStr).
                getSingleResult();

        Currency fromCurrency = em.createNamedQuery(Currency.GET_CURRENCY_BY_SYMBOL_REQUEST, Currency.class).
                setParameter("symbol", fromCurrencySymbolStr).
                getSingleResult();

        List<ExchangeRate> resList = em.createNamedQuery(ExchangeRate.GET_EXCHANGE_RATE_REQUEST, ExchangeRate.class).
                setParameter("from", fromCurrency).
                setParameter("to", toCurrency).
                getResultList();
        if (resList.isEmpty()) {
            throw new ExchangeRateNotFoundException(fromCurrency, toCurrency);
        } else {
            ExchangeRate er = resList.get(0);
            return amount * er.getRate();
        }
    }

    public void createCurrency(String symbol, String country, String name) {
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            Currency newCurrency = new Currency(symbol, country, name);
            em.persist(newCurrency);
        } finally {
            commitTransaction(transaction);
        }
    }

    public void changeExchangeRate(String fromCurrencySymbolStr, String toCurrencySymbolStr, float rate) {
        Currency toCurrency = em.createNamedQuery(Currency.GET_CURRENCY_BY_SYMBOL_REQUEST, Currency.class).
                setParameter("symbol", toCurrencySymbolStr).
                getSingleResult();

        Currency fromCurrency = em.createNamedQuery(Currency.GET_CURRENCY_BY_SYMBOL_REQUEST, Currency.class).
                setParameter("symbol", fromCurrencySymbolStr).
                getSingleResult();

        List<ExchangeRate> resList = em.createNamedQuery(ExchangeRate.GET_EXCHANGE_RATE_REQUEST, ExchangeRate.class).
                setParameter("from", fromCurrency).
                setParameter("to", toCurrency).
                getResultList();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            if (resList.isEmpty()) {
                ExchangeRate newEr = new ExchangeRate(fromCurrency, toCurrency, rate);
                em.persist(newEr);
            } else {
                ExchangeRate er = resList.get(0);
                er.setRate(rate);
                em.merge(er);
            }
        } finally {
            commitTransaction(transaction);
        }
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

    private EntityTransaction beginTransaction() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        return transaction;
    }

    private void commitTransaction(EntityTransaction transaction) {
        transaction.commit();
    }
}

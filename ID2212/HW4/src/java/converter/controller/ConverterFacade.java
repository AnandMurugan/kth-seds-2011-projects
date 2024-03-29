/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.ConverterUser;
import converter.model.ConverterUserDTO;
import converter.model.Currency;
import converter.model.CurrencyDTO;
import converter.model.ExchangeRate;
import converter.model.ExchangeRateNotFoundException;
import converter.model.NotUserFoundException;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author julio
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
        Currency newCurrency = new Currency(symbol, country, name);
        em.persist(newCurrency);

        ExchangeRate newExchangeRate = new ExchangeRate(newCurrency, newCurrency, 1.0f);
        em.persist(newExchangeRate);
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

        if (resList.isEmpty()) {
            ExchangeRate newEr = new ExchangeRate(fromCurrency, toCurrency, rate);
            em.persist(newEr);
        } else {
            ExchangeRate er = resList.get(0);
            er.setRate(rate);
            em.merge(er);
        }
    }

    public boolean login(String userId, String pwd) throws NotUserFoundException {

        try {
            boolean valid = false;
            ConverterUserDTO requestedUser = em.createNamedQuery(ConverterUser.GET_USER_BY_NAME_PASSWORD_REQUEST, ConverterUserDTO.class).setParameter("userName", userId).setParameter("password", pwd).getSingleResult();
            return true;
        } catch (NoResultException ex) {
            throw new NotUserFoundException(userId);
        }
    }
}

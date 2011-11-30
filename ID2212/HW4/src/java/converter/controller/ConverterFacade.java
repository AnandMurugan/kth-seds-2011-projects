/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.Currency;
import converter.model.CurrencyDTO;
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
    public List<CurrencyDTO> getCurrencyList() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @PersistenceContext(unitName = "currencyPU")
    private EntityManager em;

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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author julio
 */
@NamedQueries({
    @NamedQuery(name = Currency.GET_ALL_CURRENCIES,
    query = "SELECT c "
    + "FROM Currency c")
})
@Entity
public class Currency implements CurrencyDTO, Serializable {
    public static final String GET_ALL_CURRENCIES = "Currency_getAllCurrencies";
    private static final long serialVersionUID = 16247164401L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String symbol;
    private String country;
    private String name;

    public Currency() {
    }

    public Currency(String symbol, String country, String name) {
        this.symbol = symbol;
        this.country = country;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "converter.model.Currency[id=" + id + "]";
    }
}

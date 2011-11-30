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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Igor
 */
@NamedQueries({
    @NamedQuery(name = ExchangeRate.GET_EXCHANGE_RATE_REQUEST,
    query = "SELECT r "
    + "FROM ExchangeRate r "
    + "WHERE "
    + "(r.fromCurrency = :from) "
    + "AND "
    + "(r.toCurrency = :to)")
})
@Entity
public class ExchangeRate implements ExchangeRateDTO, Serializable {
    public static final String GET_EXCHANGE_RATE_REQUEST = "ExchangeRate_getExchangeRate";
    private static final long serialVersionUID = 123L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Currency fromCurrency;
    @ManyToOne
    private Currency toCurrency;
    private float rate;

    public ExchangeRate() {
    }

    public ExchangeRate(Currency fromCurrency, Currency toCurrency, float rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    @Override
    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExchangeRate)) {
            return false;
        }
        ExchangeRate other = (ExchangeRate) object;
        if (!other.fromCurrency.equals(fromCurrency) || !other.toCurrency.equals(toCurrency)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "converter.model.ExchangeRate[ id=" + id + " ]";
    }
}

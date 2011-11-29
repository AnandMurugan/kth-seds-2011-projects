/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author julio
 */
public class Currency implements CurrencyDTO {
private static final long serialVersionUID = 16247164401L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;
    private String symbol;
    private String country;
    private float dollarRate;
    
    
    public Currency(){
        
    }
    
    public Currency(String symbol, String country, float dollarRate){
        this.symbol = symbol;
        this.country = country;
        this.dollarRate = dollarRate;
    }
    
    @Override
    public String getSymbol(){
        return symbol;
    }
    
    @Override
    public String getCountry(){
        return country;
    }
    
    @Override
    public float getDollarRate(){
        return dollarRate;
    }
    
    public void setDollarRate(float newRate){
        this.dollarRate = newRate;
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

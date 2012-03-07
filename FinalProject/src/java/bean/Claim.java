/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Date;
import java.util.Random;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anand
 */
@XmlRootElement
public class Claim {
    private Long id;
    private String carRegNo;
    private Integer statusCd;
    private String statusDesc;
    private String type;
    private String owner;
    private Date policyExpiryDt;
    private Double carValue;
    
    public Claim(){
        Random generator = new Random(System.currentTimeMillis());
        id = generator.nextLong();
    }
    
    public Claim(String carRegNo, Double carValue){
        Random generator = new Random(System.currentTimeMillis());
        id = generator.nextLong();
        this.carRegNo = carRegNo;
        this.carValue = carValue;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public Double getCarValue() {
        return carValue;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Date getPolicyExpiryDt() {
        return policyExpiryDt;
    }

    public Integer getStatusCd() {
        return statusCd;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Claim{" + "id=" + id + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Claim other = (Claim) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
}

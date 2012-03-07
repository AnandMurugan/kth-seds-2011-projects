/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anand
 */
@Entity
@XmlRootElement
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String carRegNo;
    private String type;
    private Integer status_code;
    private String status_desc;
    private Double carValue;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public Double getCarValue() {
        return carValue;
    }

    public Claim() {
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public void setCarValue(Double carValue) {
        this.carValue = carValue;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Claim(String carRegNo, Double carValue) {
        this.carRegNo = carRegNo;
        this.carValue = carValue;
    }
    public Claim(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public Integer getStatus_code() {
        return status_code;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public String getType() {
        return type;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Claim)) {
            return false;
        }
        Claim other = (Claim) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Claim{" + "id=" + id + '}';
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claims.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
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
    // Updated by customer
    private Double carValue;
    private String owner;
    private String car_model;
    private Integer car_year;
    // Updated by the officer
    private String type;
    private Integer status_code;
    private String status_desc;   
    // Updated by garage
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date repair_date;    
    private Integer car_status;
    private Double repair_cost;

    // Constructors
    public Claim() {
    }
    
    public Claim(String carRegNo, Double carValue) {
        this.carRegNo = carRegNo;
        this.carValue = carValue;
    }
    public Claim(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    // Getters
    
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
    
    public String getOwner() {
        return owner;
    }

    public Double getCarValue() {
        return carValue;
    }

    public Date getRepair_Date(){
        return repair_date;
    }    
    
    public Integer getCar_status(){
        return car_status;
    }
    public Double getRepair_Cost(){
        return repair_cost;
    }
    
    public String getCar_model(){
        return car_model;
    }
    
    public Integer getCar_year(){
        return car_year;
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
   
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public void setRepair_Date(Date repair_date){
        this.repair_date = repair_date;
    }    
    
    public void setCar_status(Integer car_status){
        this.car_status = car_status;
    }
    
    public void setRepair_Cost(Double repair_cost){
        this.repair_cost = repair_cost;
    }
    
    public void setCar_model(String car_model){
        this.car_model = car_model;
    }
    
    public void setCar_year(Integer car_year){
        this.car_year = car_year;
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

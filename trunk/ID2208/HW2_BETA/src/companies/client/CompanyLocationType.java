
package companies.client;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for companyLocationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="companyLocationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://companies.service/companies}locationType">
 *       &lt;sequence>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "companyLocationType", propOrder = {
    "postalCode"
})
public class CompanyLocationType
    extends LocationType
{

    @XmlElement(namespace = "", required = true)
    protected BigInteger postalCode;

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPostalCode(BigInteger value) {
        this.postalCode = value;
    }

}


package employment.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for positionInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="positionInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="positionTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entranceDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType"/>
 *         &lt;element name="exitDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "positionInfoType", propOrder = {
    "positionTitle",
    "entranceDate",
    "exitDate"
})
@XmlSeeAlso({
    ExtendedPositionInfoType.class
})
public class PositionInfoType {

    @XmlElement(required = true)
    protected String positionTitle;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected Object entranceDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected Object exitDate;

    /**
     * Gets the value of the positionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPositionTitle() {
        return positionTitle;
    }

    /**
     * Sets the value of the positionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPositionTitle(String value) {
        this.positionTitle = value;
    }

    /**
     * Gets the value of the entranceDate property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEntranceDate() {
        return entranceDate;
    }

    /**
     * Sets the value of the entranceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEntranceDate(Object value) {
        this.entranceDate = value;
    }

    /**
     * Gets the value of the exitDate property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getExitDate() {
        return exitDate;
    }

    /**
     * Sets the value of the exitDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setExitDate(Object value) {
        this.exitDate = value;
    }

}

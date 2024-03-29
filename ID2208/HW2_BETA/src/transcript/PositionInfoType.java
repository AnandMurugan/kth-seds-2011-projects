//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.08 at 04:04:02 PM CET 
//


package transcript;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="entranceDate" type="{http://xml.netbeans.org/schema/commonSchema}minBoundedDate"/>
 *         &lt;element name="exitDate" type="{http://xml.netbeans.org/schema/commonSchema}minBoundedDate"/>
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
    protected XMLGregorianCalendar entranceDate;
    @XmlElement(required = true)
    protected XMLGregorianCalendar exitDate;

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
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEntranceDate() {
        return entranceDate;
    }

    /**
     * Sets the value of the entranceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEntranceDate(XMLGregorianCalendar value) {
        this.entranceDate = value;
    }

    /**
     * Gets the value of the exitDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExitDate() {
        return exitDate;
    }

    /**
     * Sets the value of the exitDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExitDate(XMLGregorianCalendar value) {
        this.exitDate = value;
    }

}

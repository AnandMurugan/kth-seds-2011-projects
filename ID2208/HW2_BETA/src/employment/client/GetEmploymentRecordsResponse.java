
package employment.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getEmploymentRecordsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getEmploymentRecordsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="employmentRecord" maxOccurs="unbounded" form="qualified">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="companyInfo" form="qualified">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="contactInfo" type="{http://xml.netbeans.org/schema/commonSchema}contactInformationType" form="qualified"/>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" form="qualified"/>
 *                             &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" form="qualified"/>
 *                             &lt;element name="positions" form="qualified">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="position" type="{http://xml.netbeans.org/schema/commonSchema}extendedPositionInfoType" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="personalNumber" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEmploymentRecordsResponse", namespace = "http://employment.service/employment", propOrder = {
    "_return"
})
public class GetEmploymentRecordsResponse {

    @XmlElement(name = "return", namespace = "")
    protected GetEmploymentRecordsResponse.Return _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link GetEmploymentRecordsResponse.Return }
     *     
     */
    public GetEmploymentRecordsResponse.Return getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetEmploymentRecordsResponse.Return }
     *     
     */
    public void setReturn(GetEmploymentRecordsResponse.Return value) {
        this._return = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="employmentRecord" maxOccurs="unbounded" form="qualified">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="companyInfo" form="qualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="contactInfo" type="{http://xml.netbeans.org/schema/commonSchema}contactInformationType" form="qualified"/>
     *                           &lt;/sequence>
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" form="qualified"/>
     *                   &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" form="qualified"/>
     *                   &lt;element name="positions" form="qualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="position" type="{http://xml.netbeans.org/schema/commonSchema}extendedPositionInfoType" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="personalNumber" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "employmentRecord"
    })
    public static class Return {

        @XmlElement(namespace = "http://employment.service/employment", required = true)
        protected List<GetEmploymentRecordsResponse.Return.EmploymentRecord> employmentRecord;
        @XmlAttribute(name = "personalNumber")
        protected String personalNumber;

        /**
         * Gets the value of the employmentRecord property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the employmentRecord property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEmploymentRecord().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GetEmploymentRecordsResponse.Return.EmploymentRecord }
         * 
         * 
         */
        public List<GetEmploymentRecordsResponse.Return.EmploymentRecord> getEmploymentRecord() {
            if (employmentRecord == null) {
                employmentRecord = new ArrayList<GetEmploymentRecordsResponse.Return.EmploymentRecord>();
            }
            return this.employmentRecord;
        }

        /**
         * Gets the value of the personalNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPersonalNumber() {
            return personalNumber;
        }

        /**
         * Sets the value of the personalNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPersonalNumber(String value) {
            this.personalNumber = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="companyInfo" form="qualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="contactInfo" type="{http://xml.netbeans.org/schema/commonSchema}contactInformationType" form="qualified"/>
         *                 &lt;/sequence>
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" form="qualified"/>
         *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" form="qualified"/>
         *         &lt;element name="positions" form="qualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="position" type="{http://xml.netbeans.org/schema/commonSchema}extendedPositionInfoType" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "companyInfo",
            "startDate",
            "endDate",
            "positions"
        })
        public static class EmploymentRecord {

            @XmlElement(namespace = "http://employment.service/employment", required = true)
            protected GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo companyInfo;
            @XmlElement(namespace = "http://employment.service/employment", required = true)
            @XmlSchemaType(name = "anySimpleType")
            protected Object startDate;
            @XmlElement(namespace = "http://employment.service/employment", required = true)
            @XmlSchemaType(name = "anySimpleType")
            protected Object endDate;
            @XmlElement(namespace = "http://employment.service/employment", required = true)
            protected GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions positions;

            /**
             * Gets the value of the companyInfo property.
             * 
             * @return
             *     possible object is
             *     {@link GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo }
             *     
             */
            public GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo getCompanyInfo() {
                return companyInfo;
            }

            /**
             * Sets the value of the companyInfo property.
             * 
             * @param value
             *     allowed object is
             *     {@link GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo }
             *     
             */
            public void setCompanyInfo(GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo value) {
                this.companyInfo = value;
            }

            /**
             * Gets the value of the startDate property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getStartDate() {
                return startDate;
            }

            /**
             * Sets the value of the startDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setStartDate(Object value) {
                this.startDate = value;
            }

            /**
             * Gets the value of the endDate property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getEndDate() {
                return endDate;
            }

            /**
             * Sets the value of the endDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setEndDate(Object value) {
                this.endDate = value;
            }

            /**
             * Gets the value of the positions property.
             * 
             * @return
             *     possible object is
             *     {@link GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions }
             *     
             */
            public GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions getPositions() {
                return positions;
            }

            /**
             * Sets the value of the positions property.
             * 
             * @param value
             *     allowed object is
             *     {@link GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions }
             *     
             */
            public void setPositions(GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions value) {
                this.positions = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="contactInfo" type="{http://xml.netbeans.org/schema/commonSchema}contactInformationType" form="qualified"/>
             *       &lt;/sequence>
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "contactInfo"
            })
            public static class CompanyInfo {

                @XmlElement(namespace = "http://employment.service/employment", required = true)
                protected ContactInformationType contactInfo;
                @XmlAttribute(name = "name")
                @XmlSchemaType(name = "anySimpleType")
                protected String name;

                /**
                 * Gets the value of the contactInfo property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ContactInformationType }
                 *     
                 */
                public ContactInformationType getContactInfo() {
                    return contactInfo;
                }

                /**
                 * Sets the value of the contactInfo property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ContactInformationType }
                 *     
                 */
                public void setContactInfo(ContactInformationType value) {
                    this.contactInfo = value;
                }

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="position" type="{http://xml.netbeans.org/schema/commonSchema}extendedPositionInfoType" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "position"
            })
            public static class Positions {

                @XmlElement(namespace = "http://employment.service/employment")
                protected List<ExtendedPositionInfoType> position;

                /**
                 * Gets the value of the position property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the position property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPosition().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ExtendedPositionInfoType }
                 * 
                 * 
                 */
                public List<ExtendedPositionInfoType> getPosition() {
                    if (position == null) {
                        position = new ArrayList<ExtendedPositionInfoType>();
                    }
                    return this.position;
                }

            }

        }

    }

}

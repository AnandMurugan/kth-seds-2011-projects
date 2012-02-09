
package university.service.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for retrieveTranscriptResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="retrieveTranscriptResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="transcriptRecord" form="qualified">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *                             &lt;element name="university" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *                             &lt;element name="degree" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *                             &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}int" form="qualified"/>
 *                             &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *                             &lt;element name="courses" form="qualified">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="course" maxOccurs="unbounded" form="qualified">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *                                                 &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *                                                 &lt;element name="credits" type="{http://www.w3.org/2001/XMLSchema}decimal" form="qualified"/>
 *                                                 &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade" form="qualified"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="personalNumber" type="{http://www.w3.org/2001/XMLSchema}string" />
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
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retrieveTranscriptResponse", propOrder = {
    "_return"
})
public class RetrieveTranscriptResponse {

    @XmlElement(name = "return", namespace = "")
    protected RetrieveTranscriptResponse.Return _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link RetrieveTranscriptResponse.Return }
     *     
     */
    public RetrieveTranscriptResponse.Return getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetrieveTranscriptResponse.Return }
     *     
     */
    public void setReturn(RetrieveTranscriptResponse.Return value) {
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
     *         &lt;element name="transcriptRecord" form="qualified">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
     *                   &lt;element name="university" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
     *                   &lt;element name="degree" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
     *                   &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}int" form="qualified"/>
     *                   &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
     *                   &lt;element name="courses" form="qualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="course" maxOccurs="unbounded" form="qualified">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
     *                                       &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
     *                                       &lt;element name="credits" type="{http://www.w3.org/2001/XMLSchema}decimal" form="qualified"/>
     *                                       &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade" form="qualified"/>
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
    @XmlType(name = "", propOrder = {
        "transcriptRecord"
    })
    public static class Return {

        @XmlElement(required = true)
        protected RetrieveTranscriptResponse.Return.TranscriptRecord transcriptRecord;

        /**
         * Gets the value of the transcriptRecord property.
         * 
         * @return
         *     possible object is
         *     {@link RetrieveTranscriptResponse.Return.TranscriptRecord }
         *     
         */
        public RetrieveTranscriptResponse.Return.TranscriptRecord getTranscriptRecord() {
            return transcriptRecord;
        }

        /**
         * Sets the value of the transcriptRecord property.
         * 
         * @param value
         *     allowed object is
         *     {@link RetrieveTranscriptResponse.Return.TranscriptRecord }
         *     
         */
        public void setTranscriptRecord(RetrieveTranscriptResponse.Return.TranscriptRecord value) {
            this.transcriptRecord = value;
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
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
         *         &lt;element name="university" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
         *         &lt;element name="degree" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
         *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}int" form="qualified"/>
         *         &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
         *         &lt;element name="courses" form="qualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="course" maxOccurs="unbounded" form="qualified">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
         *                             &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
         *                             &lt;element name="credits" type="{http://www.w3.org/2001/XMLSchema}decimal" form="qualified"/>
         *                             &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade" form="qualified"/>
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
            "name",
            "university",
            "degree",
            "year",
            "major",
            "courses"
        })
        public static class TranscriptRecord {

            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            protected String university;
            @XmlElement(required = true)
            protected String degree;
            protected int year;
            @XmlElement(required = true)
            protected String major;
            @XmlElement(required = true)
            protected RetrieveTranscriptResponse.Return.TranscriptRecord.Courses courses;
            @XmlAttribute(name = "personalNumber")
            protected String personalNumber;

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

            /**
             * Gets the value of the university property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUniversity() {
                return university;
            }

            /**
             * Sets the value of the university property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUniversity(String value) {
                this.university = value;
            }

            /**
             * Gets the value of the degree property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDegree() {
                return degree;
            }

            /**
             * Sets the value of the degree property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDegree(String value) {
                this.degree = value;
            }

            /**
             * Gets the value of the year property.
             * 
             */
            public int getYear() {
                return year;
            }

            /**
             * Sets the value of the year property.
             * 
             */
            public void setYear(int value) {
                this.year = value;
            }

            /**
             * Gets the value of the major property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMajor() {
                return major;
            }

            /**
             * Sets the value of the major property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMajor(String value) {
                this.major = value;
            }

            /**
             * Gets the value of the courses property.
             * 
             * @return
             *     possible object is
             *     {@link RetrieveTranscriptResponse.Return.TranscriptRecord.Courses }
             *     
             */
            public RetrieveTranscriptResponse.Return.TranscriptRecord.Courses getCourses() {
                return courses;
            }

            /**
             * Sets the value of the courses property.
             * 
             * @param value
             *     allowed object is
             *     {@link RetrieveTranscriptResponse.Return.TranscriptRecord.Courses }
             *     
             */
            public void setCourses(RetrieveTranscriptResponse.Return.TranscriptRecord.Courses value) {
                this.courses = value;
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
             *         &lt;element name="course" maxOccurs="unbounded" form="qualified">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
             *                   &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
             *                   &lt;element name="credits" type="{http://www.w3.org/2001/XMLSchema}decimal" form="qualified"/>
             *                   &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade" form="qualified"/>
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
                "course"
            })
            public static class Courses {

                @XmlElement(required = true)
                protected List<RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course> course;

                /**
                 * Gets the value of the course property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the course property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getCourse().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course }
                 * 
                 * 
                 */
                public List<RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course> getCourse() {
                    if (course == null) {
                        course = new ArrayList<RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course>();
                    }
                    return this.course;
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
                 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
                 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
                 *         &lt;element name="credits" type="{http://www.w3.org/2001/XMLSchema}decimal" form="qualified"/>
                 *         &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade" form="qualified"/>
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
                    "name",
                    "code",
                    "credits",
                    "grade"
                })
                public static class Course {

                    @XmlElement(required = true)
                    protected String name;
                    @XmlElement(required = true)
                    protected String code;
                    @XmlElement(required = true)
                    protected BigDecimal credits;
                    @XmlElement(required = true)
                    protected Grade grade;

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

                    /**
                     * Gets the value of the code property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCode() {
                        return code;
                    }

                    /**
                     * Sets the value of the code property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCode(String value) {
                        this.code = value;
                    }

                    /**
                     * Gets the value of the credits property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigDecimal }
                     *     
                     */
                    public BigDecimal getCredits() {
                        return credits;
                    }

                    /**
                     * Sets the value of the credits property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigDecimal }
                     *     
                     */
                    public void setCredits(BigDecimal value) {
                        this.credits = value;
                    }

                    /**
                     * Gets the value of the grade property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Grade }
                     *     
                     */
                    public Grade getGrade() {
                        return grade;
                    }

                    /**
                     * Sets the value of the grade property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Grade }
                     *     
                     */
                    public void setGrade(Grade value) {
                        this.grade = value;
                    }

                }

            }

        }

    }

}

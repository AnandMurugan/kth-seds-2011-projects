
package employment.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the employment.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetEmploymentRecordsResponse_QNAME = new QName("http://employment.service/employment", "getEmploymentRecordsResponse");
    private final static QName _GetEmploymentRecords_QNAME = new QName("http://employment.service/employment", "getEmploymentRecords");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: employment.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetEmploymentRecordsResponse.Return.EmploymentRecord }
     * 
     */
    public GetEmploymentRecordsResponse.Return.EmploymentRecord createGetEmploymentRecordsResponseReturnEmploymentRecord() {
        return new GetEmploymentRecordsResponse.Return.EmploymentRecord();
    }

    /**
     * Create an instance of {@link GetEmploymentRecordsResponse.Return }
     * 
     */
    public GetEmploymentRecordsResponse.Return createGetEmploymentRecordsResponseReturn() {
        return new GetEmploymentRecordsResponse.Return();
    }

    /**
     * Create an instance of {@link ContactInformationType }
     * 
     */
    public ContactInformationType createContactInformationType() {
        return new ContactInformationType();
    }

    /**
     * Create an instance of {@link GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo }
     * 
     */
    public GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo createGetEmploymentRecordsResponseReturnEmploymentRecordCompanyInfo() {
        return new GetEmploymentRecordsResponse.Return.EmploymentRecord.CompanyInfo();
    }

    /**
     * Create an instance of {@link PositionInfoType }
     * 
     */
    public PositionInfoType createPositionInfoType() {
        return new PositionInfoType();
    }

    /**
     * Create an instance of {@link GetEmploymentRecords }
     * 
     */
    public GetEmploymentRecords createGetEmploymentRecords() {
        return new GetEmploymentRecords();
    }

    /**
     * Create an instance of {@link GetEmploymentRecordsResponse }
     * 
     */
    public GetEmploymentRecordsResponse createGetEmploymentRecordsResponse() {
        return new GetEmploymentRecordsResponse();
    }

    /**
     * Create an instance of {@link GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions }
     * 
     */
    public GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions createGetEmploymentRecordsResponseReturnEmploymentRecordPositions() {
        return new GetEmploymentRecordsResponse.Return.EmploymentRecord.Positions();
    }

    /**
     * Create an instance of {@link ExtendedPositionInfoType }
     * 
     */
    public ExtendedPositionInfoType createExtendedPositionInfoType() {
        return new ExtendedPositionInfoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmploymentRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://employment.service/employment", name = "getEmploymentRecordsResponse")
    public JAXBElement<GetEmploymentRecordsResponse> createGetEmploymentRecordsResponse(GetEmploymentRecordsResponse value) {
        return new JAXBElement<GetEmploymentRecordsResponse>(_GetEmploymentRecordsResponse_QNAME, GetEmploymentRecordsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmploymentRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://employment.service/employment", name = "getEmploymentRecords")
    public JAXBElement<GetEmploymentRecords> createGetEmploymentRecords(GetEmploymentRecords value) {
        return new JAXBElement<GetEmploymentRecords>(_GetEmploymentRecords_QNAME, GetEmploymentRecords.class, null, value);
    }

}


package university.service.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the university.service.client package. 
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

    private final static QName _RetrieveTranscript_QNAME = new QName("http://university.service/university", "retrieveTranscript");
    private final static QName _RetrieveTranscriptResponse_QNAME = new QName("http://university.service/university", "retrieveTranscriptResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: university.service.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Transcript }
     * 
     */
    public Transcript createTranscript() {
        return new Transcript();
    }

    /**
     * Create an instance of {@link RetrieveTranscriptResponse }
     * 
     */
    public RetrieveTranscriptResponse createRetrieveTranscriptResponse() {
        return new RetrieveTranscriptResponse();
    }

    /**
     * Create an instance of {@link RetrieveTranscriptResponse.Return }
     * 
     */
    public RetrieveTranscriptResponse.Return createRetrieveTranscriptResponseReturn() {
        return new RetrieveTranscriptResponse.Return();
    }

    /**
     * Create an instance of {@link RetrieveTranscriptResponse.Return.TranscriptRecord }
     * 
     */
    public RetrieveTranscriptResponse.Return.TranscriptRecord createRetrieveTranscriptResponseReturnTranscriptRecord() {
        return new RetrieveTranscriptResponse.Return.TranscriptRecord();
    }

    /**
     * Create an instance of {@link RetrieveTranscriptResponse.Return.TranscriptRecord.Courses }
     * 
     */
    public RetrieveTranscriptResponse.Return.TranscriptRecord.Courses createRetrieveTranscriptResponseReturnTranscriptRecordCourses() {
        return new RetrieveTranscriptResponse.Return.TranscriptRecord.Courses();
    }

    /**
     * Create an instance of {@link Transcript.TranscriptRecord }
     * 
     */
    public Transcript.TranscriptRecord createTranscriptTranscriptRecord() {
        return new Transcript.TranscriptRecord();
    }

    /**
     * Create an instance of {@link Transcript.TranscriptRecord.Courses }
     * 
     */
    public Transcript.TranscriptRecord.Courses createTranscriptTranscriptRecordCourses() {
        return new Transcript.TranscriptRecord.Courses();
    }

    /**
     * Create an instance of {@link RetrieveTranscript }
     * 
     */
    public RetrieveTranscript createRetrieveTranscript() {
        return new RetrieveTranscript();
    }

    /**
     * Create an instance of {@link RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course }
     * 
     */
    public RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course createRetrieveTranscriptResponseReturnTranscriptRecordCoursesCourse() {
        return new RetrieveTranscriptResponse.Return.TranscriptRecord.Courses.Course();
    }

    /**
     * Create an instance of {@link Transcript.TranscriptRecord.Courses.Course }
     * 
     */
    public Transcript.TranscriptRecord.Courses.Course createTranscriptTranscriptRecordCoursesCourse() {
        return new Transcript.TranscriptRecord.Courses.Course();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveTranscript }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://university.service/university", name = "retrieveTranscript")
    public JAXBElement<RetrieveTranscript> createRetrieveTranscript(RetrieveTranscript value) {
        return new JAXBElement<RetrieveTranscript>(_RetrieveTranscript_QNAME, RetrieveTranscript.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveTranscriptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://university.service/university", name = "retrieveTranscriptResponse")
    public JAXBElement<RetrieveTranscriptResponse> createRetrieveTranscriptResponse(RetrieveTranscriptResponse value) {
        return new JAXBElement<RetrieveTranscriptResponse>(_RetrieveTranscriptResponse_QNAME, RetrieveTranscriptResponse.class, null, value);
    }

}

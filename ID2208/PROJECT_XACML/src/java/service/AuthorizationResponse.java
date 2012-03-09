package service;

import java.io.ByteArrayOutputStream;

import com.sun.xacml.ctx.ResponseCtx;
import java.io.Serializable;
import javax.xml.bind.annotation.*;

/**
 * The authorization response 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "resourceID",
    "decision",
    "statusCode"})
@XmlRootElement(name = "response")
public class AuthorizationResponse {
    //private static final long serialVersionUID = 1L;
    @XmlElement(required = true)
    private String resourceID;
    @XmlElement(required = true)
    private String decision;
    @XmlElement(required = true)
    private String statusCode;

    public AuthorizationResponse(){
        
    }
    
    public AuthorizationResponse(String resourceID, String decision, String statusCode) {
        this.resourceID = resourceID;
        this.decision = decision;
        this.statusCode = statusCode;
    }

    public String getResourceID() {
        return resourceID;
    }

    public String getDecision() {
        return decision;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    boolean isPermit() {
        return decision.equals("Permit");
    }

    public AuthorizationResponse(ResponseCtx response) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.encode(out);
        String doc = out.toString();
        doc = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + doc;

        resourceID = "";
        int p = doc.indexOf("ResourceID");
        p = doc.indexOf("\"", p);
        int q = doc.indexOf("\"", p + 1);
        resourceID = doc.substring(p + 1, q);

        decision = "";
        p = doc.indexOf("<Decision>");
        p = doc.indexOf(">", p);
        q = doc.indexOf("<", p + 1);
        decision = doc.substring(p + 1, q);

        statusCode = "";
        p = doc.indexOf("StatusCode");
        p = doc.indexOf("status", p);
        p = doc.indexOf(":", p);
        q = doc.indexOf("\"", p + 1);
        statusCode = doc.substring(p + 1, q);
    }
}

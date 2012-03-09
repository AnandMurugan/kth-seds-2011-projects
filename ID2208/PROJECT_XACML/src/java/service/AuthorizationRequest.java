package service;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

/**
 * The authorization request 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "userID",
    "groupID",
    "groupAdminID",
    "resourceID",
    "actionID"})
@XmlRootElement(name = "request", namespace="http://xml.netbeans.org/schema/RequestXmlSchema")
public class AuthorizationRequest {
    //private static final long serialVersionUID = 1L;
    @XmlElement(name="userId", required = true)
    private String userID;
    @XmlElement(name="groupId", required = true)
    private String groupID;
    @XmlElement(name="groupAdminId",required = true)
    private String groupAdminID;
    @XmlElement(name="resourceId",required = true)
    private String resourceID;
    @XmlElement(name="actionId",required = true)
    private String actionID;
    
    
    public AuthorizationRequest(String userID, String groupID, String groupAdminID,
            String resourceID, String actionID) {
        this.userID = userID;
        this.groupID = groupID;
        this.groupAdminID = groupAdminID;
        this.resourceID = resourceID;
        this.actionID = actionID;
    }

    public AuthorizationRequest() {
        this.userID = null;
        this.groupID = null;
        this.groupAdminID = null;
        this.resourceID = null;
        this.actionID = null;
    }

    public String getUserID() {
        return userID;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupAdminID() {
        return groupAdminID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public String getActionID() {
        return actionID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setGroupAdminID(String groupAdminID) {
        this.groupAdminID = groupAdminID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }
}
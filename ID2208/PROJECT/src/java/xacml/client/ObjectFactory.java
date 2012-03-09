/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xacml.client;

import javax.xml.bind.annotation.XmlRegistry;
import service.AuthorizationRequest;

/**
 *
 * @author julio
 */
@XmlRegistry
public class ObjectFactory {
    public ObjectFactory() {
    }
    
    public AuthorizationRequest createAuthorizationRequest(){
        return new AuthorizationRequest();
    }
}

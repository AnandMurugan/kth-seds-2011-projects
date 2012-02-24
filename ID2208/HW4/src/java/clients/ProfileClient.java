/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/** Jersey REST client generated for REST resource:ProfileResource [Profile]<br>
 *  USAGE:<pre>
 *        ProfileClient client = new ProfileClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 *  </pre>
 * @author julio
 */
public class ProfileClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/PWS_HW4/resources";

    public ProfileClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("Profile");
    }

    public String getXml() throws UniformInterfaceException {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public String generateProfile(Object requestEntity) throws UniformInterfaceException {
        return webResource.type(javax.ws.rs.core.MediaType.APPLICATION_XML).accept(javax.ws.rs.core.MediaType.APPLICATION_XML).post(String.class, requestEntity);
    }

    public void close() {
        client.destroy();
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/** Jersey REST client generated for REST resource:EmploymentResource [employment]<br>
 *  USAGE:<pre>
 *        EmploymentClient client = new EmploymentClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 *  </pre>
 * @author julio
 */
public class EmploymentClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/PWS_HW4/resources";

    public EmploymentClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("employment");
    }

    public String getXml() throws UniformInterfaceException {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public void close() {
        client.destroy();
    }
    
}

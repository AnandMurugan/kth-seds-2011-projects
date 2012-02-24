/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/** Jersey REST client generated for REST resource:TranscriptResource [transcript/{personalNumber}]<br>
 *  USAGE:<pre>
 *        TranscriptClient client = new TranscriptClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 *  </pre>
 * @author julio
 */
public class TranscriptClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/PWS_HW4/resources";

    public TranscriptClient(String personalNumber) {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        String resourcePath = java.text.MessageFormat.format("transcript/{0}", new Object[]{personalNumber});
        webResource = client.resource(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String personalNumber) {
        String resourcePath = java.text.MessageFormat.format("transcript/{0}", new Object[]{personalNumber});
        webResource = client.resource(BASE_URI).path(resourcePath);
    }

    public String getXml() throws UniformInterfaceException {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public void close() {
        client.destroy();
    }
    
}

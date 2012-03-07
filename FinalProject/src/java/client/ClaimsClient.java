/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import bean.Claim;
import com.sun.jersey.api.client.*;
import javax.xml.bind.JAXBElement;

/**
 * Jersey REST client generated for REST resource:ClaimsResource [/claims]<br>
 * USAGE:
 * <pre>
 *        ClaimsClient client = new ClaimsClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Anand
 */
public class ClaimsClient {

    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/FinalProject/resources";

    public ClaimsClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("claims");
    }

   public ClientResponse putClaim(Object requestEntity) throws UniformInterfaceException {
        return webResource.type(javax.ws.rs.core.MediaType.APPLICATION_XML).put(ClientResponse.class, requestEntity);
    }

    public <T> T getClaim(Class<T> responseType) throws UniformInterfaceException {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void deleteClaim() throws UniformInterfaceException {
        webResource.delete();
    }

    public void close() {
        client.destroy();
    }

//    public Claim getClaim(String id) throws UniformInterfaceException {
//        GenericType<JAXBElement<Claim>> generic = new GenericType<JAXBElement<Claim>>() {
//        };
//        JAXBElement<Claim> jaxbContact = webResource.path(id).accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(generic);
//        Claim claim = jaxbContact.getValue();
//        System.out.println(claim.getId() + ": " + claim.getCarRegNo());
//        return claim;
//    }

    
}

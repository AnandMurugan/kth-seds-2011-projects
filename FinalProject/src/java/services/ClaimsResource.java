/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import bean.Claim;
import com.sun.jersey.api.NotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import storage.ClaimStore;

/**
 * REST Web Service
 *
 * @author Anand
 */
@Path("/claims")
public class ClaimsResource {

    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;
    private Long claimId;

    /**
     * Creates a new instance of ClaimsResource
     */
    public ClaimsResource() {
    }

    public ClaimsResource(UriInfo uriInfo, Request request, Long claimId) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.claimId = claimId;
    }

    @GET
    @Produces("application/xml")
    public Claim getClaim() {
        Claim claim = ClaimStore.getStore().get(claimId);
        if (claim == null) {
            throw new NotFoundException("No such Contact.");
        }
        return claim;
    }

    @PUT
    @Consumes("application/xml")
    public Response putClaim(JAXBElement<Claim> jaxbContact) {
        Claim c = jaxbContact.getValue();
        return putAndGetResponse(c);
    }

    private Response putAndGetResponse(Claim c) {
        Response res;
        if (ClaimStore.getStore().containsKey(c.getId())) {
            res = Response.noContent().build();
        } else {
            res = Response.created(uriInfo.getAbsolutePath()).build();
        }
        ClaimStore.getStore().put(c.getId(), c);
        return res;
    }

    @DELETE
    public void deleteClaim() {
        Claim c = ClaimStore.getStore().remove(claimId);
        if (c == null) {
            throw new NotFoundException("No such Claim.");
        }
    }

}

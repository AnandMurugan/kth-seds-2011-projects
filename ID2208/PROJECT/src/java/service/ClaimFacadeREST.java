/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import claims.model.Claim;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Anand
 */
@Stateless
@Path("claims.model.claim")
public class ClaimFacadeREST extends AbstractFacade<Claim> {
    @PersistenceContext(unitName = "ClaimsInsurancePU")
    private EntityManager em;

    public ClaimFacadeREST() {
        super(Claim.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Claim entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Claim entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Claim find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Claim> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Claim> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

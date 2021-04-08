/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.boundary;

import it.tss.blog.blog.control.UserStore;
import it.tss.blog.blog.entity.User;
import java.util.List;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author scian
 */
@Path("/users")
@DenyAll
public class UsersResource {

    @Context
    ResourceContext resource;

    @Inject
    private UserStore store;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response createAccount(User u) {
        if (u.getUsr() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("caused by", "usr mancante")
                    .build();
        }
        User saved = store.create(u);
        return Response.status(Response.Status.CREATED)
                .entity(u)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<User> search(@QueryParam("start") int start, @QueryParam("maxResult") int maxResult) {
        return store.search(start, maxResult);
    }

    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public UserResource find(@PathParam("id") Long id) {
        User found = store.find(id).orElseThrow(() -> new NotFoundException());
        if (found == null) {
            throw new NotFoundException();
        }
        UserResource sub = resource.getResource(UserResource.class);
        sub.setId(id);
        return sub;
    }

}

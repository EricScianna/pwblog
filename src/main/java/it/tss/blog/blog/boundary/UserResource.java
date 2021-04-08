/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.boundary;

import it.tss.blog.blog.control.UserStore;
import it.tss.blog.blog.entity.User;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author scian
 */
@RolesAllowed({"ADMIN", "USER"})
public class UserResource {

    @Inject
    UserStore store;

    @Context
    ResourceContext resource;

    private Long id;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<User> find() {
        Optional<User> found = store.find(id);
        if (found == null) {
            throw new NotFoundException();
        }
        return store.find(id);
    }

    @PATCH
    @Path("{update}")
    @Produces(MediaType.APPLICATION_JSON)
    public User update(JsonObject json) {
        User found = store.find(id).orElseThrow(() -> new NotFoundException());
        found.setFname(json.getString("fname"));
        found.setLname(json.getString("lname"));
        found.setUsr(json.getString("usr"));
        found.setPwd(json.getString("pwd"));
        return store.update(found);
    }

    @DELETE
    public Response delete() {
        User found = store.find(id).orElseThrow(() -> new NotFoundException());
        if (found == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        store.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /*
    getter/setter
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

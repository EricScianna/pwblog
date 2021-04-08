/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.boundary;

import it.tss.blog.blog.control.ArticleStore;
import it.tss.blog.blog.entity.Article;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author scian
 */

@Path("/articles")
@DenyAll
public class ArticlesResource {

    @Inject
    ArticleStore store;

    @Context
    private ResourceContext resource;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonObject> search() {
        return store.search()
                .stream()
                .map(v -> v.toJson())
                .collect(Collectors.toList());
    }

    @Path("{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public ArticleResource find(@PathParam("id") Long id) {
        ArticleResource sub = resource.getResource(ArticleResource.class);
        sub.setArticleId(id);
        return sub;
    }

    @POST
    @RolesAllowed({"ADMIN", "USER"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject create(JsonObject json) {
        String title = json.getString("title");
        String body = json.getString("body");
        String tags = json.getString("tags");
        Article article = new Article(title, body, tags);
        Article art = store.create(article);
        return art.toJson();
    }

}

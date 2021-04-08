/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.boundary;

import it.tss.blog.blog.control.ArticleStore;
import it.tss.blog.blog.control.CommentStore;
import it.tss.blog.blog.control.UserStore;
import it.tss.blog.blog.entity.Article;
import it.tss.blog.blog.entity.Comment;
import it.tss.blog.blog.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

/**
 *
 * @author scian
 */
public class ArticleResource {

    @Inject
    private ArticleStore store;

    @Context
    ResourceContext resource;
    @Inject
    CommentStore commentStore;
    @Inject
    ArticleStore articleStore;
    @Inject
    UserStore userStore;

    @Inject
    @Claim(standard = Claims.sub)
    String userId;

    private Long articleId;

    @PATCH
    @RolesAllowed({"ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject update(JsonObject json) {
        String title = json.getString("title");
        String body = json.getString("body");
        String tags = json.getString("tags");
        Article article = store.find(articleId).orElseThrow(() -> new NotFoundException());
        Article art = new Article(title, body, tags);
        Article artupdate = store.update(art, json);
        return artupdate.toJson();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject find(@PathParam("id") Long id) {
        Article article = store.find(id).orElseThrow(() -> new NotFoundException());
        return article.toJson();
    }

    @GET
    @Path("comments")
    @RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonObject> comments() {
        return commentStore.searchByArticle(articleId)
                .stream()
                .map(v -> v.toJson())
                .collect(Collectors.toList());
    }

    @POST
    @Path("comments")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject create(JsonObject json) {
        String text = json.getString("text");
        String value = json.getString("value");
        Article article = articleStore.find(articleId).orElseThrow(() -> new NotFoundException());
        User user = userStore.find(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException());
        Comment comment = new Comment(text, article, user, Integer.parseInt(value));
        Comment comm = commentStore.create(comment);
        return comm.toJson();
    }

    @DELETE
    @RolesAllowed({"ADMIN"})
    public Response delete(@PathParam("articleId") Long id) {
        Article found = store.find(articleId).orElseThrow(() -> new NotFoundException());
        if (found == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        store.delete(articleId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

}

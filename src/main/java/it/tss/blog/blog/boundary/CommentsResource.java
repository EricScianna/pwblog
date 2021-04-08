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
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

/**
 *
 * @author scian
 */
@DenyAll
@Path("/comments")
public class CommentsResource {

    @Inject
    CommentStore commentStore;

    @Inject
    ArticleStore articleStore;

    @Inject
    UserStore userStore;

    @Inject
    @Claim(standard = Claims.sub)
    String userId;

    @RolesAllowed({"ADMIN", "USER"})
    @GET
    @Path("{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject find(@PathParam("commentId") Long id) {
        Comment comment = commentStore.find(id).orElseThrow(() -> new NotFoundException());
        return comment.toJson();
    }

    @POST
    @Path("{commentId}")
    @RolesAllowed({"ADMIN", "USER"})
    public JsonObject response(@PathParam("commentId") Long id, JsonObject json) {
        Comment commentParent = commentStore.find(id).orElseThrow(() -> new NotFoundException());
        String text = json.getString("text");
        String value = json.getString("value");
        Article article = articleStore.find(commentParent.getArticle().getId()).orElseThrow(() -> new NotFoundException());
        User user = userStore.find(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException());
        Comment comment = new Comment(text, article, user, Integer.parseInt(value));
        comment.setParent(commentParent);
        Comment comm = commentStore.create(comment);
        return comm.toJson();
    }

}

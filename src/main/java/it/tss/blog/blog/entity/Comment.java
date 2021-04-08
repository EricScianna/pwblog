/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.entity;

import it.tss.blog.blog.boundary.dto.CommentCreate;
import java.io.Serializable;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author scian
 */
@NamedQueries({
    @NamedQuery(name = Comment.FIND_ALL, query = " select e from Comment e order by e.createdOn DESC"),
    @NamedQuery(name = Comment.FIND_BY_USR, query = " select e from Comment e where e.user.id= :user_id order by e.createdOn DESC"),
    @NamedQuery(name = Comment.SEARCH, query = " select e from Comment e where e.text like :search order by e.createdOn DESC")
})

@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity implements Serializable {

    public static final String FIND_ALL = "Comment.findAll";
    public static final String FIND_BY_USR = "Comment.findByUsr";
    public static final String SEARCH = "Comment.search";

    @Column(name = "testo")
    private String text;
    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id")
    private Article article;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;
    @Column(name = "value")
    private int value;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    public Comment() {
    }

    public Comment(CommentCreate c) {
        this.text = text;
        this.value = value;
    }

    public Comment(String text, Article article, User user, int value) {
        this.text = text;
        this.article = article;
        this.user = user;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public JsonObject toJson() {
        AbstractEntity ab;
        return Json.createObjectBuilder()
                .add("fname", this.user.getFname())
                .add("article", this.article.getTitle())
                .add("text", this.text)
                .add("value", this.value)
                .build();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.text);
        hash = 29 * hash + Objects.hashCode(this.user);
        hash = 29 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (this.value != other.value) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

}

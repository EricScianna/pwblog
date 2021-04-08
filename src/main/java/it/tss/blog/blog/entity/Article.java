/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.entity;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author scian
 */
@Entity
@Table(name = "article")
public class Article extends AbstractEntity implements Serializable {

    @Column(nullable = false, unique = true)
    private String title;
    private String body;
    private String tags;
    private boolean deleted;

    public Article() {
    }

    public Article(String title, String body, String tags) {
        this.title = title;
        this.body = body;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("title", this.title)
                .add("body", this.body)
                .add("tags", this.tags)
                .add("id", super.id)
                .build();

    }
}

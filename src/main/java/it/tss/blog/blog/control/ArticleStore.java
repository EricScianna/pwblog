/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.control;

import it.tss.blog.blog.entity.Article;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author scian
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRED)
public class ArticleStore {
    
    @Inject
    CommentStore commentStore;
    
    @PersistenceContext
    EntityManager em;
    
    public Article create(Article article) {
        return em.merge(article);
    }
    
    public List<Article> search() {
        return em.createQuery("select e from Article e order by e.id ", Article.class)
                .getResultList();
    }
    
    public void delete(Long id) {
        Article found = em.find(Article.class, id);
        commentStore.searchByArticle(id).forEach(v -> commentStore.delete(v.getId()));
        em.remove(found);
    }
    
    public Optional<Article> find(Long id) {
        Article found = em.find(Article.class, id);
        return found == null ? Optional.empty() : Optional.of(found);
        
    }
    
    public Article update(Article article, JsonObject json) {
        if (json.getString("title") != null) {
            article.setTitle(json.getJsonString("title").getString());
        }
        if (json.getString("content") != null) {
            article.setBody(json.getJsonString("content").getString());
        }
        if (json.getString("tag") != null) {
            article.setTags(json.getJsonString("tag").getString());
        }
        return em.merge(article);
    }
}

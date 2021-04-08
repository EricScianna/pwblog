/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.control;

import it.tss.blog.blog.entity.Article;
import it.tss.blog.blog.entity.Comment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author scian
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRED)
public class CommentStore {

    @PersistenceContext
    EntityManager em;

    public Comment create(Comment comment) {
        return em.merge(comment);
    }

    public List<Comment> searchByArticle(Long articleId) {
        return em.createQuery("select e from Comment e where e.article.id= :articleId  order by e.createdOn ", Comment.class)
                .setParameter("articleId", articleId)
                .getResultStream()
                .collect(Collectors.toList());
    }

    public Optional<Comment> find(Long id) {
        Comment found = em.find(Comment.class, id);
        return found == null ? Optional.empty() : Optional.of(found);
    }

    public List<Comment> findByUsr(Long id) {
        return em.createNamedQuery(Comment.FIND_BY_USR, Comment.class)
                .setParameter("user_id", id)
                .getResultList();
    }

    public void delete(Long id) {
        Comment found = em.find(Comment.class, id);
        em.remove(found);
    }
}

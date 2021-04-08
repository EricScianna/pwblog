/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.control;

import it.tss.blog.blog.entity.User;
import it.tss.blog.security.control.SecurityEncoding;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author scian
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRED)
public class UserStore {

    @Inject
    @ConfigProperty(name = "maxResult", defaultValue = "10")
    int maxResult;

    @PersistenceContext
    private EntityManager em;

    UserStore users;

    public User create(User u) {
        u.setPwd(SecurityEncoding.shaHash(u.getPwd()));
        User saved = em.merge(u);
        return saved;
    }

    public List<User> search(int start, int maxResult) {
        System.out.println(start + " " + maxResult);
        return em.createQuery("select e from User e where e.bloccato=false order by e.usr", User.class)
                .setFirstResult(start)
                .setMaxResults(maxResult == 0 ? this.maxResult : maxResult)
                .getResultList();
    }

    public Optional<User> findByUserAndPwd(String usr, String pwd) {
        try {
            User found = em.createQuery("select e from User e where e.usr= :usr and e.pwd= :pwd", User.class)
                    .setParameter("usr", usr)
                    .setParameter("pwd", SecurityEncoding.shaHash(pwd))
                    .getSingleResult();
            return Optional.of(found);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<User> find(Long id) {
        User found = em.find(User.class, id);
        return found == null ? Optional.empty() : Optional.of(found);
    }

    public void delete(Long id) {
        User found = em.find(User.class, id);
        found.setBloccato(true);
    }

    public User update(User u) {
        return em.merge(u);

    }

}

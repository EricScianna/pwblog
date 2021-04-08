/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.boundary.adapters;

import it.tss.blog.blog.entity.User;
import javax.json.bind.adapter.JsonbAdapter;

/**
 *
 * @author Eric
 */
/**
 * JsonbAdapter converte da JsonObject a Role e viceversa
 */
public class RoleTypeAdapter implements JsonbAdapter<User.Role, String> {

    @Override
    public String adaptToJson(User.Role r) throws Exception {
        return r.name();
    }

    @Override
    public User.Role adaptFromJson(String json) throws Exception {
        return User.Role.valueOf(json);
    }

}

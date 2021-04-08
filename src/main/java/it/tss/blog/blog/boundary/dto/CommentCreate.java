/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.blog.blog.boundary.dto;

import javax.validation.constraints.NotEmpty;

/**
 *
 * @author scian
 */
public class CommentCreate {

    @NotEmpty
    public String text;
    @NotEmpty
    public int rating;
}

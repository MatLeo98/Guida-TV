/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.guida.tv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guida.tv.data.model.User;

/**
 *
 * @author Matteo
 */
public class UserImpl extends DataItemImpl<String> implements User {

    //private String email;
    private String password;
    private Boolean confirmed;
    private Boolean newsletter;

    public UserImpl(){

    super();
    //email = "";
    password = "";
    confirmed = false;
    newsletter = false;

    }

   /* @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }*/

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Boolean getConfirmed() {
        return confirmed;
    }

    @Override
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public Boolean getNewsletter() {
        return newsletter;
    }

    @Override
    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

}

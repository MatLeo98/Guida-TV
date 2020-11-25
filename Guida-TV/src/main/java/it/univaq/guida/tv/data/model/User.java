/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

/**
 *
 * @author Matteo
 */
public interface User {

    Boolean getConfirmed();

    String getEmail();

    Boolean getNewsletter();

    String getPassword();

    void setConfirmed(Boolean confirmed);

    void setEmail(String email);

    void setNewsletter(Boolean newsletter);

    void setPassword(String password);
    
}

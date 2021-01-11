/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.model.User;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface UserDAO {
    
    User createUser();
    
    User getUser(String userEmail) throws DataException;
    
    List<User> getUsers() throws DataException;
    
    List<User> getSubUsers() throws DataException;
    
    void storeUser(String email, String password, String URI) throws DataException;
    
    void setNewsletter(String email, Boolean scelta);
    
    //login

    public void setConfirmed(String email);
    
}

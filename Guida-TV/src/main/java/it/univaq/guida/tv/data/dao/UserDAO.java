/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.model.User;

/**
 *
 * @author Matteo
 */
public interface UserDAO {
    
    User createUser();
    
    User getUser(String userEmail) throws DataException;
    
    User storeUser(User user) throws DataException;
    
    //login
    
}

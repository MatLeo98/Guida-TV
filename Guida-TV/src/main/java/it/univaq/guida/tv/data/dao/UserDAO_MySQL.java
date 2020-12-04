/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.UserProxy;
import it.univaq.guida.tv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Matteo
 */
public class UserDAO_MySQL extends DAO implements UserDAO{
    
    private PreparedStatement UserByEmail;

    public UserDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            UserByEmail = connection.prepareStatement("SELECT * FROM user WHERE email = ?");            

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            UserByEmail.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public UserProxy createUser() {
        return new UserProxy(getDataLayer());
    }

    public UserProxy createUser(ResultSet rs) throws DataException {
        UserProxy user = createUser();
        try{
            user.setKey(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setConfirmed(rs.getBoolean("confirmed"));
            user.setNewsletter(rs.getBoolean("newsletter"));
            user.setVersion(rs.getInt("version"));
        } catch (SQLException ex){
            throw new DataException ("Unable to create user object form ResultSet",ex);
        }
       return user;
    }

    @Override
    public User getUser(String userEmail) throws DataException {
      User user = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(User.class, userEmail)) {
            user = dataLayer.getCache().get(User.class, userEmail);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                UserByEmail.setString(1, userEmail);
                try (ResultSet rs = UserByEmail.executeQuery()) {
                    if (rs.next()) {
                        user = createUser(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(User.class, user);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load article by ID", ex);
            }
        }
        return user;  
    }

    @Override
    public User storeUser(User user) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

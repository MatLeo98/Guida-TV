/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.OptimisticLockException;
import it.univaq.framework.data.proxy.UserProxy;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Matteo
 */
public class UserDAO_MySQL extends DAO implements UserDAO{
    
    private PreparedStatement UserByEmail;
    private PreparedStatement register;
    private PreparedStatement setNewsletter;
    private PreparedStatement setConfirmed;
    private PreparedStatement getSubUsers;
    private PreparedStatement allUsers;

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
            register = connection.prepareStatement("INSERT INTO user (email,password,URI) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            setNewsletter = connection.prepareStatement("UPDATE user SET newsletter = ? WHERE email = ?");
            setConfirmed = connection.prepareStatement("UPDATE user SET confirmed = 1 WHERE email = ?");
            getSubUsers = connection.prepareStatement("SELECT * FROM user WHERE newsletter = 1"); 
            allUsers = connection.prepareStatement("SELECT email FROM user");

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
            register.close();
            setNewsletter.close();
            setConfirmed.close();
            getSubUsers.close();
            allUsers.close();

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
            user.setUri(rs.getString("URI"));
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
    public List<User> getUsers() throws DataException {
        List<User> users = new ArrayList();
        
            try (ResultSet rs = allUsers.executeQuery()) {
            while (rs.next()) {
                users.add((User) getUser(rs.getString("email")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load users", ex);
        }
        return users;
    }

    @Override
    public void storeUser(User user) throws DataException {
       try {
           
                register.setString(1, user.getKey());
                register.setString(2, user.getPassword());
                register.setString(3, user.getUri());
                
                if (register.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = register.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            String key = keys.getString(1);
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                            user.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(User.class, user);
                        }
                    }
                }
            

//            //se possibile, restituiamo l'oggetto appena inserito RICARICATO
//            //dal database tramite le API del modello. In tal
//            //modo terremo conto di ogni modifica apportata
//            //durante la fase di inserimento
//            //if possible, we return the just-inserted object RELOADED from the
//            //database through our API. In this way, the resulting
//            //object will ambed any data correction performed by
//            //the DBMS
//            if (key > 0) {
//                article.copyFrom(getArticle(key));
//            }
            //se abbiamo un proxy, resettiamo il suo attributo dirty
            //if we have a proxy, reset its dirty attribute
            
            if (user instanceof DataItemProxy) {
                ((DataItemProxy) user).setModified(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setNewsletter(String email, Boolean bln) {
        try {
            setNewsletter.setBoolean(1, bln);
            setNewsletter.setString(2, email);
            if (setNewsletter.executeUpdate() == 0) {
                User user = getUser(email);
                    throw new OptimisticLockException(user);
                }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @Override
    public void setConfirmed(String email) {
        try {
           
            setConfirmed.setString(1, email);
            if (setConfirmed.executeUpdate() == 0) {
                User user = getUser(email);
                    throw new OptimisticLockException(user);
                }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<User> getSubUsers() throws DataException {
        List<User> users = new ArrayList();
        
                       
            try (ResultSet rs = getSubUsers.executeQuery()) {
                while (rs.next()) {
                     users.add((User) getUser(rs.getString("email")));
            }
        } catch (SQLException ex) {
            try {
                throw new DataException("Unable to load users", ex);
            } catch (DataException ex1) {
                Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return users;
    }
}
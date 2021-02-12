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
import it.univaq.framework.data.proxy.SavedSearchesProxy;
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class SavedSearchesDAO_MySQL extends DAO implements SavedSearchesDAO{
    
    private PreparedStatement s;
    private PreparedStatement storeSearches; 
    private PreparedStatement SSByKey;
    private PreparedStatement savedSByUser;
     private PreparedStatement last;
      private PreparedStatement dayMail;
      private PreparedStatement delSS;
      private PreparedStatement delSSProg;
    

    public SavedSearchesDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            s = connection.prepareStatement("SELECT * FROM savedsearches");   
            savedSByUser = connection.prepareStatement("SELECT * FROM savedsearches WHERE emailUser = ?");  
            SSByKey = connection.prepareStatement("SELECT * FROM savedsearches WHERE idSavedS = ?");
            last = connection.prepareStatement("SELECT * FROM savedsearches WHERE emailUser = ? ORDER BY idSavedS DESC ");
            storeSearches = connection.prepareStatement("INSERT INTO savedsearches (title, genre, minStartHour, maxStartHour, channel, startDate, endDate, emailUser) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dayMail = connection.prepareStatement("UPDATE savedsearches SET sendEmail = ? WHERE idSavedS = ?");
            delSS = connection.prepareStatement("DELETE FROM savedsearches WHERE idSavedS = ?");
            delSSProg = connection.prepareStatement("DELETE FROM favouriteprogram WHERE savedSearchId = ?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            s.close();
            savedSByUser.close();
            storeSearches.close();
            SSByKey.close();
            last.close();
            dayMail.close();
            delSSProg.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public SavedSearchesProxy createSavedSearch() {
        return new SavedSearchesProxy(getDataLayer());
    }
    
    public SavedSearchesProxy createSavedSearch(ResultSet rs) throws DataException {
        SavedSearchesProxy savedSearch = createSavedSearch();
        try{
            savedSearch.setKey(rs.getInt("idSavedS"));
            savedSearch.setTitle(rs.getString("title"));
            savedSearch.setGenre(Genre.valueOf(rs.getString("genre")));
            savedSearch.setMaxStartHour(rs.getTime("maxStartHour").toLocalTime());
            savedSearch.setMinStartHour(rs.getTime("minStartHour").toLocalTime());
            savedSearch.setChannel(rs.getString("channel"));
            savedSearch.setStartDate(rs.getDate("startDate"));
            savedSearch.setEndDate(rs.getDate("endDate"));
            savedSearch.setSendEmail(rs.getBoolean("sendEmail"));
            savedSearch.setUserKey(rs.getString("emailUser"));
        } catch (SQLException ex){
            throw new DataException ("Unable to create savedSearch object form ResultSet",ex);
        }
        return savedSearch;
    }
    
    @Override
    public SavedSearches getSavedSearch(int key) throws DataException{
        SavedSearches ss = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(SavedSearches.class, key)) {
            ss = dataLayer.getCache().get(SavedSearches.class, key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                SSByKey.setInt(1, key);
                try (ResultSet rs = SSByKey.executeQuery()) {
                    if (rs.next()) {
                        ss = createSavedSearch(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(SavedSearches.class, ss);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load SavedSearch by ID", ex);
            }
        }
        return ss;  
    }

    @Override
    public List<SavedSearches> getSavedSearches(User user) throws DataException {
        List<SavedSearches> searches = new ArrayList();
        
            
        
            try {
                savedSByUser.setString(1,user.getKey());
                ResultSet rs = savedSByUser.executeQuery();
                
            while (rs.next()) {
                searches.add((SavedSearches) getSavedSearch(rs.getInt("idSavedS")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load saved searches", ex);
        }
        return searches;
    }

    @Override
    public void deleteSavedSearch(int key) {
        try {
            
            delSS.setInt(1,key);
            delSS.executeUpdate();
            delSSProg.setInt(1, key);
            delSSProg.executeUpdate();
            
                
        } catch (SQLException ex) {
            Logger.getLogger(SavedSearchesDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public SavedSearches storeSavedSearches(String titolo, String genere, String channel, String dateMin, String dateMax, String minTime, String maxTime, String email) throws DataException {
        SavedSearches ss = null;
        try {
            
           
            storeSearches.setString(1,titolo);
            storeSearches.setString(2,genere);
            storeSearches.setTime(3,java.sql.Time.valueOf(minTime+":00"));
            storeSearches.setTime(4,java.sql.Time.valueOf(maxTime+":00"));
            storeSearches.setString(5,channel);
            storeSearches.setDate(6,java.sql.Date.valueOf(dateMin)); 
            storeSearches.setDate(7,java.sql.Date.valueOf(dateMax));
            storeSearches.setString(8,email);
            
            
            if (storeSearches.executeUpdate() == 1) {
                 try (ResultSet keys = storeSearches.getGeneratedKeys()) {
                     if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                            ss = getSavedSearch(key);
                            ss.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(SavedSearches.class, ss);
                        }
                 }
            }
            
            
            if (ss instanceof DataItemProxy) {
                ((DataItemProxy) ss).setModified(false);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SavedSearchesDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ss;
        
    }

    /*@Override
    public SavedSearches getLast(String email) throws DataException{
        SavedSearches ss = null;
        try {
                last.setString(1,email);
                try (ResultSet rs = last.executeQuery()) {
                    if (rs.next()) {
                        
                       ss = createSavedSearch(rs);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load SavedSearch by ID", ex);
            }
        return ss;
    }*/

    @Override
    public void setDayMail(int key, boolean email) throws DataException{
       try {
            dayMail.setBoolean(1, email);
            dayMail.setInt(2, key);
            if (dayMail.executeUpdate() == 0) {
                SavedSearches ss = getSavedSearch(key);
                    throw new OptimisticLockException(ss);
                }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.SavedSearchesProxy;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.SavedSearches;
import it.univaq.guida.tv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Matteo
 */
public class SavedSearchesDAO_MySQL extends DAO implements SavedSearchesDAO{
    
    private PreparedStatement s;

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
            savedSearch.setMaxStartHour(rs.getDate("maxStartHour"));
            savedSearch.setMinStartHour(rs.getDate("maxEndHour"));
            savedSearch.setChannel(rs.getInt("channel"));
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
    public List<SavedSearches> getSavedSearches(User user) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteSavedSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

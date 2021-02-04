/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.User;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface SavedSearchesDAO {
    
    SavedSearches createSavedSearch();
    
    SavedSearches getSavedSearch(int key) throws DataException;
    
    List<SavedSearches> getSavedSearches (User user) throws DataException;
    
    SavedSearches storeSavedSearches(String titolo, String genere, String channel, String dateMin, String dateMax, String minTime, String maxTime, String email) throws DataException;
    
    void deleteSavedSearch(int key);

    //public SavedSearches getLast(String email) throws DataException;
    
    public void setDayMail(int key, boolean email) throws DataException;
}

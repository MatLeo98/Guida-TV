/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.guida.tv.data.model.SavedSearches;
import it.univaq.guida.tv.data.model.User;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface SavedSearchesDAO {
    
    SavedSearches createSavedSearch();
    
    List<SavedSearches> getSavedSearches (User user);
    
    void deleteSavedSearch();
    
}

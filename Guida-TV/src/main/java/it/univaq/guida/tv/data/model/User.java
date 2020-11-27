/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface User extends DataItem<String>{

    Boolean getConfirmed();

    //String getEmail();

    Boolean getNewsletter();

    String getPassword();

    void setConfirmed(Boolean confirmed);
    
    public List<SavedSearches> getSavedSearches();
    
    public List<FavouriteChannel> getFavouriteChannels();
    
    public List<FavouriteProgram> getFavouriteProgram();

    //void setEmail(String email);

    void setNewsletter(Boolean newsletter);

    void setPassword(String password);
    
    public void setSavedSearches(List<SavedSearches> savedSearches);
    
    public void setFavouriteChannels(List<FavouriteChannel> favouriteChannels);
    
    public void setFavouriteProgram(List<FavouriteProgram> favouriteProgram);
    
    public void addSavedSearch(SavedSearches savedSearch);
            
    public void addFavouriteChannel(FavouriteChannel favouriteChannel);
    
    public void addFavouriteProgram(FavouriteProgram favouriteProgram);
    
}

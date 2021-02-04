/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.User;
import java.util.List;
import it.univaq.guidatv.data.model.FavouriteChannel;

/**
 *
 * @author Matteo
 */
public class UserImpl extends DataItemImpl<String> implements User {

    

    //private String email;
    private String password;
    private Boolean confirmed;
    private Boolean newsletter;
    private String uri;
    List<SavedSearches> savedSearches;
    List<FavouriteChannel> favouriteChannels;
    List<FavouriteProgram> favouritePrograms;

    public UserImpl(){

        super();
        //email = "";
        password = "";
        confirmed = false;
        newsletter = false;
        uri = "";

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public Boolean getNewsletter() {
        return newsletter;
    }

    @Override
    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }
    
    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    @Override
    public void addSavedSearch(SavedSearches savedSearch){
        this.savedSearches.add(savedSearch);
    }

    @Override
    public List<SavedSearches> getSavedSearches() {
        return savedSearches;
    }

    @Override
    public void setSavedSearches(List<SavedSearches> savedSearches) {
        this.savedSearches = savedSearches;
    }
    
    @Override
    public void addFavouriteChannel(FavouriteChannel favouriteChannel){
        this.favouriteChannels.add(favouriteChannel);
    }

    @Override
    public List<FavouriteChannel> getFavouriteChannels() {
        return favouriteChannels;
    }

    @Override
    public void setFavouriteChannels(List<FavouriteChannel> favouriteChannels) {
        this.favouriteChannels = favouriteChannels;
    }
    
    @Override
    public void addFavouriteProgram(FavouriteProgram favouriteProgram){
        this.favouritePrograms.add(favouriteProgram);
    }

    @Override
    public List<FavouriteProgram> getFavouriteProgram() {
        return favouritePrograms;
    }

    @Override
    public void setFavouriteProgram(List<FavouriteProgram> favouriteProgram) {
        this.favouritePrograms = favouriteProgram;
    }

}

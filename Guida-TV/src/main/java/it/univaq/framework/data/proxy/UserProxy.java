/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guidatv.data.impl.UserImpl;
import it.univaq.guidatv.data.model.FavouriteChannel;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.SavedSearches;
import java.util.List;

/**
 *
 * @author Matteo
 */
public class UserProxy extends UserImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    
    public UserProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setFavouriteProgram(List<FavouriteProgram> favouriteProgram) {
        super.setFavouriteProgram(favouriteProgram);
        this.modified = true;
    }

    @Override
    public void addFavouriteProgram(FavouriteProgram favouriteProgram) {
        super.addFavouriteProgram(favouriteProgram);
        this.modified = true;
    }

    @Override
    public void setFavouriteChannels(List<FavouriteChannel> favouriteChannels) {
        super.setFavouriteChannels(favouriteChannels);
        this.modified = true;
    }

    @Override
    public void addFavouriteChannel(FavouriteChannel favouriteChannel) {
        super.addFavouriteChannel(favouriteChannel);
        this.modified = true;
    }

    @Override
    public void setSavedSearches(List<SavedSearches> savedSearches) {
        super.setSavedSearches(savedSearches);
        this.modified = true;
    }

    @Override
    public void addSavedSearch(SavedSearches savedSearch) {
        super.addSavedSearch(savedSearch);
        this.modified = true;
    }

    @Override
    public void setNewsletter(Boolean newsletter) {
        super.setNewsletter(newsletter);
        this.modified = true;
    }

    @Override
    public void setConfirmed(Boolean confirmed) {
        super.setConfirmed(confirmed);
        this.modified = true;
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        this.modified = true;
    }
    
    @Override
    public void setUri(String uri) {
        super.setUri(uri);
        this.modified = true;
    }
    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
    
}

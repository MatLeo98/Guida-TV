/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guida.tv.data.dao.EpisodeDAO;
import it.univaq.guida.tv.data.dao.ProgramDAO;
import it.univaq.guida.tv.data.dao.SavedSearchesDAO;
import it.univaq.guida.tv.data.dao.UserDAO;
import it.univaq.guida.tv.data.impl.FavouriteProgramImpl;
import it.univaq.guida.tv.data.impl.ScheduleImpl;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.SavedSearches;
import it.univaq.guida.tv.data.model.User;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class FavouriteProgramProxy extends FavouriteProgramImpl implements DataItemProxy{
    
    protected boolean modified;
    protected int program_key = 0;
     protected int savedS_key = 0;
    protected String user_key = ""; //email
    
    protected DataLayer dataLayer;
    
    
    public FavouriteProgramProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
        this.program_key = 0;
        this.savedS_key = 0;
        this.user_key = "";
        
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }
    
    @Override
    public void setUser(User user) {
        super.setUser(user); 
        this.modified = true;
        
    }

    @Override
    public User getUser() {
        if (super.getUser() == null && !(user_key.equals(""))) {
            try {
                super.setUser(((UserDAO) dataLayer.getDAO(User.class)).getUser(user_key));
            } catch (DataException ex) {
                Logger.getLogger(FavouriteProgramProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getUser();
    }

    @Override
    public void setProgram(Program program) {
        super.setProgram(program); 
        this.modified = true;
    }

    @Override
    public Program getProgram() {
        if (super.getProgram() == null && program_key > 0) {
            try {
                super.setProgram(((ProgramDAO) dataLayer.getDAO(Program.class)).getProgram(program_key));
            } catch (DataException ex) {
                Logger.getLogger(FavouriteProgramProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getProgram();
    }

    @Override
    public void setSavedSearch(SavedSearches ss) {
        super.setSavedSearch(ss); 
        this.modified = true;
    }
    
    @Override
    public SavedSearches getSavedSearch() {
        if (super.getSavedSearch() == null && savedS_key > 0) {
            try {
                super.setSavedSearch(((SavedSearchesDAO) dataLayer.getDAO(SavedSearches.class)).getSavedSearch(savedS_key));
            } catch (DataException ex) {
                Logger.getLogger(FavouriteProgramProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getSavedSearch();
    }
    
    
    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
    
    public void setUserKey(String user_key) {
        this.user_key = user_key;
        super.setUser(null);
    }
    
    public void setProgramKey(int program_key) {
        this.program_key = program_key;
        super.setProgram(null);
    }

    public void setSavedSearchKey(int savedS_key) {
        this.savedS_key = savedS_key;
        super.setSavedSearch(null);
    }
    
}

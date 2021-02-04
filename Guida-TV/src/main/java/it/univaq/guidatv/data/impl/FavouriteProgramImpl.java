/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;


import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.User;

/**
 *
 * @author giorg
 */
public class FavouriteProgramImpl extends DataItemImpl<Integer> implements FavouriteProgram {

    private SavedSearches ss;
    
    private Program program;
    private User user;
    
    public FavouriteProgramImpl(){
        super();
        ss = null;
        program = null;
        user = null;
    } 
    
    @Override
    public SavedSearches getSavedSearch() {
        return ss;
    }

    @Override
    public void setSavedSearch(SavedSearches ss) {
        this.ss = ss;
    }

    @Override
    public Program getProgram() {
        return program;
    }

    @Override
    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    

    
    
    
    
    
    
}

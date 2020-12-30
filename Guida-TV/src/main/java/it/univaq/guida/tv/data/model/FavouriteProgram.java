/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guida.tv.data.impl.ScheduleImpl;
import it.univaq.guida.tv.data.model.Program;

/**
 *
 * @author giorg
 */
public interface FavouriteProgram extends DataItem<Integer>{

    Program getProgram();

    SavedSearches getSavedSearch();

    User getUser();

    void setProgram(Program program);

    void setSavedSearch(SavedSearches ss);

    void setUser(User user);
    
}

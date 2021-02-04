/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.User;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface FavouriteProgramDAO {
    
    FavouriteProgram createFavouriteProgram();
    
    List<FavouriteProgram> getFavouritePrograms(User user) throws DataException;
    
    public FavouriteProgram getFavouriteProgram(int key) throws DataException;
    
    void storeFavPrograms (List<Program> programs, String email, Integer SavedSId) throws DataException;
    
    void deleteFavouriteProgram(int key);
    
}

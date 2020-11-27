/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.User;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface FavouriteProgramDAO {
    
    FavouriteProgram createFavouriteProgram();
    
    List<FavouriteProgram> getFavouritePrograms(User user) throws DataException;
    
    void deleteFavouriteProgram(FavouriteProgram favProgram);
    
}

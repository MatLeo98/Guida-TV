/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guidatv.data.model.User;
import java.util.List;
import it.univaq.guidatv.data.model.FavouriteChannel;

/**
 *
 * @author Matteo
 */
public interface FavouriteChannelDAO {
    
    FavouriteChannel createFavouriteChannel();
    
    FavouriteChannel getFavouriteChannel(int key) throws DataException;
    
    List<FavouriteChannel> getFavouriteChannels(User user) throws DataException;
    
    void deleteFavouriteChannel(int key);
    
    void storeFavChannel(String[] channels, String[] timeslots, String email);

   
    
}

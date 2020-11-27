/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.model.User;
import java.util.List;
import it.univaq.guida.tv.data.model.FavouriteChannel;

/**
 *
 * @author Matteo
 */
public interface FavouriteChannelDAO {
    
    FavouriteChannel createFavouriteChannel();
    
    List<FavouriteChannel> getFavouriteChannels(User user) throws DataException;
    
    void deleteFavouriteChannel();
    
}

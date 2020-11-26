/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.guida.tv.data.model.FavouriteChannels;
import it.univaq.guida.tv.data.model.User;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface FavouriteChannelsDAO {
    
    FavouriteChannels createFavouriteChannel();
    
    List<FavouriteChannels> getFavouriteChannels(User user);
    
    void deleteFavouriteChannel();
    
}

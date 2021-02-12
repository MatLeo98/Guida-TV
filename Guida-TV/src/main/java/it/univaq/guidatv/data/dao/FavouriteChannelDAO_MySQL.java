/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.FavouriteChannelProxy;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.FavouriteChannel;
import it.univaq.guidatv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class FavouriteChannelDAO_MySQL extends DAO implements FavouriteChannelDAO{
    
    private PreparedStatement favChannelById;
    private PreparedStatement storeFavChannels;
     private PreparedStatement favChannelsByUser;
     private PreparedStatement delFavCh;

    public FavouriteChannelDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            favChannelById = connection.prepareStatement("SELECT * FROM favouritechannel WHERE idFavChannel = ?");
            storeFavChannels = connection.prepareStatement("INSERT INTO favouritechannel (timeSlot,emailUser,channelId) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            favChannelsByUser = connection.prepareStatement("SELECT * FROM favouritechannel WHERE emailUser = ? ORDER BY channelId");
            delFavCh = connection.prepareStatement("DELETE FROM favouritechannel WHERE idFavChannel = ?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            
            favChannelById.close();
            storeFavChannels.close();
            favChannelsByUser.close();
            delFavCh.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public FavouriteChannelProxy createFavouriteChannel() {
        return new FavouriteChannelProxy(getDataLayer());
    }
    
    public FavouriteChannelProxy createFavouriteChannel(ResultSet rs) throws DataException{
        FavouriteChannelProxy favChannel = createFavouriteChannel();
        try {
            favChannel.setKey(rs.getInt("idFavChannel"));
            favChannel.setTimeSlot(TimeSlot.valueOf(rs.getString("timeSlot")));
            favChannel.setUserKey(rs.getString("emailUser"));
            favChannel.setChannelKey(rs.getInt("channelId"));
            favChannel.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create FavouriteChannel object form ResultSet", ex);
        }
        return favChannel;
    }
    
    @Override
    public FavouriteChannel getFavouriteChannel(int key) throws DataException {
       FavouriteChannel favChannel = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(FavouriteChannel.class, key)) {
            favChannel = dataLayer.getCache().get(FavouriteChannel.class, key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                favChannelById.setInt(1, key);
                try (ResultSet rs = favChannelById.executeQuery()) {
                    if (rs.next()) {
                        favChannel = createFavouriteChannel(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(FavouriteChannel.class, favChannel);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Favourite Channel by ID", ex);
            }
        }
        return favChannel;  
    }

    @Override
    public List<FavouriteChannel> getFavouriteChannels(User user) throws DataException {
        List<FavouriteChannel> favC = new ArrayList();
        try {
            favChannelsByUser.setString(1,user.getKey());
            
        
            try (ResultSet rs = favChannelsByUser.executeQuery()) {
            while (rs.next()) {
                favC.add((FavouriteChannel) getFavouriteChannel(rs.getInt("idFavChannel")));
            }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load favourite channels", ex);
        }
        
        
        return favC;
    }

    @Override
    public void deleteFavouriteChannel(int key) {
        try {
            
            delFavCh.setInt(1,key);
            delFavCh.executeUpdate();
                
        } catch (SQLException ex) {
            Logger.getLogger(FavouriteChannelDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void storeFavChannel(String[] channels, String[] timeslots, String email) {
        ciclo:
        for(String c : channels){
            for(String ts : timeslots){
                try {
                    if(c.equals("default"))
                        break ciclo;
                    storeFavChannels.setString(1,ts);
                    storeFavChannels.setString(2,email);
                    storeFavChannels.setInt(3,Integer.parseInt(c));
                    
                    
                    if (storeFavChannels.executeUpdate() == 1) {
                    
                    try (ResultSet keys = storeFavChannels.getGeneratedKeys()) {
                        
                        if (keys.next()) {
                            
                            int key = keys.getInt(1);
                            
                            FavouriteChannel favChannel = getFavouriteChannel(key);
                            favChannel.setKey(key);
                            
                            dataLayer.getCache().add(FavouriteChannel.class, favChannel);
                        }
                    }   catch (DataException ex) {
                            Logger.getLogger(FavouriteChannelDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    
                } catch (SQLException ex) {
                    Logger.getLogger(FavouriteChannelDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
    }

    
    
    
    
}

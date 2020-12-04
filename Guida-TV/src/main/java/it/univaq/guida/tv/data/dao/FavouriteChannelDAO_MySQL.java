/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.FavouriteChannelProxy;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.FavouriteChannel;
import it.univaq.guida.tv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Matteo
 */
public class FavouriteChannelDAO_MySQL extends DAO implements FavouriteChannelDAO{
    
    private PreparedStatement favChannelByID;

    public FavouriteChannelDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            favChannelByID = connection.prepareStatement("SELECT * FROM favouritechannel WHERE idFavChannel = ?");
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            
            favChannelByID.close();


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
            favChannel.setKey(rs.getInt("idFavProgram"));
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
    public List<FavouriteChannel> getFavouriteChannels(User user) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteFavouriteChannel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.ChannelProxy;
import it.univaq.guida.tv.data.model.Channel;
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
 * @author giorg
 */
public class ChannelDAO_MySQL extends DAO implements ChannelDAO{
    
    private PreparedStatement allChannels;
    private PreparedStatement channelByID;
    private PreparedStatement insertChannel;

    public ChannelDAO_MySQL(DataLayer d) {
        super(d);
    }
    
     @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            allChannels = connection.prepareStatement("SELECT idChannel FROM channel");
            channelByID = connection.prepareStatement("SELECT * FROM channel WHERE idChannel = ?");
            insertChannel = connection.prepareStatement("INSERT INTO channel (idChannel,name) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            channelByID.close();
            allChannels.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public ChannelProxy createChannel() {
        return new ChannelProxy(getDataLayer());
    }
    
    public ChannelProxy createChannel(ResultSet rs) throws DataException{
            ChannelProxy channel = createChannel();
        try {
            channel.setKey(rs.getInt("idChannel"));
            channel.setName(rs.getString("name"));
            channel.setImageKey(rs.getInt("imageId"));
            channel.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return channel;
    }

    @Override
    public Channel getChannel(int channelId) throws DataException {
       Channel channel = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Channel.class, channelId)) {
            channel = dataLayer.getCache().get(Channel.class, channelId);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                channelByID.setInt(1, channelId);
                try (ResultSet rs = channelByID.executeQuery()) {
                    if (rs.next()) {
                        channel = createChannel(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Channel.class, channel);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load article by ID", ex);
            }
        }
        return channel;
    }

    @Override
    public List<Channel> getChannels() throws DataException {
        List<Channel> channels = new ArrayList();
        
            try (ResultSet rs = allChannels.executeQuery()) {
            while (rs.next()) {
                channels.add((Channel) getChannel(rs.getInt("idChannel")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load channels", ex);
        }
        return channels;
    }

    @Override
    public void storeChannel(Integer num, String channel) throws DataException {
        try {
            insertChannel.setInt(1, num);
            insertChannel.setString(2, channel);
            if (insertChannel.executeUpdate() == 1) {
                    
                    try (ResultSet keys = insertChannel.getGeneratedKeys()) {
                        
                        if (keys.next()) {
                            
                            int key = keys.getInt(1);
                            
                            Channel c = getChannel(num);
                            c.setKey(key);
                            
                            dataLayer.getCache().add(Channel.class, c);
                        }
                    }
                }
            
            Channel c = getChannel(num);
            if (c instanceof DataItemProxy) {
                ((DataItemProxy) c).setModified(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChannelDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    @Override
    public void deleteChannel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

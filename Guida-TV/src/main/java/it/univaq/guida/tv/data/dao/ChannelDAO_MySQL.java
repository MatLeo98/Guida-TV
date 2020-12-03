/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.ChannelProxy;
import it.univaq.guida.tv.data.impl.ChannelImpl;
import it.univaq.guida.tv.data.impl.EpisodeImpl;
import it.univaq.guida.tv.data.impl.ImageImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Image;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class ChannelDAO_MySQL extends DAO implements ChannelDAO{
    
    private PreparedStatement s;
    private PreparedStatement channelByID;

    public ChannelDAO_MySQL(DataLayer d) {
        super(d);
    }
    
     @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            s = connection.prepareStatement("SELECT * FROM channel");
            channelByID = connection.prepareStatement("SELECT * FROM channel WHERE idChannel = ?");
            

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
            s.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public ChannelProxy createChannel() {
return new ChannelProxy(getDataLayer());
    }
    
    @Override
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
        List<Channel> result = new ArrayList();

        try {
            //sArticlesByIssue.setInt(1, issue.getKey());            
            try (ResultSet rs = s.executeQuery()) {
                while (rs.next()) {
                     Channel candidatura = new ChannelImpl();
					candidatura.setKey(rs.getInt("id"));
					candidatura.setName(rs.getString("name"));
                                        Image image = new ImageImpl();
					candidatura.setImage(image);
					candidatura.setVersion(1);
					
					
            result.add(candidatura);
                    //result.add((Channel) rs);
                }
            }
        } catch (SQLException ex) {
            try {
                throw new DataException("Unable to load articles by issue", ex);
            } catch (DataException ex1) {
                Logger.getLogger(ChannelDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return result;
    }

    @Override
    public void storeChannel(Channel channel) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteChannel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

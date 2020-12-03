/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.proxy.ChannelProxy;
import it.univaq.guida.tv.data.model.Channel;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface ChannelDAO {
    
    Channel createChannel();
    
    public ChannelProxy createChannel(ResultSet rs) throws DataException;
    
    Channel getChannel (int num) throws DataException;
    
    List<Channel> getChannels() throws DataException;
    
    void storeChannel (Channel channel) throws DataException;
    
    void deleteChannel();
    
}

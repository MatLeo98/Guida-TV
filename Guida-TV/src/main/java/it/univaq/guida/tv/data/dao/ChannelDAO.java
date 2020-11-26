/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.guida.tv.data.model.Channel;
import java.util.List;

/**
 *
 * @author Matteo
 */
public interface ChannelDAO {
    
    Channel createChannel();
    
    Channel getChannel (int num);
    
    List<Channel> getChannels();
    
    void storeChannel (Channel channel);
    
    void deleteChannel();
    
}

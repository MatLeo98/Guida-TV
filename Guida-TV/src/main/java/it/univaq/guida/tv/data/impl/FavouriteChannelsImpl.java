/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.FavouriteChannels;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.User;

/**
 *
 * @author Matteo
 */
public class FavouriteChannelsImpl extends DataItemImpl<Integer> implements FavouriteChannels {
    
    private TimeSlot timeSlot;
    private User user;
    private Channel channel;

    public FavouriteChannelsImpl() {
        super();
        this.timeSlot = null;
        this.user = null;
        this.channel = null;
   
    }

    @Override
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    
}

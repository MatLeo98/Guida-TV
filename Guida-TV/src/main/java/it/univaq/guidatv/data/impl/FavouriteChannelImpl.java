/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.User;
import it.univaq.guidatv.data.model.FavouriteChannel;

/**
 *
 * @author Matteo
 */
public class FavouriteChannelImpl extends DataItemImpl<Integer> implements FavouriteChannel {
    
    private TimeSlot timeSlot;
    private User user;
    private Channel channel;

    public FavouriteChannelImpl() {
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

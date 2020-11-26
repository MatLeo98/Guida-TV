/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.User;

/**
 *
 * @author Matteo
 */
public interface FavouriteChannels extends DataItem<Integer>{

    Channel getChannel();

    TimeSlot getTimeSlot();

    User getUser();

    void setChannel(Channel channel);

    void setTimeSlot(TimeSlot timeSlot);

    void setUser(User user);
    
}

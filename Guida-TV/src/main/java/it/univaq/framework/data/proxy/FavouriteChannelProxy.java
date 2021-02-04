/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guidatv.data.dao.ChannelDAO;
import it.univaq.guidatv.data.dao.UserDAO;
import it.univaq.guidatv.data.impl.FavouriteChannelImpl;
import it.univaq.guidatv.data.impl.ScheduleImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.User;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class FavouriteChannelProxy extends FavouriteChannelImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected int channel_key = 0;
    protected String user_key = "";
    
    public FavouriteChannelProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.channel_key = 0;
        this.user_key = "";
    }

    @Override
    public void setChannel(Channel channel) {
        super.setChannel(channel);
        this.modified = true;
    }

    @Override
    public Channel getChannel() {
        if (super.getChannel() == null && channel_key > 0) {
            try {
                super.setChannel(((ChannelDAO) dataLayer.getDAO(Channel.class)).getChannel(channel_key));
            } catch (DataException ex) {
                Logger.getLogger(FavouriteChannelProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getChannel();
    }

    @Override
    public void setUser(User user) {
        super.setUser(user);
        this.modified = true;
    }

    @Override
    public User getUser() {
        if (super.getUser() == null && !(user_key.equals(""))) {
            try {
                super.setUser(((UserDAO) dataLayer.getDAO(User.class)).getUser(user_key));
            } catch (DataException ex) {
                Logger.getLogger(FavouriteChannelProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getUser();
    }

    @Override
    public void setTimeSlot(ScheduleImpl.TimeSlot timeSlot) {
        super.setTimeSlot(timeSlot);
        this.modified = true;
    }
    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
    
    public void setUserKey(String user_key) {
        this.user_key = user_key;
        super.setUser(null);
    }
    
    public void setChannelKey(int channel_key) {
        this.channel_key = channel_key;
        super.setChannel(null);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guida.tv.data.dao.UserDAO;
import it.univaq.guida.tv.data.impl.SavedSearchesImpl;
import it.univaq.guida.tv.data.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class SavedSearchesProxy extends SavedSearchesImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected String user_key = "";
    
    public SavedSearchesProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.user_key = "";
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
                Logger.getLogger(SavedSearchesProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getUser();
    }

    @Override
    public void setSendEmail(Boolean sendEmail) {
        super.setSendEmail(sendEmail);
        this.modified = true;
    }

    @Override
    public void setEndDate(LocalDate endDate) {
        super.setEndDate(endDate);
        this.modified = true;
    }

    @Override
    public void setStartDate(LocalDate startDate) {
        super.setStartDate(startDate);
        this.modified = true;
    }

    @Override
    public void setChannel(int channel) {
        super.setChannel(channel);
        this.modified = true;
    }

    @Override
    public void setMaxEndHour(LocalDateTime maxEndHour) {
        super.setMaxEndHour(maxEndHour);
        this.modified = true;
    }

    @Override
    public void setMaxStartHour(LocalDateTime maxStartHour) {
        super.setMaxStartHour(maxStartHour);
        this.modified = true;
    }

    @Override
    public void setGenre(String genre) {
        super.setGenre(genre);
        this.modified = true;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
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
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.SavedSearches;
import it.univaq.guida.tv.data.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Matteo
 */
public class SavedSearchesImpl extends DataItemImpl<Integer> implements SavedSearches {
    
    private String title;
    private Genre genre;
    private Date maxStartHour;
    private Date maxEndHour;
    private int channel;
    private Date startDate;
    private Date endDate;
    private Boolean sendEmail;
    private User user;
    
    public SavedSearchesImpl(){
    
        super();
        title = "";
        genre = null;
        maxStartHour = null;
        maxEndHour = null;
        channel = 0;
        startDate = null;
        endDate = null;
        sendEmail = false;
        user = null;
        
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Genre getGenre() {
        return genre;
    }

    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public Date getMaxStartHour() {
        return maxStartHour;
    }

    @Override
    public void setMaxStartHour(Date maxStartHour) {
        this.maxStartHour = maxStartHour;
    }

    @Override
    public Date getMaxEndHour() {
        return maxEndHour;
    }

    @Override
    public void setMaxEndHour(Date maxEndHour) {
        this.maxEndHour = maxEndHour;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Boolean getSendEmail() {
        return sendEmail;
    }

    @Override
    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
}

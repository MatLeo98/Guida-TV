/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author Matteo
 */
public class SavedSearchesImpl extends DataItemImpl<Integer> implements SavedSearches {
    
    private String title;
    private Genre genre;
    private LocalTime maxStartHour;
    private LocalTime minStartHour;
    private String channel;
    private Date startDate;
    private Date endDate;
    private Boolean sendEmail;
    private User user;
    
    public SavedSearchesImpl(){
    
        super();
        title = "";
        genre = null;
        maxStartHour = null;
        minStartHour = null;
        channel = null;
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
    public LocalTime getMaxStartHour() {
        return maxStartHour;
    }

    @Override
    public void setMaxStartHour(LocalTime maxStartHour) {
        this.maxStartHour = maxStartHour;
    }

    @Override
    public LocalTime getMinStartHour() {
        return minStartHour;
    }

    @Override
    public void setMinStartHour(LocalTime minStartHour) {
        this.minStartHour = minStartHour;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String channel) {
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

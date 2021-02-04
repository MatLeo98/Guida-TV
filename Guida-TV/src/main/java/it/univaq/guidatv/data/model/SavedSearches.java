/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author Matteo
 */
public interface SavedSearches extends DataItem<Integer> {

    String getChannel();

    Date getEndDate();

    Genre getGenre();

    LocalTime getMinStartHour();

    LocalTime getMaxStartHour();

    Boolean getSendEmail();

    Date getStartDate();

    String getTitle();
    
    User getUser();

    void setChannel(String channel);

    void setEndDate(Date endDate);

    void setGenre(Genre genre);

    void setMinStartHour(LocalTime MinStartHour);

    void setMaxStartHour(LocalTime maxStartHour);

    void setSendEmail(Boolean sendEmail);

    void setStartDate(Date startDate);

    void setTitle(String title);
    
    void setUser(User user);
    
}

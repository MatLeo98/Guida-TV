/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Matteo
 */
public interface SavedSearches extends DataItem<Integer> {

    int getChannel();

    Date getEndDate();

    Genre getGenre();

    Date getMinStartHour();

    Date getMaxStartHour();

    Boolean getSendEmail();

    Date getStartDate();

    String getTitle();
    
    User getUser();

    void setChannel(int channel);

    void setEndDate(Date endDate);

    void setGenre(Genre genre);

    void setMinStartHour(Date MinStartHour);

    void setMaxStartHour(Date maxStartHour);

    void setSendEmail(Boolean sendEmail);

    void setStartDate(Date startDate);

    void setTitle(String title);
    
    void setUser(User user);
    
}

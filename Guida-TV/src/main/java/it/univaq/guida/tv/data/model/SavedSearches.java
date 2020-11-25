/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Matteo
 */
public interface SavedSearches {

    int getChannel();

    LocalDate getEndDate();

    String getGenre();

    LocalDateTime getMaxEndHour();

    LocalDateTime getMaxStartHour();

    Boolean getSendEmail();

    LocalDate getStartDate();

    String getTitle();
    
    User getUser();

    void setChannel(int channel);

    void setEndDate(LocalDate endDate);

    void setGenre(String genre);

    void setMaxEndHour(LocalDateTime maxEndHour);

    void setMaxStartHour(LocalDateTime maxStartHour);

    void setSendEmail(Boolean sendEmail);

    void setStartDate(LocalDate startDate);

    void setTitle(String title);
    
    void setUser(User user);
    
}

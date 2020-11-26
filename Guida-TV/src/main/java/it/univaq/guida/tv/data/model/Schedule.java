/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author giorg
 */
public interface Schedule extends DataItem<Integer> {

    Channel getChannel();

    LocalDate getDate();

    LocalDateTime getEndTime();

    Episode getEpisode();

    Program getProgram();

    LocalDateTime getStartTime();

    TimeSlot getTimeslot();

    void setChannel(Channel channel);

    void setDate(LocalDate date);

    void setEndTime(LocalDateTime endTime);

    void setEpisode(Episode episode);

    void setProgram(Program program);

    void setStartTime(LocalDateTime startTime);

    void setTimeslot(TimeSlot timeslot);
    
}

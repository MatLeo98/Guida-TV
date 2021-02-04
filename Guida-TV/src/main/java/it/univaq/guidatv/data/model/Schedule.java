/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author giorg
 */
public interface Schedule extends DataItem<Integer> {

    Channel getChannel();

    LocalDate getDate();

    LocalTime getEndTime();

    Episode getEpisode();

    Program getProgram();

    LocalTime getStartTime();

    TimeSlot getTimeslot();

    void setChannel(Channel channel);

    void setDate(LocalDate date);

    void setEndTime(LocalTime endTime);

    void setEpisode(Episode episode);

    void setProgram(Program program);

    void setStartTime(LocalTime startTime);

    void setTimeslot(TimeSlot timeslot);
    
}

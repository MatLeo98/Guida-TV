/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author giorg
 */
public class ScheduleImpl extends DataItemImpl<Integer> implements Schedule {

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private TimeSlot timeslot;
    
    private Channel channel;
    private Program program;
    private Episode episode;
    
    public ScheduleImpl(){
        super();
        startTime = null;
        endTime = null;
        date = null;
        timeslot = null;
        channel = null;
        program = null;
        episode = null;
    }
    
    //Mattina: 6:00 - 12:00 
    //Pomeriggio: 12:00 - 18:00
    //Sera: 18:00 - 24:00
    //Notte: 00:00 - 6:00
    public enum TimeSlot {
        mattina, pomeriggio, sera, notte;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public TimeSlot getTimeslot() {
        return timeslot;
    }

    @Override
    public void setTimeslot(TimeSlot timeslot) {
        this.timeslot = timeslot;
    }
    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Program getProgram() {
        return program;
    }

    @Override
    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public Episode getEpisode() {
        return episode;
    }

    @Override
    public void setEpisode(Episode episode) {
        this.episode = episode;
    }
    
}

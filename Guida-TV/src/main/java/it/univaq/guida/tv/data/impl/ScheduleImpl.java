/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Schedule;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author giorg
 */
public class ScheduleImpl extends DataItemImpl<Integer> implements Schedule {

    private Date startTime;
    private Date endTime;
    private Date date;
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
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
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

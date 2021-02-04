/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.proxy.ScheduleProxy;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.Schedule;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface ScheduleDAO {
    
    public ScheduleProxy createSchedule();
    
    Schedule createSchedule(ResultSet rs) throws DataException;

    Schedule getSchedule(int scheduleId) throws DataException;
    
    List<Schedule> getScheduleByProgram(Program program, LocalDate date) throws DataException;
    
    List<Schedule> getScheduleByEpisode(Episode episode) throws DataException;
    
    List<Schedule> getOnAirPrograms() throws DataException;
    
    List<Schedule> getLastMonthEpisodes(Program program) throws DataException;
    
    List<Schedule> getScheduleByTimeSlotDate(TimeSlot timeslot, LocalDate date) throws DataException;

    List<Schedule> getScheduleByChannel(Channel channel, LocalDate date) throws DataException; 
    
     List<Schedule> getScheduleByChannelAdmin(Channel channel, LocalDate date) throws DataException;
    
    List<Schedule> getScheduleByFavChannel(Channel channel, LocalDate date, TimeSlot timeslot) throws DataException;
    
    TimeSlot getCurTimeSlot() throws DataException;

    void storeSchedule(Schedule schedule) throws DataException;
    
    void deleteSchedule(Schedule schedule);
    
    List<Schedule> search(String s, String g, String c, String min, String max, String d1, String d2) throws DataException;
    
    TimeSlot generateTS(String s) throws DataException;

    void delSchedules();
    
}

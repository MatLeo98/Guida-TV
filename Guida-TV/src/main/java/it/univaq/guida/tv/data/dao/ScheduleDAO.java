/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface ScheduleDAO {
    
    Schedule createSchedule();

    Schedule getSchedule(int scheduleId) throws DataException;
    
    Schedule getScheduleByProgram(Program program) throws DataException;
    
    Schedule getScheduleByEpisode(Episode episode) throws DataException;
    
    List<Schedule> getOnAirPrograms() throws DataException;

    List<Schedule> getScheduleByDate(LocalDate date) throws DataException;

    List<Schedule> getScheduleByTimeSlot(TimeSlot timeslot) throws DataException;

    List<Schedule> getScheduleByChannel(Channel channel, LocalDate date) throws DataException; //get palinsesto per una certa data, lo usiamo anche per la data odierna
    
    List<Schedule> getScheduleBetweenDates(LocalDate startDate, LocalDate endDate) throws DataException;
    
    List<Schedule> getScheduleBetweenTimes(LocalDateTime startTime, LocalDateTime endTime) throws DataException;

    void storeSchedule(Schedule schedule) throws DataException;
    
    void deleteSchedule(Schedule schedule);
    
}

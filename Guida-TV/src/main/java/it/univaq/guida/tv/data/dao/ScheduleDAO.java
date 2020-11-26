/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

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

    Schedule getSchedule(int scheduleId);
    
    Schedule getScheduleByProgram(Program program);
    
    Schedule getScheduleByEpisode(Episode episode);
    
    List<Schedule> getOnAirPrograms();

    List<Schedule> getScheduleByDate(LocalDate date);

    List<Schedule> getScheduleByTimeSlot(TimeSlot timeslot);

    List<Schedule> getScheduleByChannel(Channel channel, LocalDate date); //get palinsesto per una certa data, lo usiamo anche per la data odierna
    
    List<Schedule> getScheduleBetweenDates(LocalDate startDate, LocalDate endDate);
    
    List<Schedule> getScheduleBetweenTimes(LocalDateTime startTime, LocalDateTime endTime);

    void storeSchedule(Schedule schedule);
    
    void deleteSchedule(Schedule schedule);
    
}

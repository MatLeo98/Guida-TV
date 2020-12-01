/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.guida.tv.data.impl.ChannelImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.impl.ScheduleImpl;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class ScheduleDAO_MySQL extends DAO implements ScheduleDAO{
    
    private PreparedStatement sOnAirPrograms;

    public ScheduleDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sOnAirPrograms = connection.prepareStatement("SELECT * FROM schedule WHERE ? > startTime && ? < endTime");
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sOnAirPrograms.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Schedule createSchedule() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Schedule getSchedule(int scheduleId) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Schedule getScheduleByProgram(Program program) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Schedule getScheduleByEpisode(Episode episode) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Schedule> getOnAirPrograms() throws DataException {
        List<Schedule> result = new ArrayList();
                       
            try (ResultSet rs = sOnAirPrograms.executeQuery()) {
                while (rs.next()) {
                     Schedule schedule = new ScheduleImpl();
					schedule.setKey(rs.getInt("idSchedule"));
					schedule.setStartTime(rs.getDate("starTime"));
                                        schedule.setEndTime(rs.getDate("endTime"));
                                        schedule.setDate(rs.getDate("date"));
                                        schedule.setTimeslot(TimeSlot.valueOf(rs.getString("timeSlot")));
                                        Program program = new ProgramImpl();
					schedule.setProgram(program);
                                        Channel channel = new ChannelImpl();
                                        schedule.setChannel(channel);
                                        schedule.setVersion(1);
					
            result.add(schedule);
            }
        } catch (SQLException ex) {
            try {
                throw new DataException("Unable to load articles by issue", ex);
            } catch (DataException ex1) {
                Logger.getLogger(EpisodeDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return result;
    }

    @Override
    public List<Schedule> getScheduleByDate(LocalDate date) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Schedule> getScheduleByTimeSlot(ScheduleImpl.TimeSlot timeslot) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Schedule> getScheduleByChannel(Channel channel, LocalDate date) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Schedule> getScheduleBetweenDates(LocalDate startDate, LocalDate endDate) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Schedule> getScheduleBetweenTimes(LocalDateTime startTime, LocalDateTime endTime) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeSchedule(Schedule schedule) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
    
}

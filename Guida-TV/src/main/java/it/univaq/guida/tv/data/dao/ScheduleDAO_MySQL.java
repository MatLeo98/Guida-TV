/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.ScheduleProxy;
import it.univaq.guida.tv.data.impl.ScheduleImpl;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class ScheduleDAO_MySQL extends DAO implements ScheduleDAO{
    
    private PreparedStatement sOnAirPrograms;
    private PreparedStatement todaySchedule;
    private PreparedStatement scheduleByID;
    private PreparedStatement todayScheduleByChannel;
    private PreparedStatement lastMonth;
    private PreparedStatement search;
    private PreparedStatement insertSchedule;
     private PreparedStatement todayScheduleByFavCh;

    public ScheduleDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sOnAirPrograms = connection.prepareStatement("SELECT * FROM schedule WHERE startTime <= CURTIME() && endTime >= CURTIME() && date = CURDATE()");
            //s = connection.prepareStatement("SELECT * FROM episode");SELECT * FROM schedule WHERE '10:30:00' < startTime && '11:40:00' > endTime
            //s = connection.prepareStatement("SELECT * FROM episode");
            todaySchedule = connection.prepareStatement("SELECT * FROM schedule WHERE date = CURDATE() AND timeSlot = ? ORDER BY channelId");
            lastMonth = connection.prepareStatement("SELECT * FROM schedule WHERE DATE(date) >= DATE(NOW()) - INTERVAL 30 DAY AND programId = ?");
            scheduleByID = connection.prepareStatement("SELECT * FROM schedule WHERE idSchedule = ?");
            todayScheduleByChannel = connection.prepareStatement("SELECT * FROM schedule WHERE date = ? AND channelId = ? ORDER BY startTime");
            todayScheduleByFavCh = connection.prepareStatement("SELECT * FROM schedule WHERE date = ? AND channelId = ? AND timeSlot = ? ORDER BY startTime");
            //todayScheduleByProgram = connection.prepareStatement("SELECT * FROM schedule WHERE date = ? ORDER BY startTime");
            search = connection.prepareStatement("SELECT * FROM schedule,program,channel WHERE programId = idProgram AND channelId = idChannel AND program.name LIKE ? AND genre LIKE ? AND channel.name LIKE ? AND startTime >= ? AND startTime <= ? AND date >= ? AND date <= ? GROUP BY program.name");
            insertSchedule = connection.prepareStatement("INSERT INTO schedule (startTime, endTime, date, timeSlot, channelId, programId) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            

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
            todayScheduleByChannel.close();
            todaySchedule.close();
            scheduleByID.close();
            lastMonth.close();
            search.close();
            insertSchedule.close();
            todayScheduleByFavCh.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public ScheduleProxy createSchedule() {
        return new ScheduleProxy(getDataLayer());
    }

    @Override
    public ScheduleProxy createSchedule(ResultSet rs) throws DataException{
        ScheduleProxy schedule = createSchedule();
        try {
            schedule.setKey(rs.getInt("idSchedule"));
            schedule.setStartTime(rs.getTime("startTime").toString());
            schedule.setEndTime(rs.getTime("endTime").toString());
            schedule.setDate(rs.getDate("date"));
            schedule.setTimeslot(TimeSlot.valueOf(rs.getString("timeSlot")));
            schedule.setProgramKey(rs.getInt("programId"));
            schedule.setChannelKey(rs.getInt("channelId"));
            schedule.setEpisodeKey(rs.getInt("episodeId"));
            schedule.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return schedule;
    }

    @Override
    public Schedule getSchedule(int scheduleId) throws DataException {
         Schedule schedule = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Schedule.class, scheduleId)) {
            schedule = dataLayer.getCache().get(Schedule.class, scheduleId);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                scheduleByID.setInt(1, scheduleId);
                try (ResultSet rs = scheduleByID.executeQuery()) {
                    if (rs.next()) {
                        schedule = createSchedule(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Schedule.class, schedule);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load article by ID", ex);
            }
        }
        return schedule;
    }

    @Override
    public Schedule getScheduleByProgram(Program program) throws DataException {
        return null;
        /*List<Schedule> result = new ArrayList();
        try {
            todayScheduleByChannel.setString(1, date.toString());
         try(ResultSet rs = todayScheduleByChannel.executeQuery()) {
                while (rs.next()) {
                   
                    result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataException("Unable to load schedule by channel", ex);
        }
        return result;*/ 
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
                     result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
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
        List<Schedule> result = new ArrayList();
        try {
            todayScheduleByChannel.setString(1, date.toString());
            todayScheduleByChannel.setInt(2, channel.getKey());
            
         try(ResultSet rs = todayScheduleByChannel.executeQuery()) {
                while (rs.next()) {
                   
                    result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataException("Unable to load schedule by channel", ex);
        }
        return result; 
    }
    
    @Override
    public List<Schedule> getScheduleByFavChannel(Channel channel, LocalDate date, TimeSlot timeslot) throws DataException {
        List<Schedule> result = new ArrayList();
        try {
            todayScheduleByFavCh.setString(1, date.toString());
            todayScheduleByFavCh.setInt(2, channel.getKey());
             todayScheduleByFavCh.setString(3, timeslot.toString());
            
         try(ResultSet rs = todayScheduleByFavCh.executeQuery()) {
                while (rs.next()) {
                   
                    result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataException("Unable to load schedule by channel", ex);
        }
        return result; 
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
    public void storeSchedule(Integer ck, Integer pk, String st, String et, String d) throws DataException {
        try {
            insertSchedule.setString(1, st.toString());
            insertSchedule.setString(2, et.toString());
            insertSchedule.setString(3, d);       
            insertSchedule.setString(4, generateTS(st, et).toString());           
            insertSchedule.setInt(5, ck);
            insertSchedule.setInt(6, pk);
            
            Schedule s = null;
            if (insertSchedule.executeUpdate() == 1) {
                    
                    try (ResultSet keys = insertSchedule.getGeneratedKeys()) {
                        
                        if (keys.next()) {
                                    
                            int key = keys.getInt(1);
                            
                            s = getSchedule(key);
                            s.setKey(key);
                            
                            dataLayer.getCache().add(Schedule.class, s);
                        }
                    }
                }

            if (s instanceof DataItemProxy) {
                ((DataItemProxy) s).setModified(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Schedule> getTodaySchedule(TimeSlot timeslot) throws DataException {
       List<Schedule> result = new ArrayList();
        try {
            todaySchedule.setString(1, timeslot.toString());
         try(ResultSet rs = todaySchedule.executeQuery()) {
                while (rs.next()) {
                    
                    result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataException("Unable to load articles by issue", ex);
        }
        return result; 
    }   

    @Override
    public TimeSlot getCurTimeSlot() throws DataException {
        TimeSlot fascia = null;
        LocalTime mattinaMin = LocalTime.parse("06:00:00");
        LocalTime mattinaMax = LocalTime.parse("11:59:59");
        LocalTime pomeriggioMin = LocalTime.parse("12:00:00");
        LocalTime pomeriggioMax = LocalTime.parse("17:59:59");
        LocalTime seraMin = LocalTime.parse("18:00:00");
        LocalTime seraMax = LocalTime.parse("23:59:59");
        LocalTime notteMin = LocalTime.parse("00:00:00");
        LocalTime notteMax = LocalTime.parse("05:59:59"); 
        if(mattinaMin.compareTo(LocalTime.now()) <= 0 && mattinaMax.compareTo(LocalTime.now()) >= 0)  
             fascia = TimeSlot.valueOf("mattina");
        
        if(pomeriggioMin.compareTo(LocalTime.now()) <= 0 && pomeriggioMax.compareTo(LocalTime.now()) >= 0) 
             fascia = TimeSlot.valueOf("pomeriggio");
             
        if(seraMin.compareTo(LocalTime.now()) <= 0 && seraMax.compareTo(LocalTime.now()) >= 0)  
             fascia = TimeSlot.valueOf("sera");
             
        if(notteMin.compareTo(LocalTime.now()) <= 0 && notteMax.compareTo(LocalTime.now()) >= 0)
             fascia = TimeSlot.valueOf("notte");
        
        return fascia;
            
    }

    @Override
    public List<Schedule> getLastMonthEpisodes(Program program) throws DataException {
         List<Schedule> result = new ArrayList();
        try {
            lastMonth.setInt(1, program.getKey());
         try(ResultSet rs = lastMonth.executeQuery()) {
                while (rs.next()) {
                    
                    result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataException("Unable to load articles by issue", ex);
        }
        return result; 
    }
    
    @Override
    public List<Schedule> search(String t, String g, String c, String min, String max, String d1, String d2) throws DataException{
        List<Schedule> result = new ArrayList();
            try{
                
                search.setString(1, "%" + t + "%");
                search.setString(2, "%" + g + "%");
                search.setString(3, "%" + c + "%");
                if(min.isEmpty()){
                    search.setString(4, "00:00");
                }else{
                    search.setString(4, min);
                }
                if(max.isEmpty()){
                    search.setString(5, "23:59");
                }else{
                    search.setString(5, max);   
                }
                if(d1.isEmpty()){
                    LocalDate d = LocalDate.now().minusMonths(1);
                    String data1 = d.toString();
                    search.setString(6, data1);
                }else{
                    search.setString(6, d1);
                }
                if(d2.isEmpty()){
                    LocalDate d = LocalDate.now().plusDays(12);
                    String data2 = d.toString();
                    search.setString(7, data2);
                }else{
                    search.setString(7, d2);
                }              
                    try(ResultSet rs = search.executeQuery()) {
                        while (rs.next()) {                  
                        result.add((Schedule) getSchedule(rs.getInt("idSchedule")));
                        }
                    }      
            }catch(SQLException ex){
                throw new DataException("Unable to load results", ex);
            }
        return result;
    }
    
   @Override
    public TimeSlot generateTS(String s, String e) throws DataException {
        TimeSlot ts = null;
        LocalTime mattinaMin = LocalTime.parse("06:00:00");
        LocalTime mattinaMax = LocalTime.parse("11:59:59");
        LocalTime pomeriggioMin = LocalTime.parse("12:00:00");
        LocalTime pomeriggioMax = LocalTime.parse("17:59:59");
        LocalTime seraMin = LocalTime.parse("18:00:00");
        LocalTime seraMax = LocalTime.parse("23:59:59");
        LocalTime notteMin = LocalTime.parse("00:00:00");
        LocalTime notteMax = LocalTime.parse("05:59:59"); 
        LocalTime start = LocalTime.parse(s);
        LocalTime end = LocalTime.parse(e);
        if(mattinaMin.compareTo(start) <= 0 && mattinaMax.compareTo(end) >= 0)  
             ts = TimeSlot.valueOf("mattina");
        
        if(pomeriggioMin.compareTo(start) <= 0 && pomeriggioMax.compareTo(end) >= 0) 
             ts = TimeSlot.valueOf("pomeriggio");
             
        if(seraMin.compareTo(start) <= 0 && seraMax.compareTo(end) >= 0)  
             ts = TimeSlot.valueOf("sera");
             
        if(notteMin.compareTo(start) <= 0 && notteMax.compareTo(end) >= 0)
             ts = TimeSlot.valueOf("notte");
        
        return ts;
            
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guidatv.data.dao.ChannelDAO;
import it.univaq.guidatv.data.dao.EpisodeDAO;
import it.univaq.guidatv.data.dao.ProgramDAO;
import it.univaq.guidatv.data.impl.ScheduleImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class ScheduleProxy extends ScheduleImpl implements DataItemProxy{
    
    protected boolean modified;
    protected int program_key = 0;
    protected int episode_key = 0;
    protected int channel_key = 0;
    

    protected DataLayer dataLayer;
    
    
    public ScheduleProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
        this.program_key = 0;
        this.episode_key = 0;
        this.channel_key = 0;
        
    }
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }
    

    @Override
    public void setEpisode(Episode episode) {
        super.setEpisode(episode);
        this.modified = true;
    }

    @Override
    public Episode getEpisode() {
        if (super.getEpisode() == null && episode_key > 0) {
            try {
                super.setEpisode(((EpisodeDAO) dataLayer.getDAO(Episode.class)).getEpisode(episode_key));
            } catch (DataException ex) {
                Logger.getLogger(ScheduleProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getEpisode();
    }

    @Override
    public void setProgram(Program program) {
        super.setProgram(program);
        this.modified = true;
    }

    @Override
    public Program getProgram() {
        if (super.getProgram() == null && program_key > 0) {
            try {
                super.setProgram(((ProgramDAO) dataLayer.getDAO(Program.class)).getProgram(program_key));
            } catch (DataException ex) {
                Logger.getLogger(ScheduleProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getProgram();
    }

    @Override
    public void setChannel(Channel channel) {
        super.setChannel(channel); 
        this.modified = true;
    }

    @Override
    public Channel getChannel() {
        if (super.getChannel() == null && channel_key > 0) {
            try {
                super.setChannel(((ChannelDAO) dataLayer.getDAO(Channel.class)).getChannel(channel_key));
            } catch (DataException ex) {
                Logger.getLogger(ScheduleProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getChannel();
    }

    @Override
    public void setTimeslot(TimeSlot timeslot) {
        super.setTimeslot(timeslot); 
        this.modified = true;
    }

    @Override
    public void setDate(LocalDate date) {
        super.setDate(date); 
        this.modified = true;
    }

    @Override
    public void setEndTime(LocalTime endTime) {
        super.setEndTime(endTime); 
        this.modified = true;
    }

    @Override
    public void setStartTime(LocalTime startTime) {
        super.setStartTime(startTime); 
        this.modified = true;
    }
    
    
    
    
    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
    
     public void setProgramKey(int program_key) {
        this.program_key = program_key;
        //resettiamo la cache dell'autore
        //reset author cache
        super.setProgram(null);
    }

    public void setChannelKey(int channel_key) {
        this.channel_key = channel_key;
        super.setChannel(null);
    }
    
    public void setEpisodeKey(int episode_key) {
        this.episode_key = episode_key;
        super.setEpisode(null);
    }
    
    
}

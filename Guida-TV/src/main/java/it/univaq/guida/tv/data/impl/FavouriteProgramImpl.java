/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;


import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.User;

/**
 *
 * @author giorg
 */
public class FavouriteProgramImpl implements FavouriteProgram {

    private TimeSlot timeSlot;
    
    private Program program;
    private User user;
    
    public FavouriteProgramImpl(){
        timeSlot = null;
        program = null;
        user = null;
    } 
    
    @Override
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
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
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    
    
    
    
}

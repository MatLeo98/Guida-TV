/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;

/**
 *
 * @author giorg
 */
public class EpisodeImpl extends DataItemImpl<Integer> implements Episode {

    private String name;
    private int seasonNumber;
    private int number;
    
    private Program program;
    
    public EpisodeImpl(){
        super();
        name = "";
        seasonNumber = 0;
        number = 0;
        
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    @Override
    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    @Override
    public Integer getNumber() {
        return number;
    }

    @Override
    public void setNumber(Integer number) {
        this.number = number;
    }
    
    @Override
     public Program getProgram() {
        return program;
    }

    @Override
    public void setProgram(Program program) {
        this.program = program;
    }
    
    
}

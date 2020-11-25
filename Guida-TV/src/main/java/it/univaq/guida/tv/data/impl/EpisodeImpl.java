/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;

/**
 *
 * @author giorg
 */
public class EpisodeImpl implements Episode {

    private Integer id;
    private String name;
    private int seasonNumber;
    private int number;
    
    private Program program;
    
    public EpisodeImpl(){
        id = null;
        name = "";
        seasonNumber = 0;
        number = 0;
        
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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

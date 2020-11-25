/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

/**
 *
 * @author giorg
 */
public interface Episode {

    Integer getId();

    String getName();

    Integer getNumber();

    Integer getSeasonNumber();
    
    Program getProgram();

    void setId(Integer id);

    void setName(String name);

    void setNumber(Integer number);

    void setSeasonNumber(Integer seasonNumber);
    
    void setProgram(Program program);
    
}

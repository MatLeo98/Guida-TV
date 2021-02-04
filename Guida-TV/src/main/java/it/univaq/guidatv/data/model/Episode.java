/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.model;

import it.univaq.framework.data.DataItem;

/**
 *
 * @author giorg
 */
public interface Episode extends DataItem<Integer> {

    String getName();

    Integer getNumber();

    Integer getSeasonNumber();
    
    Program getProgram();

    void setName(String name);

    void setNumber(Integer number);

    void setSeasonNumber(Integer seasonNumber);
    
    void setProgram(Program program);
    
}

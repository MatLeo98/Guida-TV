/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.Program;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface ProgramDAO {
    
    Program createProgram();

    Program getProgram(int programId) throws DataException;
    
    Program getProgramByEpisode(Episode episode) throws DataException;
    
    List<Program> getProgramsByGenre(Genre genre) throws DataException;
    
    List<Program> getProgramsLikeTitolo(String titolo) throws DataException;

    List<Program> getPrograms() throws DataException;

    List<Program> getTvSeries() throws DataException;

    void storeProgram(String n, String desc, String gen, String l, Boolean serie, Integer sN) throws DataException;
    
    void deleteProgram(Program program);
    
}

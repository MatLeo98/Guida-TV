/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Program;
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

    void storeProgram(Program program) throws DataException;
    
    void deleteProgram(Program program);
    
}

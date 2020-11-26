/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface ProgramDAO {
    
    Program createProgram();

    Program getProgram(int programId);
    
    Program getProgramByEpisode(Episode episode);
    
    List<Program> getProgramsByGenre(Genre genre);
    
    List<Program> getProgramsLikeTitolo(String titolo);

    List<Program> getPrograms();

    List<Program> getTvSeries();

    void storeProgram(Program program);
    
    void deleteProgram(Program program);
    
}

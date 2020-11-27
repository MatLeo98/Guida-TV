/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface EpisodeDAO {
    
    Episode createEpisode();

    Episode getEpisode(int episodeId) throws DataException;

    List<Episode> getProgramEpisodes(Program program) throws DataException;
    
    List<Episode> getLastMonthEpisodes(Program program) throws DataException;
    
    void deleteEpisode(Episode episode);
    
}

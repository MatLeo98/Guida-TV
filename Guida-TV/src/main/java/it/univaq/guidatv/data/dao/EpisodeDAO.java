/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
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
    
    List<Episode> getAllEpisodes() throws DataException;
    
    void storeEpisode(Episode episode) throws DataException;
    
    void deleteEpisode(Episode episode);
    
}

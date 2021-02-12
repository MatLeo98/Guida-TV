/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.OptimisticLockException;
import it.univaq.framework.data.proxy.EpisodeProxy;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class EpisodeDAO_MySQL extends DAO implements EpisodeDAO{
    
    private PreparedStatement allEpisodes;
    private PreparedStatement episodeByID;
    private PreparedStatement insertEpisode;
    private PreparedStatement programEpisodes;
    private PreparedStatement updateEpisode;
    private PreparedStatement deleteEpisode;

    public EpisodeDAO_MySQL(DataLayer d) {
        super(d);
    }
    
     @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            allEpisodes = connection.prepareStatement("SELECT * FROM episode");
            episodeByID = connection.prepareStatement("SELECT * FROM episode WHERE idEpisode = ?");
            insertEpisode = connection.prepareStatement("INSERT INTO episode (name, seasonNumber, number, programId) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            programEpisodes = connection.prepareStatement("SELECT * FROM episode WHERE programId = ?");
            updateEpisode = connection.prepareStatement("UPDATE episode SET name = ?, seasonNumber = ?, number = ?, version = ? WHERE idEpisode = ? AND version = ?");
            deleteEpisode = connection.prepareStatement("DELETE FROM episode WHERE idEpisode = ?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            allEpisodes.close();
            episodeByID.close();
            insertEpisode.close();
            programEpisodes.close();
            updateEpisode.close();
            deleteEpisode.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public EpisodeProxy createEpisode() {
        return new EpisodeProxy(getDataLayer());
    }
    
     public EpisodeProxy createEpisode(ResultSet rs) throws DataException{
            EpisodeProxy episode = createEpisode();
        try {
            episode.setKey(rs.getInt("idEpisode"));
            episode.setName(rs.getString("name"));
            episode.setSeasonNumber(rs.getInt("seasonNumber"));
            episode.setNumber(rs.getInt("number"));
            episode.setProgramKey(rs.getInt("programId"));
            episode.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create episode object form ResultSet", ex);
        }
        return episode;
    }

    @Override
    public Episode getEpisode(int episodeId) throws DataException {
        Episode episode = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Episode.class, episodeId)) {
            episode = dataLayer.getCache().get(Episode.class, episodeId);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                episodeByID.setInt(1, episodeId);
                try (ResultSet rs = episodeByID.executeQuery()) {
                    if (rs.next()) {
                        episode = createEpisode(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Episode.class, episode);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load article by ID", ex);
            }
        }
        return episode;
    }

    @Override
    public List<Episode> getProgramEpisodes(Program program) throws DataException {
        List<Episode> result = new ArrayList();
        try {
            programEpisodes.setInt(1, program.getKey());
         try(ResultSet rs = programEpisodes.executeQuery()) {
                while (rs.next()) {
                   
                    result.add((Episode) getEpisode(rs.getInt("idEpisode")));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataException("Unable to load program's episodes", ex);
        }
        return result; 
    }
    @Override
    public List<Episode> getAllEpisodes() throws DataException {
        List<Episode> result = new ArrayList();
         try(ResultSet rs = allEpisodes.executeQuery()) {
                while (rs.next()) {
                   
                    result.add((Episode) getEpisode(rs.getInt("idEpisode")));
                }
            }
        catch (SQLException ex) {
            throw new DataException("Unable to load episodes", ex);
        }
        return result; 
    }
    
    @Override
    public void storeEpisode(Episode episode) throws DataException{
          try {
              if (episode.getKey() != null && episode.getKey() > 0) {//update
                updateEpisode.setString(1, episode.getName());
                updateEpisode.setInt(2, episode.getSeasonNumber());
                updateEpisode.setInt(3, episode.getNumber());
                
                long current_version = episode.getVersion();
                long next_version = current_version + 1;
                
                updateEpisode.setLong(4, next_version);
                updateEpisode.setInt(5, episode.getKey());
                updateEpisode.setLong(6, current_version);
                
                if (updateEpisode.executeUpdate() == 0) {
                    throw new OptimisticLockException(episode);
                }
                
                episode.setVersion(next_version);
                
              }else{//insert
                    insertEpisode.setString(1, episode.getName());
                    insertEpisode.setInt(2, episode.getSeasonNumber());
                    insertEpisode.setInt(3, episode.getNumber());
                    insertEpisode.setInt(4, episode.getProgram().getKey());

                    Episode e = null;
                    if (insertEpisode.executeUpdate() == 1) {

                        try (ResultSet keys = insertEpisode.getGeneratedKeys()) {

                            if (keys.next()) {

                                int key = keys.getInt(1);

                                e = getEpisode(key);
                                e.setKey(key);

                                dataLayer.getCache().add(Episode.class, e);
                            }
                        }
                    }
               }

                if (episode instanceof DataItemProxy) {
                    ((DataItemProxy) episode).setModified(false);
                }
              
        } catch (SQLException ex) {
            Logger.getLogger(ProgramDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteEpisode(Episode episode) {
        try {
            
            deleteEpisode.setInt(1,episode.getKey());
            deleteEpisode.executeUpdate();
                
        } catch (SQLException ex) {
            Logger.getLogger(EpisodeDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

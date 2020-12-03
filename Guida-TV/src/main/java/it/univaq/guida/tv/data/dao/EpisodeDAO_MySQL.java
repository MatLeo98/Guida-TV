/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.ChannelProxy;
import it.univaq.framework.data.proxy.EpisodeProxy;
import it.univaq.guida.tv.data.impl.ChannelImpl;
import it.univaq.guida.tv.data.impl.EpisodeImpl;
import it.univaq.guida.tv.data.impl.ImageImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Image;
import it.univaq.guida.tv.data.model.Program;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class EpisodeDAO_MySQL extends DAO implements EpisodeDAO{
    
    private PreparedStatement s;
    private PreparedStatement episodeByID;

    public EpisodeDAO_MySQL(DataLayer d) {
        super(d);
    }
    
     @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            s = connection.prepareStatement("SELECT * FROM episode");
            episodeByID = connection.prepareStatement("SELECT * FROM episode WHERE idEpisode = ?");
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            s.close();
            episodeByID.close();


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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Episode> getLastMonthEpisodes(Program program) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteEpisode(Episode episode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
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
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            s.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Episode createEpisode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Episode getEpisode(int episodeId) throws DataException {
        List<Episode> result = new ArrayList();

        try {
            //sArticlesByIssue.setInt(1, issue.getKey());            
            try (ResultSet rs = s.executeQuery()) {
                while (rs.next()) {
                     Episode candidatura = new EpisodeImpl();
					candidatura.setKey(rs.getInt("id"));
					candidatura.setName(rs.getString("name"));
                                        candidatura.setSeasonNumber(rs.getInt("seasonNumber"));
                                        candidatura.setNumber(rs.getInt("number"));
                                        Program program = new ProgramImpl();
					candidatura.setProgram(program);
					candidatura.setVersion(1);
					
					
            result.add(candidatura);
                    //result.add((Channel) rs);
                }
            }
        } catch (SQLException ex) {
            try {
                throw new DataException("Unable to load articles by issue", ex);
            } catch (DataException ex1) {
                Logger.getLogger(EpisodeDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return result.get(0);
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

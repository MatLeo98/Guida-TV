/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.EpisodeProxy;
import it.univaq.framework.data.proxy.ProgramProxy;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author giorg
 */
public class ProgramDAO_MySQL extends DAO implements ProgramDAO{
    
    private PreparedStatement programByID;

    public ProgramDAO_MySQL(DataLayer d) {
        super(d);
    }
    
     @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            programByID = connection.prepareStatement("SELECT * FROM program WHERE idProgram = ?");
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            programByID.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public ProgramProxy createProgram() {
           return new ProgramProxy(getDataLayer());
    }
    
    public ProgramProxy createEpisode(ResultSet rs) throws DataException{
            ProgramProxy episode = createProgram();
        try {
            episode.setKey(rs.getInt("idProgram"));
            episode.setName(rs.getString("name"));
            episode.setDescription(rs.getString("description"));
            episode.setGenre(Genre.valueOf(rs.getString("genre")));
            episode.setLink(rs.getString("link"));
            episode.setIsSerie(rs.getBoolean("isSerie"));
            episode.setSeasonsNumber(rs.getInt("seasonsNumber"));
            episode.setImageKey(rs.getInt("imageId"));
            episode.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create episode object form ResultSet", ex);
        }
        return episode;
    }

    @Override
    public Program getProgram(int programId) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Program getProgramByEpisode(Episode episode) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Program> getProgramsByGenre(ProgramImpl.Genre genre) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Program> getProgramsLikeTitolo(String titolo) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Program> getPrograms() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Program> getTvSeries() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeProgram(Program program) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteProgram(Program program) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

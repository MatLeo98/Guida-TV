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
    
    public ProgramProxy createProgram(ResultSet rs) throws DataException{
            ProgramProxy program = createProgram();
        try {
            program.setKey(rs.getInt("idProgram"));
            program.setName(rs.getString("name"));
            program.setDescription(rs.getString("description"));
            program.setGenre(Genre.valueOf(rs.getString("genre")));
            program.setLink(rs.getString("link"));
            program.setIsSerie(rs.getBoolean("isSerie"));
            program.setSeasonsNumber(rs.getInt("seasonsNumber"));
            program.setImageKey(rs.getInt("imageId"));
            program.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create program object form ResultSet", ex);
        }
        return program;
    }

    @Override
    public Program getProgram(int programId) throws DataException {
       Program program = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Program.class, programId)) {
            program = dataLayer.getCache().get(Program.class, programId);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                programByID.setInt(1, programId);
                try (ResultSet rs = programByID.executeQuery()) {
                    if (rs.next()) {
                        program = createProgram(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Program.class, program);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load article by ID", ex);
            }
        }
        return program;
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

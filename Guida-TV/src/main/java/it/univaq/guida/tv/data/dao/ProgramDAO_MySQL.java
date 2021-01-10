/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.controller.ProfileController;
import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.OptimisticLockException;
import it.univaq.framework.data.proxy.ProgramProxy;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.Program;
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
public class ProgramDAO_MySQL extends DAO implements ProgramDAO{
    
    private PreparedStatement programByID;
    private PreparedStatement allPrograms;
    private PreparedStatement insertProgram;
    private PreparedStatement updateProgram;
    private PreparedStatement deleteProgram;

    public ProgramDAO_MySQL(DataLayer d) {
        super(d);
    }
    
     @Override
    public void init() throws DataException {
        try {
            super.init();

            programByID = connection.prepareStatement("SELECT * FROM program WHERE idProgram = ?");
            allPrograms = connection.prepareStatement("SELECT idProgram FROM program");
            insertProgram = connection.prepareStatement("INSERT INTO program (name, description, genre, link, isSerie, seasonsNumber) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            updateProgram = connection.prepareStatement("UPDATE program SET name = ?, description = ?, genre = ?, link = ?, isSerie = ?, seasonsNumber = ?, version = ? WHERE idProgram = ? AND version = ?");
            deleteProgram = connection.prepareStatement("DELETE FROM program WHERE idProgram = ?");
            
        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        try {

            programByID.close();
            allPrograms.close();
            insertProgram.close();
            updateProgram.close();
            deleteProgram.close();


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
        List<Program> programs = new ArrayList();
        
            try (ResultSet rs = allPrograms.executeQuery()) {
            while (rs.next()) {
                programs.add((Program) getProgram(rs.getInt("idProgram")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load channels", ex);
        }
        return programs;
    }

    @Override
    public List<Program> getTvSeries() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeProgram(Program program) throws DataException {
        try{
        if (program.getKey() != null && program.getKey() > 0) {//update
            
                updateProgram.setString(1, program.getName());
                updateProgram.setString(2, program.getDescription());
                updateProgram.setString(3, program.getGenre().toString());
                updateProgram.setString(4, program.getLink());
                updateProgram.setBoolean(5, program.IsSerie());
                updateProgram.setInt(6, program.getSeasonsNumber());
                
                long current_version = program.getVersion();
                long next_version = current_version + 1;
                

                updateProgram.setLong(7, next_version);
                updateProgram.setInt(8, program.getKey());
                updateProgram.setLong(9, current_version);
                
                if (updateProgram.executeUpdate() == 0) {
                    throw new OptimisticLockException(program);
                }
                
                program.setVersion(next_version);
                
            
        }else{//insert
            
                 insertProgram.setString(1, program.getName());
                 insertProgram.setString(2, program.getDescription());
                 insertProgram.setString(3, program.getGenre().toString());
                 insertProgram.setString(4, program.getLink());
                 insertProgram.setBoolean(5, program.IsSerie());
                 insertProgram.setInt(6, program.getSeasonsNumber());
                 Program p = null;
                 if (insertProgram.executeUpdate() == 1) {

                         try (ResultSet keys = insertProgram.getGeneratedKeys()) {

                             if (keys.next()) {

                                 int key = keys.getInt(1);

                                 p = getProgram(key);
                                 p.setKey(key);

                                 dataLayer.getCache().add(Program.class, p);
                             }
                         }
                     }
                 
                 }

                 if (program instanceof DataItemProxy) {
                     ((DataItemProxy) program).setModified(false);
                 }
             } catch (SQLException ex) {
                 Logger.getLogger(ProgramDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
             }
        
    }

    @Override
    public void deleteProgram(Program program) {
        try {
            deleteProgram.setInt(1, program.getKey());
            deleteProgram.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProgramDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}

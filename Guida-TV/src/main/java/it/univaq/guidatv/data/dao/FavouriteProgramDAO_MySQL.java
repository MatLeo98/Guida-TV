/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.EpisodeProxy;
import it.univaq.framework.data.proxy.FavouriteProgramProxy;
import it.univaq.guidatv.data.impl.ScheduleImpl;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.User;
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
public class FavouriteProgramDAO_MySQL extends DAO implements FavouriteProgramDAO{
    
    private PreparedStatement favProgramByID;
    private PreparedStatement favProgramsByUser;
    private PreparedStatement storeFavPrograms;
    private PreparedStatement delFavProg;

    public FavouriteProgramDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            favProgramByID = connection.prepareStatement("SELECT * FROM favouriteprogram WHERE idFavProgram = ?");
            favProgramsByUser = connection.prepareStatement("SELECT * FROM favouriteprogram WHERE emailUser = ?");
            storeFavPrograms = connection.prepareStatement("INSERT INTO favouriteprogram (emailUser,programId,savedSearchId) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            delFavProg = connection.prepareStatement("DELETE FROM favouriteprogram WHERE idFavProgram = ?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            favProgramsByUser.close();
            storeFavPrograms.close();
            favProgramByID.close();
            delFavProg.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public FavouriteProgramProxy createFavouriteProgram() {
       return new FavouriteProgramProxy(getDataLayer());
    }
    
    public FavouriteProgramProxy createFavouriteProgram(ResultSet rs) throws DataException{
            FavouriteProgramProxy favProgram = createFavouriteProgram();
        try {
            favProgram.setKey(rs.getInt("idFavProgram"));
            favProgram.setUserKey(rs.getString("emailUser"));
            favProgram.setProgramKey(rs.getInt("programId"));
            favProgram.setSavedSearchKey(rs.getInt("savedSearchId"));
            favProgram.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create FavouriteProgram object form ResultSet", ex);
        }
        return favProgram;
    }
    
    @Override
    public FavouriteProgram getFavouriteProgram(int key) throws DataException {
        FavouriteProgram favP = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(FavouriteProgram.class, key)) {
            favP = dataLayer.getCache().get(FavouriteProgram.class, key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                favProgramByID.setInt(1, key);
                try (ResultSet rs = favProgramByID.executeQuery()) {
                    if (rs.next()) {
                        favP = createFavouriteProgram(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(FavouriteProgram.class, favP);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Favourite Channel by ID", ex);
            }
        }
        return favP; 
    }
    
    @Override
    public void storeFavPrograms(List<Program> programs, String email, Integer SavedSId) throws DataException {
       for(Program p : programs){
           try {
               storeFavPrograms.setString(1,email);
               storeFavPrograms.setInt(2,p.getKey());
               storeFavPrograms.setInt(3,SavedSId);
               
               if (storeFavPrograms.executeUpdate() == 1) {
                    
                    try (ResultSet keys = storeFavPrograms.getGeneratedKeys()) {
                        
                        if (keys.next()) {
                            
                            int key = keys.getInt(1);
                            
                            FavouriteProgram favP = getFavouriteProgram(key);
                            favP.setKey(key);
                            
                            dataLayer.getCache().add(FavouriteProgram.class, favP);
                        }
                    }   catch (DataException ex) {
                            Logger.getLogger(FavouriteChannelDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
           } catch (SQLException ex) {
               Logger.getLogger(FavouriteProgramDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
    }

    @Override
    public List<FavouriteProgram> getFavouritePrograms(User user) throws DataException {
        List<FavouriteProgram> favP = new ArrayList();
        try {
            favProgramsByUser.setString(1,user.getKey());
            
        
            try (ResultSet rs = favProgramsByUser.executeQuery()) {
            while (rs.next()) {
                favP.add((FavouriteProgram) getFavouriteProgram(rs.getInt("idFavProgram")));
            }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load favourite programs", ex);
        }
        
        
        return favP;
    }

    @Override
    public void deleteFavouriteProgram(int key) {
        try {
            
            delFavProg.setInt(1,key);
            delFavProg.executeUpdate();
                
        } catch (SQLException ex) {
            Logger.getLogger(FavouriteProgramDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    
    
    
    
}

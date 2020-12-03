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
import it.univaq.framework.data.proxy.FavouriteProgramProxy;
import it.univaq.guida.tv.data.impl.ScheduleImpl;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author giorg
 */
public class FavouriteProgramDAO_MySQL extends DAO implements FavouriteProgramDAO{
    
    private PreparedStatement favProgramByID;

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
            

        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            
            favProgramByID.close();


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
            favProgram.setTimeSlot(TimeSlot.valueOf(rs.getString("timeSlot")));
            favProgram.setUserKey(rs.getString("emailUser"));
            favProgram.setProgramKey(rs.getInt("programId"));
            favProgram.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create FavouriteProgram object form ResultSet", ex);
        }
        return favProgram;
    }

    @Override
    public List<FavouriteProgram> getFavouritePrograms(User user) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteFavouriteProgram(FavouriteProgram favProgram) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

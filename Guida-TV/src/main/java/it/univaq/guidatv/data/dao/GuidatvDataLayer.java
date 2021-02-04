/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.FavouriteChannel;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Image;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author giorg
 */
public class GuidatvDataLayer extends DataLayer{
    
    public GuidatvDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }

    @Override
    public void init() throws DataException {
        //registriamo i nostri dao
        //register our daos
        registerDAO(Channel.class, new ChannelDAO_MySQL(this));
        registerDAO(Episode.class, new EpisodeDAO_MySQL(this));
        registerDAO(FavouriteChannel.class, new FavouriteChannelDAO_MySQL(this));
        registerDAO(FavouriteProgram.class, new FavouriteProgramDAO_MySQL(this));
        registerDAO(Program.class, new ProgramDAO_MySQL(this));
        registerDAO(SavedSearches.class, new SavedSearchesDAO_MySQL(this));
        registerDAO(Schedule.class, new ScheduleDAO_MySQL(this));
        registerDAO(User.class, new UserDAO_MySQL(this));
        registerDAO(Image.class, new ImageDAO_MySQL(this));
    }

    //helpers    
    public ChannelDAO getChannelDAO() {
        return (ChannelDAO) getDAO(Channel.class);
    }

    public EpisodeDAO getEpisodeDAO() {
        return (EpisodeDAO) getDAO(Episode.class);
    }

    public FavouriteChannelDAO getFavouriteChannelDAO() {
        return (FavouriteChannelDAO) getDAO(FavouriteChannel.class);
    }
    
    public FavouriteProgramDAO getFavouriteProgramDAO() {
        return (FavouriteProgramDAO) getDAO(FavouriteProgram.class);
    }
    
    public ProgramDAO getProgramDAO() {
        return (ProgramDAO) getDAO(Program.class);
    }
    
    public SavedSearchesDAO getSavedSearchesDAO() {
        return (SavedSearchesDAO) getDAO(SavedSearches.class); 
    }
    
    public ScheduleDAO getScheduleDAO() {
        return (ScheduleDAO) getDAO(Schedule.class);
    }
    
    public UserDAO getUserDAO() {
        return (UserDAO) getDAO(User.class);
    }

    public ImageDAO getImageDAO() {
        return (ImageDAO) getDAO(Image.class);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.dao;

import it.univaq.framework.data.DAO;
import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.data.proxy.ImageProxy;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Image;
import it.univaq.guida.tv.data.model.Program;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author giorg
 */
public class ImageDAO_MySQL extends DAO implements ImageDAO{
    
    private PreparedStatement ImageByID;

    public ImageDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            ImageByID = connection.prepareStatement("SELECT * FROM image WHERE idImage=?");

            

        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            ImageByID.close();


        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public ImageProxy createImage() {
        return new ImageProxy(getDataLayer());
    }
    
     private ImageProxy createImage(ResultSet rs) throws DataException {
        ImageProxy image = createImage();
        try {
            image.setKey(rs.getInt("idImage"));
            image.setImageSize(rs.getLong("size"));
            image.setCaption(rs.getString("caption"));
            image.setImageType(rs.getString("type"));
            image.setImageFilename(rs.getString("fileName"));
            image.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create image object form ResultSet", ex);
        }
        return image;
    }

    @Override
    public Image getImage(int image_key) throws DataException {
        Image i = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Image.class, image_key)) {
            i = dataLayer.getCache().get(Image.class, image_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                ImageByID.clearParameters();
                ImageByID.setInt(1, image_key);
                try (ResultSet rs = ImageByID.executeQuery()) {
                    if (rs.next()) {
                        i = createImage(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Image.class, i);

                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load image by ID", ex);
            }
        }
        return i;
    }

    @Override
    public Image getProgramImage(Program program) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Image getChannelImage(Channel channel) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

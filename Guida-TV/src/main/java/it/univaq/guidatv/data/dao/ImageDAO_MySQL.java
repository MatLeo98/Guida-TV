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
import it.univaq.framework.data.proxy.ImageProxy;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Image;
import it.univaq.guidatv.data.model.Program;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class ImageDAO_MySQL extends DAO implements ImageDAO{
    
    private PreparedStatement ImageByID;
    private PreparedStatement insertImage;
    private PreparedStatement updateImage;
    private PreparedStatement deleteImage;

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
            insertImage = connection.prepareStatement("INSERT INTO image (link) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            updateImage = connection.prepareStatement("UPDATE image SET link = ?, version = ? WHERE idImage = ? AND version = ?");
            deleteImage = connection.prepareStatement("DELETE FROM image WHERE idImage = ?");

            

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
            insertImage.close();
            updateImage.close();
            deleteImage.close();

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
            image.setLink(rs.getString("link"));
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
    
    @Override
    public Image storeImage(Image image) throws DataException {
        Image i = null;    
        try{
            if (image.getKey() != null && image.getKey() > 0) {//update
                updateImage.setString(1, image.getLink());

                long current_version = image.getVersion();
                long next_version = current_version + 1;                

                updateImage.setLong(2, next_version);
                updateImage.setInt(3, image.getKey());
                updateImage.setLong(4, current_version);

                if (updateImage.executeUpdate() == 0) {
                    throw new OptimisticLockException(image);
                }

                image.setVersion(next_version);
                    
            }else{//insert
                insertImage.setString(1, image.getLink());
                
                 if (insertImage.executeUpdate() == 1) {

                         try (ResultSet keys = insertImage.getGeneratedKeys()) {

                             if (keys.next()) {

                                 int key = keys.getInt(1);

                                 i = getImage(key);
                                 i.setKey(key);

                                 dataLayer.getCache().add(Image.class, i);
                             }
                         }
                     }
            }
            
            if (image instanceof DataItemProxy) {
                     ((DataItemProxy) image).setModified(false);
                 }
            
        } catch (SQLException ex) {
                 Logger.getLogger(ProgramDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
             }
        
        return i;
    }
}

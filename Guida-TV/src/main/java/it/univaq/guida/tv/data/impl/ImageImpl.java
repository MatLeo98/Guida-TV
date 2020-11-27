/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemImpl;
import it.univaq.guida.tv.data.model.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class ImageImpl extends DataItemImpl<Integer> implements Image {
 
    private String caption;
    private String imageType;
    private String imageFilename;
    private long imageSize;
  

    public ImageImpl() {
         super();
        caption = "";
        imageSize = 0;
        imageType = "";
        imageFilename = "";
        
    }
    
    @Override
     public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getImageType() {
        return imageType;
    }

    @Override
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Override
    public String getImageFilename() {
        return imageFilename;
    }

    @Override
    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    @Override
    public long getImageSize() {
        return imageSize;
    }

    @Override
    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }
    
    @Override
    public InputStream getImageData() throws DataException {
        try {
            return new FileInputStream(imageFilename);
        } catch (FileNotFoundException ex) {
            throw new DataException("Error opening image file", ex);
        }
    }

    @Override
    public void setImageData(InputStream is) throws DataException {

        OutputStream os = null;
        try {
            byte[] buffer = new byte[1024];
            os = new FileOutputStream(imageFilename);
            int read;
            while ((read = is.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        } catch (FileNotFoundException ex) {
            throw new DataException("Error storing image file", ex);
        } catch (IOException ex) {
            throw new DataException("Error storing image file", ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

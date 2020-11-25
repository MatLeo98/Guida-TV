/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.guida.tv.data.model.Image;

/**
 *
 * @author giorg
 */
public class ImageImpl implements Image {
 
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
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;

/**
 *
 * @author giorg
 */
public interface Image extends DataItem<Integer> {

    String getCaption();

    String getImageFilename();

    long getImageSize();

    String getImageType();

    void setCaption(String caption);

    void setImageFilename(String imageFilename);

    void setImageSize(long imageSize);

    void setImageType(String imageType);
    
}

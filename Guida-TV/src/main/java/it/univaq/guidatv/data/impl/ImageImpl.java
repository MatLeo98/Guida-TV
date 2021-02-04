/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.model.Image;

/**
 *
 * @author giorg
 */
public class ImageImpl extends DataItemImpl<Integer> implements Image {

    private String link;

    public ImageImpl() {
         super();
        link = "";
        
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }
    
}

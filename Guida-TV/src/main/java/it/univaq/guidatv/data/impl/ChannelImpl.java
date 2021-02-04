/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.impl;

import it.univaq.framework.data.DataItemImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Image;

/**
 *
 * @author Matteo
 */
public class ChannelImpl extends DataItemImpl<Integer> implements Channel {
    
  
    private String name;
    private Image image;
    
    public ChannelImpl(){
    super();
    name = "";
    image = null;
    
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void setImage(Image image) {
        this.image = image;
    }
    
}

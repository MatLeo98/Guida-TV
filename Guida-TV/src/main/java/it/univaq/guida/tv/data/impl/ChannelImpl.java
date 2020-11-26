/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Image;

/**
 *
 * @author Matteo
 */
public class ChannelImpl implements Channel {
    
    private int number;
    private String name;
    private Image image;
    
    public ChannelImpl(){
    
    number = 0;
    name = "";
    image = null;
    
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
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

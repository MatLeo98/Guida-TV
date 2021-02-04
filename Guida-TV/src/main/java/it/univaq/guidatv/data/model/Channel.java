/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.model;

import it.univaq.framework.data.DataItem;

/**
 *
 * @author Matteo
 */
public interface Channel extends DataItem<Integer>{

    String getName();

    void setName(String name);
    
    Image getImage();
    
    void setImage(Image image);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

/**
 *
 * @author Matteo
 */
public interface Channel {

    String getName();

    int getNumber();

    void setName(String name);

    void setNumber(int number);
    
    Image getImage();
    
    void setImage(Image image);
    
}

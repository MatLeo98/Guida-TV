/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.model;

import it.univaq.framework.data.DataItem;

/**
 *
 * @author giorg
 */
public interface Image extends DataItem<Integer> {

    String getLink();

    void setLink(String link);   
    
}

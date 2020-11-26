/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.model;

import it.univaq.framework.data.DataItem;
import it.univaq.guida.tv.data.impl.ImageImpl;
import it.univaq.guida.tv.data.impl.ImageImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import java.util.ArrayList;

/**
 *
 * @author giorg
 */
public interface Program extends DataItem<Integer> {

    String getDescription();

    Genre getGenre();

    ImageImpl getImage();

    Boolean getIsSerie();

    String getLink();

    String getName();

    int getSeasonsNumber();
    
    public ArrayList getEpisodes();
    

    void setDescription(String description);

    void setGenre(Genre genre);

    void setImage(ImageImpl image);

    void setIsSerie(Boolean isSerie);

    void setLink(String link);

    void setName(String name);

    void setSeasonsNumber(int seasonsNumber);
    
    void setEpisodes(ArrayList episodes);
    
    
}

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
import java.util.List;

/**
 *
 * @author giorg
 */
public interface Program extends DataItem<Integer> {

    String getDescription();

    Genre getGenre();

    Image getImage();

    Boolean IsSerie();

    String getLink();

    String getName();

    Integer getSeasonsNumber();
    
    public List<Episode> getEpisodes();
    

    void setDescription(String description);

    void setGenre(Genre genre);

    void setImage(Image image);

    void setIsSerie(Boolean isSerie);

    void setLink(String link);

    void setName(String name);

    void setSeasonsNumber(Integer seasonsNumber);
    
    void addEpisode(Episode episode);
    
    void setEpisodes(List<Episode> episodes);
    
    
}

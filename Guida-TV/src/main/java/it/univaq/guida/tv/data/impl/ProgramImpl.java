/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guida.tv.data.impl;

import it.univaq.guida.tv.data.model.Program;
import java.util.ArrayList;

/**
 *
 * @author giorg
 */
public class ProgramImpl implements Program {
    
    private Integer id;
    private String name;
    private String description;
    private Genre genre;
    private String link;
    private ImageImpl image;
    private Boolean isSerie;
    private int seasonsNumber;
    
    private ArrayList episodes = new ArrayList<>();
    
    public enum Genre {
    	
    	informazione, cultura, fiction, intrattenimento, giocoTelevisivo, horror, avventura, crime, romantico; 
    
    }
    
    
     public ProgramImpl() {
        super();
        id = null;
        name = "";
        description = "";
        genre = null;
        link = "";
        image = null;
        isSerie = false;
        seasonsNumber = 0;
        
        episodes = null;
    }
    
    @Override
     public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Genre getGenre() {
        return genre;
    }

    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public ImageImpl getImage() {
        return image;
    }

    @Override
    public void setImage(ImageImpl image) {
        this.image = image;
    }

    @Override
    public Boolean getIsSerie() {
        return isSerie;
    }

    @Override
    public void setIsSerie(Boolean isSerie) {
        this.isSerie = isSerie;
    }

    @Override
    public int getSeasonsNumber() {
        return seasonsNumber;
    }

    @Override
    public void setSeasonsNumber(int seasonsNumber) {
        this.seasonsNumber = seasonsNumber;
    }
    
    @Override
    public ArrayList getEpisodes() {
        return episodes;
    }

    @Override
    public void setEpisodes(ArrayList episodes) {
        this.episodes = episodes;
    }
    
    
    
    
}

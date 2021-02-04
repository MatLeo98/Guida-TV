/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guidatv.data.dao.ImageDAO;
import it.univaq.guidatv.data.impl.ProgramImpl;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Image;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author giorg
 */
public class ProgramProxy  extends ProgramImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected int image_key;
    
    public ProgramProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }
    
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }
    
    @Override
    public void addEpisode(Episode episode) {
        super.addEpisode(episode);
        this.modified = true;
    }

    @Override
    public void setEpisodes(List<Episode> episodes) {
        super.setEpisodes(episodes); 
        this.modified = true;
    }

    @Override
    public void setSeasonsNumber(Integer seasonsNumber) {
        super.setSeasonsNumber(seasonsNumber);
        this.modified = true;
    }


    @Override
    public void setImage(Image image) {
        super.setImage(image); 
        this.modified = true;
    }
    
    @Override
    public Image getImage() {
       
        if (super.getImage() == null && image_key > 0) {
            try {
                super.setImage(((ImageDAO) dataLayer.getDAO(Image.class)).getImage(image_key));
            } catch (DataException ex) {
                Logger.getLogger(ProgramProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        return super.getImage();
    }

    @Override
    public void setLink(String link) {
        super.setLink(link); 
        this.modified = true;
        
    }

    @Override
    public void setGenre(Genre genre) {
        super.setGenre(genre);
        this.modified = true;
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description); 
        this.modified = true;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.modified = true;
    }

   
    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }

    public void setImageKey(int key) {
        this.image_key = key;
        
    }
    
}

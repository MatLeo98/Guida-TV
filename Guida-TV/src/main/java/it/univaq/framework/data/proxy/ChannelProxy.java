/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guida.tv.data.dao.ImageDAO;
import it.univaq.guida.tv.data.impl.ChannelImpl;
import it.univaq.guida.tv.data.model.Image;

/**
 *
 * @author Matteo
 */
public class ChannelProxy extends ChannelImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    
    public ChannelProxy(DataLayer d) {
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
    public void setImage(Image image) {
        super.setImage(image);
        this.modified = true;
    }

    @Override
    public Image getImage() {
        if (super.getImage() == null) {
            super.setImage(((ImageDAO) dataLayer.getDAO(Image.class)).getChannelImage(this));
        }
        return super.getImage();
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
    
    public void setImageKey() {
        super.setImage(null);
    }
    
}

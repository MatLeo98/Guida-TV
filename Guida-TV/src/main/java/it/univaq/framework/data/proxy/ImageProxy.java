/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guida.tv.data.impl.ImageImpl;
import java.io.InputStream;

/**
 *
 * @author giorg
 */
public class ImageProxy extends ImageImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public ImageProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;

    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);
        this.modified = true;
    }

    @Override
    public void setImageData(InputStream is) throws DataException {
        super.setImageData(is);
        this.modified = true;
    }

    @Override
    public void setImageType(String type) {
        super.setImageType(type);
        this.modified = true;
    }

    @Override
    public void setImageSize(long size) {
        super.setImageSize(size);
        this.modified = true;
    }

    @Override
    public void setImageFilename(String imageFilename) {
        super.setImageFilename(imageFilename);
        this.modified = true;
    }

    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
    
}

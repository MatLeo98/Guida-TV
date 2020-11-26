/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataItemProxy;
import it.univaq.guida.tv.data.impl.ProgramImpl;

/**
 *
 * @author giorg
 */
public class ProgramProxy  extends ProgramImpl implements DataItemProxy{
    
    protected boolean modified;
    
    
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
    
}

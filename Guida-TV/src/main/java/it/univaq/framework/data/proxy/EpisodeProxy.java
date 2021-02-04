/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.data.proxy;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataItemProxy;
import it.univaq.framework.data.DataLayer;
import it.univaq.guidatv.data.dao.ProgramDAO;
import it.univaq.guidatv.data.impl.EpisodeImpl;
import it.univaq.guidatv.data.model.Program;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorg
 */
public class EpisodeProxy extends EpisodeImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected int program_key = 0;
    
    public EpisodeProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
         this.program_key = 0;
    }
    
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }
    

    @Override
    public Program getProgram() {
       
        if (super.getProgram() == null && program_key > 0) {
            try {
                super.setProgram(((ProgramDAO) dataLayer.getDAO(Program.class)).getProgram(program_key));
            } catch (DataException ex) {
                Logger.getLogger(EpisodeProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        return super.getProgram();
    }

    @Override
    public void setProgram(Program program) {
        super.setProgram(program);
        this.program_key = program.getKey();
        this.modified = true;
    }

    @Override
    public void setNumber(Integer number) {
        super.setNumber(number);
        this.modified = true;
    }

    @Override
    public void setSeasonNumber(Integer seasonNumber) {
        super.setSeasonNumber(seasonNumber); 
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
    
    public void setProgramKey(int program_key) {
        this.program_key = program_key;
        //resettiamo la cache del programma
        super.setProgram(null);
    }
    
}

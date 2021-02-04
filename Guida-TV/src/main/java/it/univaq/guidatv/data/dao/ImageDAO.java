/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.guidatv.data.dao;

import it.univaq.framework.data.DataException;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Image;
import it.univaq.guidatv.data.model.Program;
import java.util.List;

/**
 *
 * @author giorg
 */
public interface ImageDAO {
    
    Image createImage();

    Image getImage(int image_key) throws DataException;

    Image getProgramImage(Program program) throws DataException;

    Image getChannelImage(Channel channel) throws DataException;
    
    Image storeImage (Image image) throws DataException;
    
}

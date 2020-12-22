/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.Channel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matteo
 */

public class Edit extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
            action_edit(request, response);
        } catch (DataException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    private void action_edit(HttpServletRequest request, HttpServletResponse response){       
        response.setContentType("text/html;charset=UTF-8");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Edit</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Modifica un canale: </h1>");
            for(Channel c : channels){
            out.println("<button>" + c.getName() + "</button>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

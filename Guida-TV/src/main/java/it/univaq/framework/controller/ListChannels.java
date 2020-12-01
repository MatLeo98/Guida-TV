/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giorg
 */
public class ListChannels extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        
        int k;
        
        try {
            k = 1;
            
            //request.setAttribute("episode", ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(k));
            request.setAttribute("channel", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(k));
            action_channel(request, response, k);
           
        
            
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Article key not specified");
            
        } catch (DataException ex) { 
            Logger.getLogger(ListChannels.class.getName()).log(Level.SEVERE, null, ex);
        }
    
       
       
       
    }

    private void action_channel(HttpServletRequest request, HttpServletResponse response, int k) {
        
        //Episode episode = (Episode) request.getAttribute("episode");
        Channel channel = (Channel) request.getAttribute("channel");
        /* TODO output your page here. You may use following sample code. */
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Lista</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Lista at " + request.getContextPath() + "</h1>");
            out.println("<h1>Canale " + channel.getName() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(ListChannels.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_program(HttpServletRequest request, HttpServletResponse response){
    
        response.setContentType("text/html;charset=UTF-8");
        
        
    }
}

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    



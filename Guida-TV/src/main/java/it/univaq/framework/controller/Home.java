/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Schedule;
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
 * @author giorg
 */
public class Home extends BaseController {

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
        
        try {    
            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
            request.setAttribute("onAirPrograms", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getOnAirPrograms());
            action_home(request, response);                 
            
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Home key not specified");           
        } catch (DataException ex) { 
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }

    private void action_home(HttpServletRequest request, HttpServletResponse response) {
        Genre[] genres = Genre.values();
        List<Schedule> onAirPrograms = (List<Schedule>) request.getAttribute("onAirPrograms");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Home</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<form action='searchresults'>");
            out.println("<input type='text' placeholder='Titolo' name='title'>");
            out.println("<select name='genre' id='genre'>");
            out.println("<option value = ''> Scegli il genere </option>");
            for(Genre g : genres){
            out.println("<option value = '" + g + "'>" + g + "</option>");
            }
            out.println("</select>");
            out.println("<select name='channel' id='channel'>");
            out.println("<option value = ''> Scegli i canali </option>");
            for(Channel c : channels){
            out.println("<option value = '" + c.getName() + "'>" + c.getName() + "</option>");
            }
            out.println("</select>");
            out.println("<button type='submit'>Search</button>");
            out.println("</form>");
            out.println("<h1>Programmi in onda </h1>");
            for(Schedule s : onAirPrograms){
            out.println("<h3> Ora in onda: <a href = 'program?id=" + s.getProgram().getKey() + "'>" + s.getProgram().getName() + "</a></h3>");
            out.println("<h1> <a href = 'channel?id=" + s.getChannel().getKey()+ "'>" + s.getChannel().getName() + " </a></h1>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    



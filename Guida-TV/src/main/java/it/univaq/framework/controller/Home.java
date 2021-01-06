/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
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
           TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            //add to the template a wrapper object that allows to call the stripslashes function
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
            request.setAttribute("onAirPrograms", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getOnAirPrograms());
            res.activate("home.ftl.html", request, response);
            //action_home(request, response);                 
            
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Home key not specified");           
        } catch (DataException ex) { 
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
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
            out.println("Ora Min:");
            out.println("<input type='time' id='min' name='min'>");
            out.println("Ora Max:");
            out.println("<input type='time' id='max' name='max'>");
            out.println("Da:");
            out.println("<input type='date' id='date1' name='date1'>");
            out.println("A:");
            out.println("<input type='date' id='date2' name='date2'>");
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
    



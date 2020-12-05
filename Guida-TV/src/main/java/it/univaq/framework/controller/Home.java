/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.Schedule;
import it.univaq.guida.tv.data.model.User;
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
           
            request.setAttribute("schedule", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(1));
            request.setAttribute("onAirPrograms", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getOnAirPrograms());
            action_channel(request, response);                 
            
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Article key not specified");           
        } catch (DataException ex) { 
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }

    private void action_channel(HttpServletRequest request, HttpServletResponse response) {
        
        //Episode episode = (Episode) request.getAttribute("episode");
        List<Schedule> onAirPrograms = (List<Schedule>) request.getAttribute("onAirPrograms");
        Schedule schedule = (Schedule) request.getAttribute("schedule");
        /* TODO output your page here. You may use following sample code. */
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Home</title>");            
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Servlet Home at " + request.getContextPath() + "</h1>");
            for(Schedule s : onAirPrograms){
            out.println("<h1>" + s.getChannel().getName() + "</h1>");
            out.println("<h3> Ora in onda: " + s.getProgram().getName() + "</h3>");
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
    



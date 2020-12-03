/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Schedule;
import java.io.IOException;
import java.io.PrintWriter;
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
public class ScheduleList extends BaseController {

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
           
            System.out.println("ciao");
            //request.setAttribute("schedule", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(1));
            request.setAttribute("schedules", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getTodaySchedule());
            action_schedule(request, response);
           
           
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Article key not specified");
            
        } catch (DataException ex) { 
            Logger.getLogger(ScheduleList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void action_schedule(HttpServletRequest request, HttpServletResponse response) {
        List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
        //Schedule schedule = (Schedule) request.getAttribute("schedule");
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
            out.println("<h1>Canale " + schedules.get(0).getChannel().getName() + "</h1>");
            out.println("<p>Episodio numero: "+ schedules.get(0).getEpisode().getNumber() +" - " +schedules.get(0).getEpisode().getName() +"</p>");
            out.println("<p> Ora inizio: " + schedules.get(0).getStartTime().getTime() + "</p>");
            //out.println("<h1>Canale " + schedule.getChannel().getName() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

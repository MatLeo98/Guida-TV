/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Schedule;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
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
            
            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
            
            if (request.getParameter("tsSelect") == null){
                //TimeSlot timeSelected = TimeSlot.valueOf("sera");
                request.setAttribute("nowtimeslot", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getCurTimeSlot());
                request.setAttribute("schedules", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getTodaySchedule((TimeSlot)request.getAttribute("nowtimeslot")));
                action_schedule(request, response);
            }else{
                
                TimeSlot timeSelected = TimeSlot.valueOf(request.getParameter("tsSelect"));
                request.setAttribute("schedules", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getTodaySchedule(timeSelected));
                action_schedule(request, response);
            }
            //request.setAttribute("schedule", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(1));
            
            
           
           
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Article key not specified");
            
        } catch (DataException ex) { 
            Logger.getLogger(ScheduleList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void action_schedule(HttpServletRequest request, HttpServletResponse response) {
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
        TimeSlot[] timeslots = TimeSlot.class.getEnumConstants(); //VA FATTO IN UN METODO CREDO
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
            out.println(" <form method=\"get\" action=\"schedules\">");
            out.println("<label for=\"tsSelect\">Scegli una fascia oraria:</label>");
            out.println("<select name=\"tsSelect\" id=\"tsSelect\">");
            for(TimeSlot timeslot : timeslots){
                out.println("<option value=\""+timeslot+"\">"+timeslot.toString()+"</option>");
            }
            out.println("</select>");
            out.println("<br><br>");
            out.println("<input type=\"submit\" name=\"s\"/>");
            out.println("</form>");
            
           // out.println("<h1>Servlet Lista at " + request.getContextPath() + "</h1>");
            for(Channel c : channels){
                
                out.println("<h1>Canale: <a href = 'channel?id=" + c.getKey() + "'>" + c.getName() + " </a> </h1>");
            
                for(Schedule s : schedules){
                    if( s.getChannel().getName().equals(c.getName())){
                        //out.println("<h1>Canale: " + s.getChannel().getName() + "</h1>");
                        out.println("<p>Programma: "+ s.getProgram().getName() +"</p>");
                        if(s.getEpisode() != null){
                            out.println("<p>Episodio numero: "+ s.getEpisode().getNumber() + " - "+ s.getEpisode().getName() +"</p>");
                        }
                        
                       
                        //String currentDate = Date.format(cals.getTime());
                        out.println("<p> Data: " + s.getDate() + "</p>");
                        out.println("<p> Ora inizio: " + s.getStartTime() + "</p>");
                        out.println("<p> Ora fine: " + s.getEndTime() + "</p>");
                        out.println("<br>");
                    }
                }
            }
            //out.println("<h1>Canale " + schedule.getChannel().getName() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

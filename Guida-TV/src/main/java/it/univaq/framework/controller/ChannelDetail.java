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
import java.time.LocalDate;
import java.util.Date;
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
public class ChannelDetail extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    LocalDate date = LocalDate.parse("2020-12-06");
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        
        try {
            
            Channel channel = ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(1);
            request.setAttribute("channel", channel);           
            request.setAttribute("schedule", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByChannel(channel, date));
        } catch (DataException ex) {
            Logger.getLogger(ChannelDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        action_channel(request, response);
        
    }
    
    private void action_channel(HttpServletRequest request, HttpServletResponse response){
        Channel channel = (Channel) request.getAttribute("channel");
        List<Schedule> schedule = (List<Schedule>) request.getAttribute("schedule");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChannelDetail</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChannelDetail at " + request.getContextPath() + "</h1>");
            out.println("<h1>" + channel.getName() + "</h1>");
            out.println("<h2>Palinsesto del " + date + "</h2>");
            for(Schedule s : schedule){
            out.println("<h3>" + s.getProgram().getName() + "</h3>");
            out.println("<h3>Ora di inizio: " + s.getStartTime() + "</h3>");
            out.println("<h3>Ora di fine: " + s.getEndTime() + "</h3>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
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
     * Handles the HTTP <code>POST</code> method.
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

}

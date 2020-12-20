/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.Schedule;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
 * @author Matteo
 */
public class SearchResults extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try{
            String tit = request.getParameter("title");
            String gen = request.getParameter("genre");
            String ch = request.getParameter("channel");
            String date1 = request.getParameter("date1");
            String date2 = request.getParameter("date2");
            String min = request.getParameter("min");
            String max = request.getParameter("max");

            request.setAttribute("search", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().search(tit, gen, ch, min, max, date1, date2));
        action_results(request, response);
        }catch (NumberFormatException ex) {
            request.setAttribute("message", "Home key not specified");           
        } catch (DataException ex) { 
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_results(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Schedule> search = (List<Schedule>) request.getAttribute("search");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SearchResults</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<button type='submit'>Salva questi criteri di ricerca</button>");
            out.println("<h1>RISULTATI:</h1>");
            for(Schedule s : search){
            out.println("<h3><a href = 'program?id=" + s.getProgram().getKey() + "'>" + s.getProgram().getName() + "</a></h3>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(SearchResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

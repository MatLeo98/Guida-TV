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
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giorg
 */

public class ProgramDetail extends BaseController {

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
            throws ServletException {
        
        try {
            
            request.setAttribute("program", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(1));
            Program program = (Program) request.getAttribute("program");
            if(program.getIsSerie()){
                request.setAttribute("episodes", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getLastMonthEpisodes(program));
                serie_detail(request, response);
            }else
                program_detail(request, response);
            
        } catch (DataException ex) {
            Logger.getLogger(ProgramDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private void program_detail(HttpServletRequest request, HttpServletResponse response) {
        
        Program program = (Program) request.getAttribute("program");
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProgramDetail</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> " + program.getName() + "</h1>");
            out.println("<p>" + program.getDescription() + "</p>");
            out.println("<p> Genere: " + program.getGenre() + "</p>");
            out.println("<a href=\"" + program.getLink() + "\"> Approfondisci  </a>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (IOException ex) {
            Logger.getLogger(ProgramDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void serie_detail(HttpServletRequest request, HttpServletResponse response) {
        
        Program program = (Program) request.getAttribute("program");
        List<Schedule> episodes = (List<Schedule>) request.getAttribute("episodes");
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProgramDetail</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> " + program.getName() + "</h1>");
            out.println("<p>" + program.getDescription() + "</p>");
            out.println("<p> Genere: " + program.getGenre() + "</p>");
            out.println("<a href=\"" + program.getLink() + "\"> Approfondisci  </a>");
            out.println("<h2> Episodi dell'ultimo mese </h2>");
            for(Schedule s : episodes){
                out.println("<h3> Episodio numero " + s.getEpisode().getNumber() + " - " + s.getEpisode().getName() + "</h3>");
                out.println("<p> Andato in onda: " + s.getDate() + " dalle ore: " + s.getStartTime() + " alle ore "+ s.getEndTime() + "</p>");
            }
            out.println("</body>");
            out.println("</html>");
            
        } catch (IOException ex) {
            Logger.getLogger(ProgramDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Program;
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

public class Insert extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(request.getParameter("channel") != null){
            if(request.getParameter("channelNumber") == null){
                System.out.println(request.getParameter("channelNumber"));
                channel_insert(request, response);
            }else{
                insert_done(request, response);
            }
        }
            
        if(request.getParameter("program") != null){
            if(request.getParameter("programName") == null){
                program_insert(request, response);
            }else{
                insert_done(request, response);
            }
        }
                
        if(request.getParameter("episode") != null){
            if(request.getParameter("episodeName") == null){
                try {
                    request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                    episode_insert(request, response);
                } catch (DataException ex) {
                    Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
                  }
            }else{
                insert_done(request, response);
            }                
        }
        
        if(request.getParameter("schedule") != null){
            if(request.getParameter("pr") == null){
                try {
                    request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                    request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                    //request.setAttribute("episodes", ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes());
                    schedule_insert(request, response);
                } catch (DataException ex) {
                    Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }else{
                insert_done(request, response);
            } 
        }    
               
    }
    
    private void channel_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");        
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo canale: </h1>");
            out.println("<form method=\"post\" action=\"insert?channel=1\">");

 out.println("<input type=\"text\" id=\"channelNumber\" name=\"channelNumber\" placeholder=\"Numero Canale\"/>");

 out.println("<br><br>");

 out.println("<input type=\"text\" id=\"channelName\" name=\"channelName\" placeholder=\"Nome Canale\"/>");

 out.println("<br><br>");


 out.println("<input type=\"submit\" name=\"crea\" value=\"CREA\"/>");

 out.println("<br><br>");

 out.println("<a href=\"admin\"> Torna a admin </a>");

 out.println(" </center>");

 out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void program_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");      
        ProgramImpl.Genre[] genres = ProgramImpl.Genre.values();
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo programma: </h1>");
            out.println("<form method='post' action='insert?program=1'>");            
            out.println("<input type='text' placeholder='Nome canale' name='programName'>");
            out.println("<input type='text' placeholder='Descrizione' name='programDescription'>");
            out.println("<select name='genre' id='genre'>");
            for(ProgramImpl.Genre g : genres){
            out.println("<option value = '" + g + "'>" + g + "</option>");
            }
            out.println("</select>");
            out.println("<input type='text' placeholder='Link' name='link'>");
            out.println("<select name='serie' id='serie'>");
            out.println("<option value = ''> È una serie? </option>");
            out.println("<option value = '0'> No </option>");
            out.println("<option value = '1'> Si </option>");
            out.println("</select>");
            out.println("<input type='text' placeholder='Numero stagioni' name='nSeasons'>");
            out.println("<button type='submit'>Crea</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void episode_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo episodio: </h1>");
            out.println("<form method='post' action='insert?episode=1'>");
            out.println("<input type='text' placeholder='Nome episodio' name='episodeName'>");
            out.println("<input type='text' placeholder='Numero stagione' name='seasonNumber'>");
            out.println("<input type='text' placeholder='Numero episodio' name='episodeNumber'>");
            out.println("<select name='p' id='p'>");
            for(Program p : programs){
            out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");
            }
            out.println("</select>");
            out.println("<button type='submit'>Crea</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void schedule_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");   
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo palinsesto: </h1>");
            out.println("<form method='post' action='insert?schedule=1'>");
            out.println("<select name='pr' id='pr'>");
            for(Program p : programs){
            out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");
            }
            out.println("</select>");           
            out.println("<select name='ch' id='ch'>");
            for(Channel c : channels){
            out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
            }
            out.println("</select>");
            //VA IMPLEMENTATO IL METODO GETPROGRAMEPISODES() IN EPISODEDAO PER PRENDERE GLI EPISODI DEL PROGRAMMA CHE SELEZIONO
            out.println("Ora inizio:");
            out.println("<input type='time' id='start' name='start'>");
            out.println("Ora fine:");
            out.println("<input type='time' id='end' name='end'>");
            out.println("Data:");
            out.println("<input type='date' id='date' name='date'>");
            out.println("<button type='submit'>Crea</button>");
            out.println("</form>");*/
           out.println("<form method=\"post\" action=\"insert?channel=1\">");
                out.println("<input type=\"text\" id=\"channelnumber\" name=\"channelnumber\" placeholder=\"Numero Canale\"/>");
                out.println("<br><br>");
                out.println("<input type=\"text\" id=\"channelname\" name=\"channelname\" placeholder=\"Nome Canale\"/>");
                out.println("<br><br>");
                
                out.println("<input type=\"submit\" name=\"crea\" value=\"CREA\"/>");
                out.println("<br><br>");
                out.println("<a href=\"admin\"> Torna a admin </a>");
            out.println(" </center>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insert_done(HttpServletRequest request, HttpServletResponse response){

            try (PrintWriter out = response.getWriter()){
                if(request.getParameter("channel") != null){
                    Integer n = Integer.parseInt(request.getParameter("channelNumber"));
                    ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().storeChannel(n, request.getParameter("channelName"));
                }
                if(request.getParameter("program") != null){
                    Integer ns = Integer.parseInt(request.getParameter("nSeasons"));
                    Boolean is = (Integer.parseInt(request.getParameter("serie")) == 1);
                    ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().storeProgram(request.getParameter("programName"), request.getParameter("programDescription"), request.getParameter("genre"), request.getParameter("link"), is , ns);
                }
                if(request.getParameter("episode") != null){                   
                    Integer ns = Integer.parseInt(request.getParameter("seasonNumber"));
                    Integer ne = Integer.parseInt(request.getParameter("episodeNumber"));
                    Integer p = Integer.parseInt(request.getParameter("p"));
                   ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().storeEpisode(request.getParameter("episodeName"), ns, ne, p); 
                }
                if(request.getParameter("schedule") != null){    
                    Integer p = Integer.parseInt(request.getParameter("pr"));
                    Integer c = Integer.parseInt(request.getParameter("ch"));
                   ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().storeSchedule(c, p, request.getParameter("start") , request.getParameter("end") , request.getParameter("date")); 
                }
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet Insert</title>"); 
                out.println("</head>");
                out.println("<body>");
                out.println("<h1> Inserimento effettuato </h1>");
            } catch (DataException ex) {
                Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

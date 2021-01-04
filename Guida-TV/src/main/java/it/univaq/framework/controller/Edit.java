/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
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

public class Edit extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //channel
        if(request.getParameter("channel") != null){
            if(request.getParameter("channelNumber") == null){
                if(request.getParameter("ch") != null){ 
                    try { 
                        Integer id = Integer.parseInt(request.getParameter("ch"));
                        request.setAttribute("channelSelected", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(id));
                        } catch (DataException ex) {
                        Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                        try {
                            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                            channel_edit(request, response);
                        } catch (DataException ex) {
                            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                
            }else{
                edit_done(request, response);
            }
        }  
        //program
        if(request.getParameter("program") != null){
            int program_key;           
                if(request.getParameter("prog") == null){
                                  
                    if(request.getParameter("pr") != null){ 
                        try { 
                            Integer id = Integer.parseInt(request.getParameter("pr"));
                            request.setAttribute("programSelected", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(id));
                            } catch (DataException ex) {
                            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                            }      
                    }
                            try {
                                request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                                program_edit(request, response);
                            } catch (DataException ex) {
                                Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                            }


                }else{
                    program_key = SecurityLayer.checkNumeric(request.getParameter("prog"));
                    request.setAttribute("key", program_key);
                    edit_done(request, response);
                }
            
        }
        //episode
        if(request.getParameter("episode") != null){
            int episode_key;    
            System.out.println(request.getParameter("sn"));
                if(request.getParameter("sn") == null){
                            try {
     
                                if(request.getParameter("pr") != null){
                                    try {
                                    Integer id = Integer.parseInt(request.getParameter("pr"));
                                    Program pro = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(id);
                                    
                                    request.setAttribute("programSelected", pro);
                                    request.setAttribute("episodes", ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes(pro));
                                    } catch (DataException ex) {
                                    Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                //try {
                                
                                    request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                                    
                                    episode_edit(request, response);
                                /*} catch (DataException ex) {
                                    Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                                }*/
                                
                                
                            } catch (DataException ex) {
                                Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                            }


                }else{
                    episode_key = SecurityLayer.checkNumeric(request.getParameter("ek"));
                    request.setAttribute("key", episode_key);
                    edit_done(request, response);
                }
            
        }
    }
    
    private void channel_edit(HttpServletRequest request, HttpServletResponse response){       
        response.setContentType("text/html;charset=UTF-8");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        Channel channelSelected = (Channel) request.getAttribute("channelSelected");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Edit</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Modifica un canale: </h1>");
            out.println("<form method='post' action='edit?channel=1'>");
            out.println("Canale da modificare:");
            out.println("<select name='ch' id='ch'>");
            for(Channel c : channels){
            out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
            }
            out.println("</select>");
            out.println("<input type='submit' name='select' value='SELEZIONA'/>");
            out.println("</form>");
            out.println("<br><br>");
            out.println("<form method='post' action='edit?channel=1'>");
            out.println("<input type='text' id='channelNumber' name='channelNumber' placeholder='Numero Canale' value ='" + channelSelected.getKey() + "' readonly />");
            out.println("<br><br>");
            out.println("<input type='text' id='channelName' name='channelName' placeholder='Nome Canale' value ='" + channelSelected.getName() + "'/>");
            out.println("<br><br>");
            out.println("<input type='submit' name='edit' value='MODIFICA'/>");
            out.println("<br><br>");
            out.println("<a href='admin'> Torna a admin </a>");
            out.println(" </center>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void program_edit(HttpServletRequest request, HttpServletResponse response){       
        response.setContentType("text/html;charset=UTF-8");      
        ProgramImpl.Genre[] genres = ProgramImpl.Genre.values();
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        Program programSelected = (Program) request.getAttribute("programSelected");
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Edit</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Modifica un programma: </h1>");
            out.println("<form method='post' action='edit?program=1'>");
            out.println("Programma da modificare:");           
            out.println("<select name='pr' id='pr'>");
            for(Program p : programs){
            out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");
            }
            out.println("</select>");
            out.println("<input type='submit' name='select' value='SELEZIONA'/>");
            out.println("</form>");
            out.println("<form method='post' action='edit?program=1'>");
            if(programSelected != null){
            out.println("<input type='text' name='prog' value='" + programSelected.getKey() + "' hidden/>");
            out.println("<input type='text' placeholder='Nome canale' name='programName' value='" + programSelected.getName() + "'>");
            out.println("<input type='text' placeholder='Descrizione' name='programDescription' value='" + programSelected.getDescription() + "'>");
            out.println("<select name='genre' id='genre'>");
            out.println("<option value = '" + programSelected.getGenre() + "'>" + programSelected.getGenre() + "</option>");
            for(ProgramImpl.Genre g : genres){
                if(!(g.equals(programSelected.getGenre())))
                  out.println("<option value = '" + g + "'>" + g + "</option>");
            }
            out.println("</select>");
            out.println("<input type='text' placeholder='Link' name='link' value='" + programSelected.getLink() + "'>");
            out.println("È una serie?"); 
            if(programSelected.IsSerie()){
                out.println("<input type='radio' name='serie' value='0'> No");
                out.println("<input type='radio' name='serie' value='1' checked> Sì");
                out.println("<input type='text' placeholder='Numero stagioni' name='nSeasons' value='" + programSelected.getSeasonsNumber() + "'>");

            }else{
                out.println("<input type='radio' name='serie' value='0' checked> No");
                out.println("<input type='radio' name='serie' value='1'> Sì");  
            }
            out.println("<button type='submit'>Modifica</button>");
            }
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void episode_edit(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        List<Episode> episodes = (List<Episode>) request.getAttribute("episodes");
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Edit</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Modifica un episodio: </h1>");
            out.println("<form method='post' action='edit?episode=1'>");
            out.println("Scegli la serie:");           
            out.println("<select name='pr' id='pr'>");
            for(Program p : programs){
                if(p.IsSerie())
            out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");     
            }
            out.println("</select>");
            out.println("<input type='submit' name='select' value='SELEZIONA'/>");
            out.println("</form>");
            out.println("<table style='width:100%'>");
                out.println("<tr>");
                  out.println("<th>Numero stagione</th>");
                  out.println("<th>Numero episodio</th>");
                  out.println("<th>Nome episodio</th>");                   
                  out.println("<th>Modifica</th>");
                  out.println("<th>Elimina</th>");
                out.println("</tr>");
                if(episodes != null){
                    for(Episode e : episodes){
                        out.println("<tr>");
                        
                            out.println("<form method='post' action='edit?episode=1'>");
                             out.println("<input type='text' name='ek' value='" + e.getKey() + "' hidden/>"); 
                             out.println("<td style='text-align:center'> <input type='text' name='sn' value='" + e.getSeasonNumber() + "'/> </td>");
                             out.println("<td style='text-align:center'> <input type='text' name='en' value='" + e.getNumber() + "'/> </td>"); 
                             out.println("<td style='text-align:center'> <input type='text' name='nm' value='" + e.getName() + "'/> </td>");
                             
                            out.println("<td style='text-align:center'>");
                             out.println("<input type='submit' name='edit' value='MODIFICA'/>");
                             out.println("</form>");
                            out.println("</td>");
                             
                           out.println("<td style='text-align:center'>");  
                            out.println("<form method='post' action='edit?delEp=" +e.getKey()+"'>");
                            out.println("<input type='submit' name='delete' value='ELIMINA'/>");
                            out.println("</form>");
                           out.println("</td>");
                           
                        out.println("</tr>");
                    }
                }
            out.println("</table>");
            /*out.println("</form>");
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
            out.println("</form>");*/
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void edit_done(HttpServletRequest request, HttpServletResponse response){
        try (PrintWriter out = response.getWriter()){
        if(request.getParameter("channel") != null){
            Integer n = Integer.parseInt(request.getParameter("channelNumber"));
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().storeChannel(n, request.getParameter("channelName"));
        }
        if(request.getParameter("program") != null){
            int key = (int)request.getAttribute("key");
            Program program = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(key);
            program.setName(request.getParameter("programName"));
            program.setDescription(request.getParameter("programDescription"));
            program.setGenre(Genre.valueOf(request.getParameter("genre")));
            program.setLink(request.getParameter("link"));
            program.setIsSerie(Boolean.valueOf(request.getParameter("serie")));
            if(program.IsSerie()){
            program.setSeasonsNumber(Integer.parseInt(request.getParameter("nSeasons")));
            }
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().storeProgram(program);
        }
        if(request.getParameter("episode") != null){
            int key = (int)request.getAttribute("key");
            Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(key);
            episode.setName(request.getParameter("nm"));
            episode.setSeasonNumber(Integer.parseInt(request.getParameter("sn")));
            episode.setNumber(Integer.parseInt(request.getParameter("en")));
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().storeEpisode(episode);
        }
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Insert</title>"); 
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Modifica effettuata </h1>");
        } catch (IOException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

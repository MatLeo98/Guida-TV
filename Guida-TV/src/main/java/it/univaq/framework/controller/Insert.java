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
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import it.univaq.guida.tv.data.model.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Matteo
 */

public class Insert extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);
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
            //if(s.getAttribute("programSelected") == null){
            if(s.getAttribute("channelSelected") == null){
                try{
                    //if(request.getParameter("pr") != null){
                        if(request.getParameter("ch") != null){
                            try {
                                /*Integer id = Integer.parseInt(request.getParameter("pr"));
                                Program pro = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(id);
                                request.setAttribute("programSelected", pro);
                                s.setAttribute("programSelected", pro);*/
                                Integer id = Integer.parseInt(request.getParameter("ch"));
                                Channel cha = ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(id);
                                request.setAttribute("channelSelected", cha);
                                s.setAttribute("channelSelected", cha);
                                request.setAttribute("episodes", ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getAllEpisodes());
                                //request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                                request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                                
                            } catch (DataException ex) {
                            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
                            }        
                    }
                    
                    request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                   
                    schedule_insert(request, response);
                }catch (DataException ex) {
                                Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                insert_done(request, response);
            } 
        }    
               
    }
    
    private void channel_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8"); 

        try {        
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inseriscicanale.ftl.html", request, response);
            
            /*
            try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo canale: </h1>");
            out.println("<form method='post' action='insert?channel=1'>");
            out.println("<input type='text' id='channelNumber' name='channelNumber' placeholder='Numero Canale'/>");
            out.println("<br><br>");
            out.println("<input type='text' id='channelName' name='channelName' placeholder='Nome Canale'/>");
            out.println("<br><br>");
            out.println("<input type='submit' name='crea' value='CREA'/>");
            out.println("<br><br>");            
            out.println(" </center>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private void program_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");      
        ProgramImpl.Genre[] genres = ProgramImpl.Genre.values();
       
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inseriscicanale.ftl.html", request, response);
            
            /*
            try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo programma: </h1>");
            out.println("<form method='post' action='insert?program=1'>");            
            out.println("<input type='text' placeholder='Nome programma' name='programName'>");
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
            */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void episode_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inserisciepisodio.ftl.html", request, response);
            /*
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
            if(p.IsSerie())
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
            
            */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void schedule_insert(HttpServletRequest request, HttpServletResponse response) throws DataException{
        response.setContentType("text/html;charset=UTF-8");   
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        //Program programSelected = (Program) request.getAttribute("programSelected");
         Channel channelSelected = (Channel) request.getAttribute("channelSelected");
        

        try {       
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            //if(programSelected != null){
                if(channelSelected != null){
                //List<Episode> episodes = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes(programSelected);
                //request.setAttribute("episodes", episodes);                    
                if(request.getParameter("ln") != null){
                    int ln = Integer.parseInt(request.getParameter("ln"));
                    request.setAttribute("lNum", ln);
                    List<Integer> rows = new ArrayList();
                    for(int i = 1; i <= ln; i++){
                        rows.add(i);
                    }
                    request.setAttribute("rows", rows);                   
                }
                res.activate("inseriscipalinsesto.ftl.html", request, response);
            }else{
                res.activate("inseriscipalinsestoparz.ftl.html", request, response); 
            }
            
            /*
            try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo palinsesto: </h1>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin'> Torna alla pagina di gestione </a>");
            out.println("<br>");
            out.println("<form method='post' action='insert?schedule=1'>");
            out.println("Scegli il programma:");
            out.println("<select name='pr' id='pr'>");
            if(programSelected != null){
               out.println("<option value = '" + programSelected.getKey() + "'>" + programSelected.getName() + "</option>");          
                for(Program p : programs){
                    if(!(p.getName().equals(programSelected.getName())))
                out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");       
                }
            }else{
                for(Program p : programs){
                out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");       
                }
            }
              out.println("</select>");
              out.println("Quanti elementi vuoi inserire?");
              out.println("<input type='text' id='ln' name='ln'>");
              out.println("<input type='submit' name='select' value='SELEZIONA'/>");
            out.println("</form>");
            out.println("<form method='post' action='insert?schedule=1'>");
            int ln = Integer.parseInt(request.getParameter("ln"));
            for(int i = 1; i <= ln; i++){
            if(programSelected.IsSerie()){
                List<Episode> episodes = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes(programSelected);
                out.println("Episodio:");
                out.println("<select name='ep " + i + "' id='ep'>");
                for(Episode e : episodes){
                    out.println("<option value = '" + e.getKey() + "'>" + e.getName() + "</option>");
                }
                out.println("</select>");
            }
            out.println("<input type='text' id='nElem' name='nElem' value='" + ln + "' hidden >");
            out.println("Canale:");
            out.println("<select name='ch" + i + "' id='ch'>");
            for(Channel c : channels){
            out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
            }
            out.println("</select>");
            out.println("Ora inizio:");
            out.println("<input type='time' id='start' name='start" + i + "'>");
            out.println("Ora fine:");
            out.println("<input type='time' id='end' name='end" + i + "'>");
            out.println("Data:");
            out.println("<input type='date' id='date' name='date" + i + "'>");
            out.println("<br>");
            }
            out.println("<button type='submit'>Crea</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NullPointerException n){
            System.out.println("Programma ancora non selezionato");
        } catch(NumberFormatException nf){
            System.out.println("Numero elementi ancora non selezionato");
        }
    }
    
    private void insert_done(HttpServletRequest request, HttpServletResponse response){
            

            try (PrintWriter out = response.getWriter()){
                if(request.getParameter("channel") != null){
                    Integer n = Integer.parseInt(request.getParameter("channelNumber"));
                    ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().storeChannel(n, request.getParameter("channelName"));
                }
                if(request.getParameter("program") != null){
                    Program program = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().createProgram();
                    program.setName(request.getParameter("programName"));
                    program.setDescription(request.getParameter("programDescription"));
                    program.setGenre(ProgramImpl.Genre.valueOf(request.getParameter("genre")));
                    program.setLink(request.getParameter("link"));
                    program.setIsSerie(Boolean.valueOf(request.getParameter("serie")));
                    if(program.IsSerie()){
                        program.setSeasonsNumber(Integer.parseInt(request.getParameter("nSeasons")));
                    }
                    ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().storeProgram(program);
                }
                if(request.getParameter("episode") != null){
                    Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().createEpisode();
                    episode.setName(request.getParameter("episodeName"));
                    episode.setSeasonNumber(Integer.parseInt(request.getParameter("seasonNumber")));
                    episode.setNumber(Integer.parseInt(request.getParameter("episodeNumber")));
                    Integer id = Integer.parseInt(request.getParameter("p"));
                    Program program = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(id);
                    episode.setProgram(program);
                   ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().storeEpisode(episode); 
                }
                System.out.println(request.getParameter("schedule"));
                if(request.getParameter("schedule") != null){ 
                    HttpSession s = request.getSession(false);
                    int ln = Integer.parseInt(request.getParameter("nElem"));
                    for(int i = 1; i <= ln; i++){
                    /*Integer c = Integer.parseInt(request.getParameter("ch" + i));
                    Channel channel = ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(c);*/            
                    Schedule schedule = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().createSchedule();
                    /*Program program = (Program) s.getAttribute("programSelected");
                    schedule.setProgram(program);
                    schedule.setChannel(channel);*/
                    Program program = null;
                    if(request.getParameter("pr" + i).substring(0,1).equals("e")){
                      Integer e = Integer.parseInt(request.getParameter("pr" + i).substring(1,2));
                      Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(e);  
                      schedule.setEpisode(episode);
                      program = episode.getProgram();
                    }else{
                    Integer p = Integer.parseInt(request.getParameter("pr" + i));
                    program = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(p);
                    }
                    Channel channel = (Channel) s.getAttribute("channelSelected");
                    schedule.setProgram(program);
                    schedule.setChannel(channel);
                    
                    
                    schedule.setStartTime(LocalTime.parse(request.getParameter("start" + i)));
                    schedule.setEndTime(LocalTime.parse(request.getParameter("end" + i)));
                    schedule.setDate(LocalDate.parse(request.getParameter("date" + i)));
                    /*if(program.IsSerie()){
                      Integer e = Integer.parseInt(request.getParameter("ep" + i));
                      Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(e);
                      schedule.setEpisode(episode);
                    }*/
                   ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().storeSchedule(schedule);
                    }
                    //s.setAttribute("programSelected", null);
                    s.setAttribute("channelSelected", null);
                }
                 
                try {
                    TemplateResult res = new TemplateResult(getServletContext());
                    request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                    res.activate("inserimentoriuscito.ftl.html", request, response);
                    /*
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet Insert</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1> Inserimento effettuato </h1>");
                    out.println("<a href='javascript:history.back()'> Inserisci di nuovo </a>");
                    out.println("</body>");
                    out.println("</html>");
                    */
                } catch (TemplateManagerException ex) {
                    Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (DataException ex) {
                Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
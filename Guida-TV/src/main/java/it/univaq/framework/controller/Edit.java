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
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ProgramImpl;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.Episode;
import it.univaq.guida.tv.data.model.Program;
import it.univaq.guida.tv.data.model.Schedule;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
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
        try{
            //channel
            if(request.getParameter("channel") != null){
                int channel_key;
                if(request.getParameter("channelNumber") == null){
                    if(request.getParameter("ch") != null){ //verifico se ho scelto un elemento dal selettore 

                        Integer id = Integer.parseInt(request.getParameter("ch"));
                        request.setAttribute("channelSelected", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(id));

                    }

                        request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                        channel_edit(request, response);

                }else{
                    channel_key = SecurityLayer.checkNumeric(request.getParameter("channelNumber"));
                    request.setAttribute("key", channel_key);
                    edit_done(request, response);
                }
            }  
            //program
            if(request.getParameter("program") != null){
                int program_key;           
                    if(request.getParameter("prog") == null){

                        if(request.getParameter("pr") != null){ //verifico se ho scelto un elemento dal selettore 

                            Integer id = Integer.parseInt(request.getParameter("pr"));
                            request.setAttribute("programSelected", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(id));

                        }

                            request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                            program_edit(request, response);

                    }else{
                        program_key = SecurityLayer.checkNumeric(request.getParameter("prog"));
                        request.setAttribute("key", program_key);
                        edit_done(request, response);
                    }

            }
            //episode
            if(request.getParameter("episode") != null){
                int episode_key;          
                    if(request.getParameter("sn") == null){

                        if(request.getParameter("pr") != null){ //verifico se ho scelto un elemento dal selettore 

                            Integer id = Integer.parseInt(request.getParameter("pr"));
                            Program pro = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(id);

                            request.setAttribute("programSelected", pro);
                            request.setAttribute("episodes", ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes(pro));
   
                        }
                        
                            if(request.getParameter("delEp") != null){
                                delete_done(request, response);
                            }

                            request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                            episode_edit(request, response);            
                        
                    }else{
                        episode_key = SecurityLayer.checkNumeric(request.getParameter("ek"));
                        request.setAttribute("key", episode_key);
                        edit_done(request, response);
                    }       
            }
            //schedule
            if(request.getParameter("schedule") != null){
                int schedule_key;
                LocalDate today = LocalDate.now();
                if(request.getParameter("pName") == null){
                    if(request.getParameter("ch") != null){ //verifico se ho scelto un elemento dal selettore 

                        Integer id = Integer.parseInt(request.getParameter("ch"));
                        Channel channel = ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(id);
                        request.setAttribute("channelSelected", channel);
                        request.setAttribute("schedules", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByChannelAdmin(channel, today));

                    }

                        if(request.getParameter("delSched") != null){                            
                            delete_done(request, response);
                        }

                        request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                        schedule_edit(request, response);

                }else{
                        schedule_key = SecurityLayer.checkNumeric(request.getParameter("sk"));
                        request.setAttribute("key", schedule_key);
                        edit_done(request, response); 
                }
            }
        
        }catch (DataException ex) {
            Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void channel_edit(HttpServletRequest request, HttpServletResponse response){       
        response.setContentType("text/html;charset=UTF-8");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        System.out.println(channels.get(0).getName());
        Channel channelSelected = (Channel) request.getAttribute("channelSelected");
        

        try {   
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if(channelSelected != null)
            res.activate("modificanale.ftl.html", request, response);           
            else
            res.activate("modificanaleparz.ftl.html", request, response);   
            
            /*
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
            if(channelSelected != null){
            out.println("<option value = '" + channelSelected.getKey() + "'>" + channelSelected.getName() + "</option>");
            for(Channel c : channels){
            if(!(c.getName().equals(channelSelected.getName())))
            out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
            }
            }else{
            for(Channel c : channels){
            out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
            }
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
        */      } catch (TemplateManagerException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void program_edit(HttpServletRequest request, HttpServletResponse response){       
        response.setContentType("text/html;charset=UTF-8");      
        ProgramImpl.Genre[] genres = ProgramImpl.Genre.values();
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        request.setAttribute("genres",genres);
        Program programSelected = (Program) request.getAttribute("programSelected");
        
            
            try {     
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if(programSelected != null)     
             res.activate("modificaprogramma.ftl.html", request, response);
             else
            res.activate("modificaprogrammaparz.ftl.html", request, response);   
        }
        catch (TemplateManagerException ex) {
        Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }           
          
        
        /*
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
            if(programSelected != null){ //Da rivedere se è possibile mettere un solo if (ce n'è un altro uguale a riga 217)
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
            out.println("<input type='submit' name='select' value='SELEZIONA'/>");
            out.println("</form>");
            out.println("<form method='post' action='edit?program=1'>");
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
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);

        } 
*/  
    }
    
    private void episode_edit(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        List<Episode> episodes = (List<Episode>) request.getAttribute("episodes");
        Program programSelected = (Program) request.getAttribute("programSelected");
        
         
             try {
                 TemplateResult res = new TemplateResult(getServletContext());
                 request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                 if(programSelected != null)
                 res.activate("modificaepisodio.ftl.html", request, response);
                  else
            res.activate("modificaepisodioparz.ftl.html", request, response);  
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        /*
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Edit</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Modifica o elimina un episodio: </h1>");
            out.println("<form method='post' action='edit?episode=0'>");
            out.println("Scegli la serie:");           
            out.println("<select name='pr' id='pr'>");
            if(programSelected != null){
                out.println("<option value = '" + programSelected.getKey() + "'>" + programSelected.getName() + "</option>");    
                for(Program p : programs){
                  if(!(p.getName().equals(programSelected.getName())) && p.IsSerie())
                    out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");     
                }
            }else{
                for(Program p : programs){
                if(p.IsSerie())
                    out.println("<option value = '" + p.getKey() + "'>" + p.getName() + "</option>");     
                }
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
                        
                            out.println("<form method='post' action='edit?episode=0'>");
                             out.println("<input type='text' name='ek' value='" + e.getKey() + "' hidden/>"); 
                             out.println("<td style='text-align:center'> <input type='text' name='sn' value='" + e.getSeasonNumber() + "'/> </td>");
                             out.println("<td style='text-align:center'> <input type='text' name='en' value='" + e.getNumber() + "'/> </td>"); 
                             out.println("<td style='text-align:center'> <input type='text' name='nm' value='" + e.getName() + "'/> </td>");
                             
                            out.println("<td style='text-align:center'>");
                             out.println("<input type='submit' name='edit' value='MODIFICA'/>");
                             out.println("</form>");
                            out.println("</td>");
                             
                           out.println("<td style='text-align:center'>");  
                            out.println("<form method='post' action='edit?episode=0&delEp=" +e.getKey()+"'>");
                            out.println("<input type='submit' name='delete' value='ELIMINA'/>");
                            out.println("</form>");
                           out.println("</td>");
                           
                        out.println("</tr>");
                    }
                }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
*/
         
    }
    
    private void schedule_edit(HttpServletRequest request, HttpServletResponse response) throws DataException{
        response.setContentType("text/html;charset=UTF-8");
        Channel channelSelected = (Channel) request.getAttribute("channelSelected");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
        
        
       
        try { TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        if(channelSelected != null)
            res.activate("modificaschedule.ftl.html", request, response);
        else
        res.activate("modificascheduleparz.ftl.html", request, response);  
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*
        try (PrintWriter out = response.getWriter()) {  
            
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Modifica o elimina un palinsesto: </h1>");
            out.println("<form method='post' action='edit?schedule=0'>");
            out.println("Scegli il canale:");
            out.println("<select name='ch' id='ch'>");
            if(channelSelected != null){
            out.println("<option value = '" + channelSelected.getKey() + "'>" + channelSelected.getName() + "</option>");          
                for(Channel c : channels){
                    if(!(c.getName().equals(channelSelected.getName())))
                out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
                }
                
                
                
            }else{
                for(Channel c : channels){
                out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
                }
            }
                out.println("</select>");
              out.println("<input type='submit' name='select' value='SELEZIONA'/>");
              
            out.println("</form>");
            
                if(schedules != null){
                    out.println("<table style='width:100%'>");
                    out.println("<tr>");
                    out.println("<th>Programma</th>");
                    out.println("<th>Episodio</th>");
                    out.println("<th>Data</th>");                   
                    out.println("<th>Ora inizio</th>");
                    out.println("<th>Ora fine</th>");
                    out.println("<th>Modifica</th>");
                    out.println("<th>Elimina</th>");
                    out.println("</tr>");
                    for(Schedule s : schedules){
                        out.println("<tr>");
                        
                             out.println("<form method='post' action='edit?schedule=1'>");
                             out.println("<input type='text' name='sk' value='" + s.getKey() + "' hidden/>"); 
                             out.println("<td style='text-align:center'> <input type='text' name='pName' value='" + s.getProgram().getName() + "'  readonly /> </td>");
                             if(s.getEpisode() != null){
                                List<Episode> episodes = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes(s.getProgram());
                                out.println("<td style='text-align:center'> <select name='ep' id='ep'>");
                                if(channelSelected != null){
                                out.println("<option value='" + s.getEpisode().getKey() + "'> " + s.getEpisode().getName() + " </option>");   
                                }
                                for(Episode e : episodes){
                                    if(!(e.getName().equals(s.getEpisode().getName()))){
                                     out.println("<option value='" + e.getKey() + "'> " + e.getName() + " </option>");   
                                    }
                                }
                                 out.println("</select> </td>");
                             }else{
                                out.println("<td style='text-align:center'> <input type='text' name='empty' disabled /> </td>"); 
                             }
                             out.println("<td style='text-align:center'> <input type='date' name='d' value='" + s.getDate() + "'/> </td>");
                             out.println("<td style='text-align:center'> <input type='time' name='st' value='" + s.getStartTime() + "'/> </td>");
                             out.println("<td style='text-align:center'> <input type='time' name='et' value='" + s.getEndTime() + "'/> </td>");
                             
                            out.println("<td style='text-align:center'>");
                             out.println("<input type='submit' name='edit' value='MODIFICA'/>");
                             out.println("</form>");
                            out.println("</td>");
                             
                           out.println("<td style='text-align:center'>");  
                            out.println("<form method='post' action='edit?schedule=0&delSched=" +s.getKey()+"'>");
                            out.println("<input type='submit' name='delete' value='ELIMINA'/>");
                            out.println("</form>");
                           out.println("</td>");
                           
                        out.println("</tr>");
                    }
                }
            
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

*/
      
    }
    
    private void edit_done(HttpServletRequest request, HttpServletResponse response){
        try (PrintWriter out = response.getWriter()){
        if(request.getParameter("channel") != null){
            Integer n = Integer.parseInt(request.getParameter("channelNumber"));
            Channel channel = ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(n);
            channel.setName(request.getParameter("channelName"));
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().storeChannel(channel);
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
        if(request.getParameter("schedule") != null){
            int key = (int)request.getAttribute("key");
            Schedule schedule = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(key);
            if(schedule.getProgram().IsSerie()){
                int ep = Integer.parseInt(request.getParameter("ep"));
                Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(ep);
                schedule.setEpisode(episode);
            }
            schedule.setDate(LocalDate.parse(request.getParameter("d")));            
            schedule.setStartTime(LocalTime.parse(request.getParameter("st")));
            schedule.setEndTime(LocalTime.parse(request.getParameter("et")));
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().storeSchedule(schedule);
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
    
    private void delete_done(HttpServletRequest request, HttpServletResponse response){
        try(PrintWriter out = response.getWriter()) {
        if(request.getParameter("delEp") != null){
            Integer id = Integer.parseInt(request.getParameter("delEp"));
            Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(id);
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().deleteEpisode(episode);
        }
        if(request.getParameter("delSched") != null){
            Integer id = Integer.parseInt(request.getParameter("delSched"));
            Schedule schedule = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(id);
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().deleteSchedule(schedule);
        }
        out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Eliminazione effettuata </h1>");
        } catch (DataException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

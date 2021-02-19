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
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.FavouriteChannel;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static java.util.concurrent.TimeUnit.*;

/**
 *
 * @author Matteo
 */

public class AdminController extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
         TemplateResult res = new TemplateResult(getServletContext());
    
        HttpSession s = request.getSession(false); 
        try {
        if(s != null){
            User user = (User) s.getAttribute("user");
            if (user != null && !(user.getKey().isEmpty())){               
                if(user.getKey().equals("admin@email.it")){
                   s.setAttribute("programSelected", null);
                   s.setAttribute("channelSelected", null);
                    if(request.getParameter("sendemail") != null)
                        sendEmail(request,response);
                    action_admin(request, response);
                }else{
                        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                        res.activate("sonoin.ftl.html", request, response);                
                }
            }else{
                notLogged(request,response);        
            }
        }else{
            notLogged(request,response);
        }
        } catch (TemplateManagerException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    private void action_admin(HttpServletRequest request, HttpServletResponse response) {      
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("admin.ftl.html", request, response);
            /* try (PrintWriter out = response.getWriter()){
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>GESTIONE PAGINA</h1>");
            out.println("<h2> Azioni sui canali </h2>");
            out.println("<a href='admin/insert?channel=1'> Inserisci canale </a> <br>");
            out.println("<a href='admin/edit?channel=1'> Modifica canale </a> <br>");
            out.println("<a href='admin/delete?channel=0'> Elimina canale </a>");
            out.println("<h2> Azioni sui programmi </h2>");
            out.println("<a href='admin/insert?program=1'> Inserisci programma </a> <br>");
            out.println("<a href='admin/edit?program=1'> Modifica programma </a> <br>");
            out.println("<a href='admin/delete?program=0'> Elimina programma </a> <br>");
            out.println("<a href='admin/insert?episode=1'> Inserisci episodio </a> <br>");
            out.println("<a href='admin/edit?episode=0'> Modifica/Elimina episodio </a> <br>");
            out.println("<h2> Azioni sui palinsesti </h2>");
            out.println("<a href='admin/insert?schedule=1'> Inserisci palinsesto </a> <br>");
            out.println("<a href='admin/edit?schedule=0'> Modifica/Elimina palinsesto </a> <br>");
            out.println("<br><br><br>");
            out.println("<a href='admin?sendemail=1'> SEND EMAILS </a>");           
            out.println("</body>");
            out.println("</html>");
            } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        */      } catch (TemplateManagerException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendEmail(HttpServletRequest request, HttpServletResponse response) {
        
        
        
          
        try {
            List<User> users = null;
            users = (List<User>) ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getSubUsers();
            for(User u : users){
                FileWriter fw=new FileWriter("S:\\Programmi\\xampp\\htdocs\\univaq-Guida-TV\\Guida-TV\\Guida-TV\\src\\main\\java\\it\\univaq\\guida\\tv\\data\\files\\emailto"+u.getKey()+LocalDate.now()+".txt");
                fw.write("Ciao "+u.getKey()+"\r\n\r\n");
                
                //schedules di oggi per canali preferiti
                List<FavouriteChannel> channels = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().getFavouriteChannels(u);
                if(!channels.isEmpty()){
                    fw.write("Ecco i programmi che andranno in onda oggi in base ai tuoi canali preferiti: \r\n");
                    for(FavouriteChannel fc : channels){
                        List<Schedule> todayByC = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByFavChannel(fc.getChannel(),LocalDate.now(),fc.getTimeSlot());
                        for(Schedule s : todayByC){


                              fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+ "\r\n");  
                               if(s.getProgram().IsSerie())
                                    fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");

                        }    
                    }
                }
                //schedules di oggi per programmi preferiti che hanno la newsletter per la ricerca salvata
                List<FavouriteProgram> programs = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().getFavouritePrograms(u);
                
                if(!programs.isEmpty()){
                    fw.write("\r\nEcco i programmi che andranno in onda oggi in base alle tue ricerche salvate: \r\n");
                
                
                    for(FavouriteProgram fp : programs){
                        if(fp.getSavedSearch().getSendEmail()){
                            List<Schedule> todayByP = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByProgram(fp.getProgram(),LocalDate.now());
                            for(Schedule s : todayByP){

                                fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+"\r\n"); 
                                if(s.getProgram().IsSerie())
                                    fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");

                            }
                        }
                    }
                }
                
                fw.close();
            }
            System.out.println("fatto");
        } catch (DataException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void notLogged(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("devilogin.ftl.html", request, response);
            response.setContentType("text/html;charset=UTF-8");

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    }
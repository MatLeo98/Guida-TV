/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.FavouriteChannel;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.Schedule;
import it.univaq.guida.tv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
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

public class Admin extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);
            
            if (s != null && s.getAttribute("email") != null && !((String) s.getAttribute("email")).isEmpty()){  
                if(s.getAttribute("email").equals("admin@email.it")){
                    if(request.getParameter("sendemail") != null)
                        sendEmail(request,response);
                    action_admin(request, response);
                }else{
                    try (PrintWriter out = response.getWriter()) {
                        response.setContentType("text/html;charset=UTF-8");
                         out.println("<!DOCTYPE html>");
                         out.println("<html>");
                         out.println("<body>");
                        out.println("<h3> Non sei autorizzato ad accedere a questa pagina </h3>");
                        out.println("<br><br>");
                        out.println("<a href=\"home\">HOME</a>"); //DA CAMBIARE CHE VA DIRETTAMENTE ALLA PAGINA LOGIN
                        out.println("</body>");
                        out.println("</html>");
                    } catch (IOException ex) {
                        Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                try (PrintWriter out = response.getWriter()) {
                    response.setContentType("text/html;charset=UTF-8");
                     out.println("<!DOCTYPE html>");
                     out.println("<html>");
                     out.println("<body>");
                    out.println("<h3> Devi essere loggato per accedere a questa pagina");
                    out.println("<br><br>");
                    out.println("<a href=\"login\">GO TO LOGIN</a>"); //DA CAMBIARE CHE VA DIRETTAMENTE ALLA PAGINA LOGIN
                    out.println("</body>");
                    out.println("</html>");
                } catch (IOException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
    }
    
    private void action_admin(HttpServletRequest request, HttpServletResponse response) {      
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()){           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Admin</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>GESTIONE PAGINA</h1>");
            out.println("<h2> Azioni sui canali </h2>");
            out.println("<a href='admin/insert?channel=1'> Inserisci canale </a> <br>");
            out.println("<a href='admin/edit?channel=1'> Modifica canale </a> <br>");
            out.println("<a href='admin/delete'> Elimina canale </a>");
            out.println("<h2> Azioni sui programmi </h2>");
            out.println("<a href='admin/insert?program=1'> Inserisci programma </a> <br>");
            out.println("<a href='admin/edit?program=1'> Modifica programma </a> <br>");
            out.println("<a href=''> Elimina programma </a> <br>");
            out.println("<a href='admin/insert?episode=1'> Inserisci episodio </a> <br>");
            out.println("<a href='admin/edit?episode=1'> Modifica episodio </a> <br>");
            out.println("<a href=''> Elimina episodio </a>");
            out.println("<h2> Azioni sui palinsesti </h2>");
            out.println("<a href='admin/insert?schedule=1'> Inserisci palinsesto </a> <br>");
            out.println("<a href='admin/edit?schedule=1'> Modifica palinsesto </a> <br>");
            out.println("<a href=''> Elimina palinsesto </a>");
            out.println("<br><br><br>");
            out.println("<a href='admin?sendemail=1'> SEND EMAILS </a>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendEmail(HttpServletRequest request, HttpServletResponse response) {
         System.out.println("ciao");
        try {
            List<User> users = new ArrayList();
            users = (List<User>) ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getSubUsers();
            for(User u : users){
                //schedules di oggi per canali preferiti
                List<FavouriteChannel> channels = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().getFavouriteChannels(u);
                for(FavouriteChannel fc : channels){
                    List<Schedule> todayByC = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByFavChannel(fc.getChannel(),LocalDate.now(),fc.getTimeSlot());
                    for(Schedule s : todayByC){
                        System.out.println(s.getProgram().getName());
                    }
                }
                //schedules di oggi per programmi preferiti che hanno la newsletter per la ricerca salvata
                List<FavouriteProgram> programs = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().getFavouritePrograms(u);
                for(FavouriteProgram fp : programs){
                    //List<Schedule> todayByP = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByProgram(fc.getChannel(),LocalDate.now());
                }
            }
            
        } catch (DataException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    }
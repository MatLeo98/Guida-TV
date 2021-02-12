/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ScheduleImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.FavouriteChannel;
import it.univaq.guidatv.data.model.FavouriteProgram;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author giorg
 */
public class ProfileController extends BaseController {

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
            HttpSession s = request.getSession(false);
            
            if(s != null){
                User user = (User) s.getAttribute("user");
                      
                      
                if (user != null && !(user.getKey().isEmpty())){  
                    request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                    if (!(request.getParameter("emailgiornaliera") == null)){
                        setEmail(request,response);
                    }  
                    if(request.getParameter("daymail") != null){
                        setEmailSS(request,response);
                    }
                    System.out.println(request.getParameter("delch"));
                    if(request.getParameter("delch") != null){
                        delFavChannel(request,response);
                    }
                    if(request.getParameter("delprog") != null){
                        delFavProgram(request,response);
                    }
                    if(request.getParameter("delSS") != null){
                        delSS(request,response);
                    }

                    request.setAttribute("savedS",((GuidatvDataLayer)request.getAttribute("datalayer")).getSavedSearchesDAO().getSavedSearches(user));
                    request.setAttribute("favPrograms",((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().getFavouritePrograms(user));
                    request.setAttribute("favChannels",((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().getFavouriteChannels(user));


                    actions(request,response);


                }else{

                    try (PrintWriter out = response.getWriter()) {
                        response.setContentType("text/html;charset=UTF-8");
                         out.println("<!DOCTYPE html>");
                         out.println("<html>");
                         out.println("<body>");
                        out.println("<h3> Devi essere loggato per visualizzare il profilo");
                        out.println("<br><br>");
                        out.println("<a href=\"login\">GO TO LOGIN</a>"); //DA CAMBIARE CHE VA DIRETTAMENTE ALLA PAGINA LOGIN
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
                        out.println("<h3> Devi essere loggato per visualizzare il profilo");
                        out.println("<br><br>");
                        out.println("<a href=\"login\">GO TO LOGIN</a>"); //DA CAMBIARE CHE VA DIRETTAMENTE ALLA PAGINA LOGIN
                        out.println("</body>");
                        out.println("</html>");
                    } catch (IOException ex) {
                        Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
            } 
            
        } catch (DataException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private void actions(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        request.setAttribute("user",user);
        try {
            user = ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getUser(user.getKey());
            request.setAttribute("after",null);
         ScheduleImpl.TimeSlot[] timeslots = ScheduleImpl.TimeSlot.class.getEnumConstants();
         request.setAttribute("timeslots",timeslots);
         if(!user.isConfirmed()){
            request.setAttribute("confirmed","no");
            String URI = user.getUri();
            request.setAttribute("URI", URI);
         }
         
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("profile.ftl.html", request, response);
            
            
        } catch (DataException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
         /*List<Channel> channels = (List<Channel>) request.getAttribute("channels");
         
         List<SavedSearches> savedS = (List<SavedSearches>) request.getAttribute("savedS");
         List<FavouriteProgram> programs = (List<FavouriteProgram>) request.getAttribute("favPrograms");
         List<FavouriteChannel> favCh = (List<FavouriteChannel>) request.getAttribute("favChannels");*/
         
         
        response.setContentType("text/html;charset=UTF-8");
        /*try (PrintWriter out = response.getWriter()) {
        out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Profile</title>");            
            out.println("</head>");
            out.println("<body>");
            if(!user.isConfirmed())
                out.println("<a href=\"confirm?URI="+URI+"\"> CONFERMA EMAIL </a>");
            out.println("<h1> PROFILO </h1>");
            out.println("<br><br>");
            out.println("<h3> Email Giornaliera: </h3>");
            out.println("<form method=\"post\" action=\"profile\">");
                out.println("<label for='channel'> Scegli i tuoi canali preferiti </label>");
                out.println("<br>");
                out.println("<select name='channels[]' id='channel' multiple>");
                for(Channel c : channels){
                    out.println("<option value = '" + c.getKey() + "'>" + c.getName() + "</option>");
                }
                out.println("</select>");
                out.println("<br><br>");
                out.println("<label for='timeslots'> Scegli le fasce orarie </label>");
                out.println("<br>");
                out.println("<select name='timeslots[]' id='timeslots' multiple>");
                for(ScheduleImpl.TimeSlot timeslot : timeslots){
                    out.println("<option value=\""+timeslot+"\">"+timeslot.toString()+"</option>");
                }
                 out.println("</select>");
                 out.println("<p> Vuoi ricevere l'email giornaliera?");
                 out.println("<br>");
                 out.println(" <input type=\"radio\" name=\"emailgiornaliera\" id=\"abilita\" value=\"1\" />");
                 out.println("<label for='abilita'> Sì </label>");
                 out.println("<br>");
                 out.println("<input type=\"radio\" name=\"emailgiornaliera\" id=\"disabilita\" value=\"0\" checked=\"checked\" />");
                 out.println("<label for='disabilita'> No </label>");
                 out.println("<br><br>");
                 out.println("<input type=\"submit\" name=\"salva\" value=\"SALVA\"/>");
            out.println("</form>");
            out.println("<br><br>");
            out.println("<center><h2> Ecco le tue richerche salvate</h2></center>");
                out.println("<br>");
                out.println("<table style=\"width:100%\">");
                out.println("<tr>");
                  out.println("<th>Titolo</th>");
                
                  out.println("<th>Genere</th>");
                
                  out.println("<th>Ora inizio minima</th>");
               
                  out.println("<th>Ora inizio massima</th>");
                
                  out.println("<th>canale</th>");
                
                  out.println("<th>Data minima</th>");
                
                  out.println("<th>Data massima</th>");
                
                  out.println("<th>Email giornaliera</th>");
                
                  out.println("<th>Vedi Risultati</th>");
                   out.println("<th>Elimina</th>");
                out.println("</tr>");
                
                for(SavedSearches s : savedS){
                    
                    out.println("<tr>");
                         out.println("<td style=\"text-align:center\">" + s.getTitle() + "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + s.getGenre() + "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + s.getMinStartHour()+ "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + s.getMaxStartHour()+ "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + s.getChannel()+ "</td>");
                   
                         out.println("<td style=\"text-align:center\">" + s.getStartDate()+ "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + s.getEndDate()+ "</td>");
                    out.println("<form method=\"post\" action=\"profile?daymail="+s.getKey()+"\">");
                    out.println("<td style=\"text-align:center\">");
                    out.println(" <button name='daymailss' type='submit' value='0'>NO</button>");
                    out.println(" <button  name='daymailss' type='submit' value='1'>SI</button>");
                    out.println("</td>");
                   
                    out.println("</form>");
                
                    
                    out.println("<td style=\"text-align:center\"><a href='searchresults?title="+s.getTitle()+"&genre="+ s.getGenre() +"&channel=" + s.getChannel()+ "&min="+ s.getMinStartHour()+ "&max=" + s.getMaxStartHour()+ "&date1=" + s.getStartDate()+ "&date2=" + s.getEndDate()+ "'><button> VEDI</button> </a></td>");
                    out.println("<td style=\"text-align:center\">");
                    out.println("<form method=\"post\" action=\"profile?delSS=" +s.getKey()+"\">");
                    
               
                    out.println("<input type=\"submit\" name=\"elimina\" value=\"ELIMINA\"/>");
                    out.println("</form>");
                    out.println("</td>");
                         out.println("</tr>");
                }
                    out.println("</table>");
                    
                    out.println("<br><br><br>");
                    
                    
                    out.println("<center><h2> Ecco i tuoi programmi preferiti</h2></center>");
                out.println("<br>");
                out.println("<table style=\"width:100%\">");
                out.println("<tr>");
                  out.println("<th>Nome</th>");
                
                  out.println("<th>Descrizione</th>");
                
                  out.println("<th>Genere</th>");
               
                  out.println("<th>Link</th>");
                
                  out.println("<th>Elimina</th>");
                
                
                out.println("</tr>");
                
                for(FavouriteProgram p : programs){
                    
                    out.println("<tr>");
                         out.println("<td style=\"text-align:center\">" + p.getProgram().getName() + "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + p.getProgram().getDescription()+ "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + p.getProgram().getGenre()+ "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + p.getProgram().getLink()+ "</td>");
                    
                     out.println("<td style=\"text-align:center\">");
                    out.println("<form method=\"post\" action=\"profile?delprog=" +p.getKey()+"\">");
                    
               
                    out.println("<input type=\"submit\" name=\"elimina\" value=\"ELIMINA\"/>");
                    out.println("</form>");
                    out.println("</td>");
                    
                      
                }
                    out.println("</table>");
                   
                
                
                out.println("<br><br><br>");
                
                
                 out.println("<center><h2> Ecco i tuoi canali preferiti</h2></center>");
                out.println("<br>");
                out.println("<table style=\"width:100%\">");
                out.println("<tr>");
                  out.println("<th>Canale</th>");
                
                  out.println("<th>Nome</th>");
                  
                  out.println("<th>Fascia Oraria</th>");
                
                  out.println("<th>Elimina</th>");
                
                
                out.println("</tr>");
                
                for(FavouriteChannel c : favCh){
                    
                    Integer var = c.getChannel().getKey();
                    
                    out.println("<tr>");
                    if(var != request.getAttribute("canale")){
                       
                         out.println("<td  style=\"text-align:center\">" + c.getChannel().getKey() + "</td>");
                    
                         out.println("<td style=\"text-align:center\">" + c.getChannel().getName()+ "</td>");
                    }else{
                        out.println("<td  style=\"text-align:center\"></td>");
                    
                         out.println("<td style=\"text-align:center\"></td>");
                    }
                         request.setAttribute("canale", c.getChannel().getKey());
                    
                         out.println("<td style=\"text-align:center\">" + c.getTimeSlot()+ "</td>");
                    
                     out.println("<td style=\"text-align:center\">");
                    out.println("<form method=\"post\" action=\"profile?delch=" +c.getKey()+"\">");
                    
               
                    out.println("<input type=\"submit\" name=\"elimina\" value=\"ELIMINA\"/>");
                    out.println("</form>");
                    
                    
                    
                    out.println("</td>");
                
                    
                      
                }
                    out.println("</table>");
                
            out.println("</body>");
            out.println("</html>");
            
    }   catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    private void setEmail(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = request.getSession(false);
        User user = (User) s.getAttribute("user");
        String email = user.getKey();
        Boolean newsletter = (Integer.parseInt(request.getParameter("emailgiornaliera"))==1);
        String[] channels = null;
        String[] timeslots = null;
        if(request.getParameterValues("channels[]") != null)
            channels = request.getParameterValues("channels[]");
        else{
            channels = new String[1];
            channels[0] = "default";
        }
        if(request.getParameterValues("timeslots[]") != null)
            timeslots = request.getParameterValues("timeslots[]");
        else{ //SE NON VIENE SCELTA NESSUNA FASCIA ORARIA, VENGONO SETTATE TUTTE
            timeslots = new String[4];
            timeslots[0] = "mattina";
            timeslots[1] = "pomeriggio";
            timeslots[2] = "sera";
            timeslots[3] = "notte";
            
        }
        ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().storeFavChannel(channels,timeslots, email);
        ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().setNewsletter(email,newsletter);
    }

    private void setEmailSS(HttpServletRequest request, HttpServletResponse response) {
        
       Integer id = Integer.parseInt(request.getParameter("daymail"));
       Boolean dayMail = (Integer.parseInt(request.getParameter("daymailss"))==1);
       
        try {
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getSavedSearchesDAO().setDayMail(id,dayMail);
            
        } catch (DataException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private void delFavChannel(HttpServletRequest request, HttpServletResponse response) {
       Integer id = Integer.parseInt(request.getParameter("delch"));
      
       ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().deleteFavouriteChannel(id);
    }

    private void delFavProgram(HttpServletRequest request, HttpServletResponse response) {
       Integer id = Integer.parseInt(request.getParameter("delprog"));
      
       ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().deleteFavouriteProgram(id);
    }

    private void delSS(HttpServletRequest request, HttpServletResponse response) {
        Integer id = Integer.parseInt(request.getParameter("delSS"));
      
       ((GuidatvDataLayer)request.getAttribute("datalayer")).getSavedSearchesDAO().deleteSavedSearch(id);
    }
    

    

}

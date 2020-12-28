/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ScheduleImpl;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.SavedSearches;
import it.univaq.guida.tv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
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
            
            if (s != null && s.getAttribute("email") != null && !((String) s.getAttribute("email")).isEmpty()){  
                request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                if (!(request.getParameter("emailgiornaliera") == null)){
                    setEmail(request,response);
                }  
                User user = ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getUser((String)s.getAttribute("email"));
                request.setAttribute("savedS",((GuidatvDataLayer)request.getAttribute("datalayer")).getSavedSearchesDAO().getSavedSearches(user));
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
            
            
        } catch (DataException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private void actions(HttpServletRequest request, HttpServletResponse response) {
         List<Channel> channels = (List<Channel>) request.getAttribute("channels");
         ScheduleImpl.TimeSlot[] timeslots = ScheduleImpl.TimeSlot.class.getEnumConstants();
         List<SavedSearches> savedS = (List<SavedSearches>) request.getAttribute("savedS");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
        out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Profile</title>");            
            out.println("</head>");
            out.println("<body>");
            
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
                
                  out.println("<th>Vedi</th>");
                out.println("</tr>");
                
                for(SavedSearches s : savedS){
                    
                    out.println("<tr>");
                         out.println("<td>" + s.getTitle() + "</td>");
                    
                         out.println("<td>" + s.getGenre() + "</td>");
                    
                         out.println("<td>" + s.getMinStartHour()+ "</td>");
                    
                         out.println("<td>" + s.getMaxStartHour()+ "</td>");
                    
                         out.println("<td>" + s.getChannel()+ "</td>");
                   
                         out.println("<td>" + s.getStartDate()+ "</td>");
                    
                         out.println("<td>" + s.getEndDate()+ "</td>");
                    out.println("<form method=\"post\" action=\"profile\">");
                    out.println("<td>");
                    out.println(" <input type=\"radio\" name=\"emailgiornaliera\" id=\"abilita\" value=\"1\" />");
                    out.println("<label for='abilita'> Sì </label>");
                    
                     out.println("<input type=\"radio\" name=\"emailgiornaliera\" id=\"disabilita\" value=\"0\" checked=\"checked\" />");
                    out.println("<label for='disabilita'> No </label>");
                    out.println("<input type=\"submit\" name=\"salva\" value=\"SALVA\"/>");
                    out.println("</form>");
                out.println("</td>");
                    
                         out.println("<td><button href=\"search\"> VEDI </button></td>");
                    out.println("</tr>");
                }
                    out.println("</table>");
                   
                
                
                out.println("<br><br>");
                
            out.println("</body>");
            out.println("</html>");
            
    }   catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setEmail(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = request.getSession(false);
        String email = (String) s.getAttribute("email");
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
    

    

}

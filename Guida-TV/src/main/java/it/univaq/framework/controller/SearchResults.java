/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.data.DataLayer;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ProgramImpl;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.SavedSearches;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
 * @author Matteo
 */
public class SearchResults extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          
            throws ServletException {
        try{
            request.setAttribute("genres", ProgramImpl.Genre.values());
            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
            String tit = request.getParameter("title");
            String gen = request.getParameter("genre");
            String ch = request.getParameter("channel");
            String date1 = request.getParameter("date1");
            String date2 = request.getParameter("date2");
            String min = request.getParameter("min");
            String max = request.getParameter("max");
            if(request.getParameter("saved") != null){
                HttpSession session = request.getSession(false);
                User user = (User) session.getAttribute("user");
                String email = user.getKey();
                tit = (String) session.getAttribute("title");
                gen = (String) session.getAttribute("genre");
                ch = (String) session.getAttribute("channel");
                date1 = (String) session.getAttribute("date1");
                date2 =(String) session.getAttribute("date2");
                min = (String) session.getAttribute("min");
                max = (String) session.getAttribute("max");
                request.setAttribute("savedS", ((GuidatvDataLayer)request.getAttribute("datalayer")).getSavedSearchesDAO().storeSavedSearches(tit, gen, ch, date1, date2, min, max, email));
                request.setAttribute("search", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().search(tit, gen, ch, min, max, date1, date2));
                request.setAttribute("message","Ricerca salvata");
                store_prefPrograms(request,response);
                //action_results(request, response);
                
              
            }else{
                request.setAttribute("search", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().search(tit, gen, ch, min, max, date1, date2));
            }
             action_results(request, response);
        }catch (NumberFormatException ex) {
            request.setAttribute("message", "Home key not specified");           
        } catch (DataException ex) { 
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_results(HttpServletRequest request, HttpServletResponse response){ 
        HttpSession session = request.getSession(false);
        String email = null;
        if(session != null){
            User user = (User) session.getAttribute("user");
            email = user.getKey();
            request.setAttribute("email",email);
        }
                 
        
        /*response.setContentType("text/html;charset=UTF-8");
        List<Schedule> search = (List<Schedule>) request.getAttribute("search");
        try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SearchResults</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<body>");*/
            if(email != null){
                String tit = request.getParameter("title");
                String gen = request.getParameter("genre");
                String ch = request.getParameter("channel");
                String date1 = request.getParameter("date1");
                String date2 = request.getParameter("date2");
                String min = request.getParameter("min");
                String max = request.getParameter("max");
                session.setAttribute("title", tit);
                session.setAttribute("genre", gen);
                session.setAttribute("channel", ch);
                session.setAttribute("date1", date1);
                session.setAttribute("date2", date2);
                session.setAttribute("min", min);
                session.setAttribute("max", max);
            /*out.println("<form method=\"post\" action=\"searchresults?saved=1\">");
            out.println("<button type='submit'>Salva questi criteri di ricerca</button>");
            out.println("</form>");*/
            }
            try {
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("searchresults.ftl.html", request, response);
            } catch (TemplateManagerException ex) {
                Logger.getLogger(SearchResults.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*out.println("<h1>RISULTATI:</h1>");
            for(Schedule s : search){
            out.println("<h3><a href = 'program?id=" + s.getProgram().getKey() + "'>" + s.getProgram().getName() + "</a></h3>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(SearchResults.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    private void store_prefPrograms(HttpServletRequest request, HttpServletResponse response) {
       List<Schedule> search = (List<Schedule>) request.getAttribute("search");
       HttpSession session = request.getSession(false);
       User user = (User) session.getAttribute("user");
       String email = user.getKey();
       List<Program> programs = new ArrayList<>();
       DataLayer datalayer = (DataLayer)request.getAttribute("datalayer");
       SavedSearches ss;
       for (Schedule s : search){
           programs.add(s.getProgram());
       }
        try {
            
            ss = (SavedSearches) request.getAttribute("savedS"); 
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().storeFavPrograms(programs, email, ss.getKey());
        } catch (DataException ex) {
            Logger.getLogger(SearchResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

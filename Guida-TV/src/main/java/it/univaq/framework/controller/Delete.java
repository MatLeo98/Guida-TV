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
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Program;
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
public class Delete extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            
            if(request.getParameter("channel") != null){

                 if(request.getParameter("delCh") != null){                  
                     delete_done(request, response);
                 }else{                 
                     request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
                     channel_delete(request, response);                     
                 }
            }
            
            if(request.getParameter("program") != null){
                  if(request.getParameter("delPr") != null){                  
                     delete_done(request, response);
                 }else{                 
                     request.setAttribute("programs", ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                     program_delete(request, response);                     
                 }
            }
            
        }catch (DataException ex) {
            Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void channel_delete(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        //try (PrintWriter out = response.getWriter()) {

            try {
                
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("cancellacanale.ftl.html", request, response);
            
            
            /*
       
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Delete</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Elimina un canale: </h1>");
            out.println("<table style='width:100%'>");
                out.println("<tr>");
                  out.println("<th>Numero canale</th>");
                  out.println("<th>Nome canale</th>");
                  out.println("<th>Elimina</th>");
                out.println("if(channels != null){</tr>");
                if(channels != null){
                    for(Channel c : channels){
                        out.println("<tr>");
                        
                           out.println("<td style='text-align:center'> " + c.getKey() + " </td>"); 
                           out.println("<td style='text-align:center'> " + c.getName() + " </td>");
                             
                           out.println("<td style='text-align:center'>");  
                            out.println("<form method='post' action='delete?channel=0&delCh=" + c.getKey() + "'>");
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
        catch (TemplateManagerException ex) {
                Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
            }
    
        }
    
    private void program_delete(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Delete</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Elimina un programma: </h1>");
            out.println("<table style='width:100%'>");
                out.println("<tr>");
                  out.println("<th>Nome programma</th>");
                  out.println("<th>Descrizione</th>");
                  out.println("<th>Genere</th>");
                  out.println("<th>Link</th>");
                  out.println("<th>Elimina</th>");
                out.println("</tr>");
                if(programs != null){
                    for(Program p : programs){
                        out.println("<tr>");

                             out.println("<td style='text-align:center' hidden>" + p.getKey() + "  </td>"); 
                             out.println("<td style='text-align:center'> " + p.getName() + " </td>");
                             out.println("<td style='text-align:center'> " + p.getDescription() + " </td>");
                             out.println("<td style='text-align:center'> " + p.getGenre() + " </td>");
                             out.println("<td style='text-align:center'> " + p.getLink() + " </td>");
                             
                           out.println("<td style='text-align:center'>");  
                            out.println("<form method='post' action='delete?program=0&delPr=" +p.getKey()+"'>");
                            out.println("<input type='submit' name='delete' value='ELIMINA'/>");
                            out.println("</form>");
                           out.println("</td>");
                           
                        out.println("</tr>");
                    }
                }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void delete_done(HttpServletRequest request, HttpServletResponse response){
        try (PrintWriter out = response.getWriter()){
        if(request.getParameter("channel") != null){
            Integer n = Integer.parseInt(request.getParameter("delCh"));
            Channel channel = ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannel(n);
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().deleteChannel(channel);
        }
        if(request.getParameter("program") != null){
            Integer n = Integer.parseInt(request.getParameter("delPr"));
            Program program = ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().getProgram(n);
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getProgramDAO().deleteProgram(program);
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Delete</title>"); 
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Eliminazione effettuata </h1>");
        out.println("<a href='javascript:history.back()'> Elimina di nuovo </a>");
        out.println("<br>");
        out.println("<br>");
        out.println("<a href='http://localhost:8080/Guida-tivu/admin'> Torna alla pagina admin </a>");
        } catch (IOException ex) {
            Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

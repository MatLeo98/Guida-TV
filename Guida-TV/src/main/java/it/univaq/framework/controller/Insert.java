/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import java.io.IOException;
import java.io.PrintWriter;
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
            if(request.getParameter("channelnumber") == null){
                System.out.println(request.getParameter("channelnumber"));
                action_insert(request, response);
            }else{
                insert_done(request, response);
            }
        }
            if(request.getParameter("program") != null){
                System.out.println("Programma");
            }
       
        
    }
    
    private void action_insert(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");        
        try (PrintWriter out = response.getWriter()) {  
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Inserisci un nuovo canale: </h1>");
           /* out.println("<form action='insert'>");
            out.println("<input type='text' placeholder='Numero canale' name='channelNumber'>");
            out.println("<input type='text' placeholder='Nome canale' name='channelName'>");
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
       
        
        response.setContentType("text/html;charset=UTF-8");        
        try (PrintWriter out = response.getWriter()) {  
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().storeChannel(Integer.parseInt(request.getParameter("channelnumber")) ,request.getParameter("channelname"));
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Insert</title>"); 
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Canale inserito </h1>");
            
        } catch (DataException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

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

public class Admin extends BaseController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_admin(request, response);
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
            out.println("<a href='admin/edit'> Modifica canale </a> <br>");
            out.println("<a href='admin/delete'> Elimina canale </a>");
            out.println("<h2> Azioni sui programmi </h2>");
            out.println("<a href='admin/insert?program=1'> Inserisci programma </a> <br>");
            out.println("<a href=''> Modifica programma </a> <br>");
            out.println("<a href=''> Elimina programma </a> <br>");
            out.println("<a href=''> Inserisci episodio </a> <br>");
            out.println("<a href=''> Modifica episodio </a> <br>");
            out.println("<a href=''> Elimina episodio </a>");
            out.println("<h2> Azioni sui palinsesti </h2>");
            out.println("<a href=''> Inserisci palinsesto </a> <br>");
            out.println("<a href=''> Modifica palinsesto </a> <br>");
            out.println("<a href=''> Elimina palinsesto </a>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    }
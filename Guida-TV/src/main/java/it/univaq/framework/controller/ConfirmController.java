/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
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
public class ConfirmController extends BaseController {

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
        HttpSession s = request.getSession(false);
        if (s != null && s.getAttribute("email") != null && !((String) s.getAttribute("email")).isEmpty() && request.getParameter("URI") != null){
            try {
                User user = ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getUser((String)s.getAttribute("email"));
                if(user.getUri().equals(request.getParameter("URI")))
                    ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().setConfirmed((String)s.getAttribute("email"));
                   
                    
            } catch (DataException ex) {
                Logger.getLogger(ConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
                
                try (PrintWriter out = response.getWriter()) {
                    response.setContentType("text/html;charset=UTF-8");
                     out.println("<!DOCTYPE html>");
                     out.println("<html>");
                     out.println("<body>");
                    out.println("<h3> Devi essere loggato per confermare l'email");
                    out.println("<br><br>");
                    out.println("<a href=\"login\">GO TO LOGIN</a>"); //DA CAMBIARE CHE VA DIRETTAMENTE ALLA PAGINA LOGIN
                    out.println("</body>");
                    out.println("</html>");
                } catch (IOException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    
}

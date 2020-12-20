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
public class LoginController extends BaseController {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        HttpSession s = request.getSession(true);
         
        if (request.getParameter("logout") == null) {
            
            if (s.getAttribute("email") != null && !((String) s.getAttribute("email")).isEmpty()){
                logged(request,response);
            }else{
                String email = request.getParameter("email");
                
                if (email == null || email.isEmpty()) {                       
                    login_action(request, response);
                }else{
                    try {
                        User user = ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getUser(email);
                        if(user != null && user.getPassword().equals(request.getParameter("password"))){
                            s.setAttribute("email", email);
                            logged(request, response);
                        }else{
                            action_error(request,response);
                        }
                    } catch (DataException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }       
        }else{
           s.invalidate();
           login_action(request, response); 
        }
        
        
         
        /*if (request.getParameter("email") == null){
            login_action(request,response);
        }else{
            try {
                User user = ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getUser(request.getParameter("email"));
                if(user != null && user.getPassword().equals(request.getParameter("password"))){
                    request.setAttribute("user",user);
                    logged(request,response);
                }else{
                    action_error(request,response);
                }
            } catch (DataException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } 
           
        }*/
            
        }

    private void login_action(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
        out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<center>");
            out.println("<h1> LOGIN </h1>");
            out.println("<br><br>");
            out.println("<form method=\"post\" action=\"login\">");
                /*out.println("<input type=\"text\" id=\"name\" name=\"name\" placeholder=\"Nome\"/>");
                out.println("<br><br>");
                out.println("<input type=\"text\" id=\"surname\" name=\"surname\" placeholder=\"Cognome\"/>");
                out.println("<br><br>");*/
                out.println("<input type=\"email\" id=\"email\" name=\"email\" placeholder=\"Email\"/>");
                out.println("<br><br>");
                out.println("<input type=\"password\" id=\"password\" name=\"password\" placeholder=\"Password\"/>");
                out.println("<br><br>");
                out.println("<input type=\"submit\" name=\"login\" value=\"LOGIN\"/>");
            out.println(" </center>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            
    }   catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logged(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()){
            
            HttpSession s = request.getSession(false);
            String email = (String) s.getAttribute("email");


            out.println("<h1> ciao " + email + "</h1>");
            out.println("<h2> login succesfull! </h2>");
            out.println("<a href=\"home\"> Home </a>");
            out.println("<p><a href=\"login?logout=1\">LOGOUT</a></p>");
               
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()){

                  User user = (User) request.getAttribute("user");
                      out.println("<h1> Login failed</h1>");
                      out.println("<a href=\"login\"> RIPROVA </a>");
                        //request.setAttribute("user", user);
                } catch (IOException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }

}

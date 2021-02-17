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
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

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

        HttpSession s = request.getSession(false);
        User loggedUser = null;
        if (s != null) {
            loggedUser = (User) s.getAttribute("user");
        }
        if (request.getParameter("logout") == null) {

            if (loggedUser != null && !(loggedUser.getKey()).isEmpty()) {

                logged(request, response);

            } else {
                String email = request.getParameter("email");

                if (email == null || email.isEmpty()) {
                    login_action(request, response);
                } else {
                    try {

                        User user = ((GuidatvDataLayer) request.getAttribute("datalayer")).getUserDAO().getUser(email);
                        if (user != null && BCrypt.checkpw(request.getParameter("password"), user.getPassword())) {
                            s = request.getSession(true);
                            s.setAttribute("user", user);

                            logged(request, response);

                        } else {
                            action_error(request, response);

                        }
                    } catch (DataException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } else {
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
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("login.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logged(HttpServletRequest request, HttpServletResponse response) {
        try {

            HttpSession s = request.getSession(false);
            User user = (User) s.getAttribute("user");

            if (user.getKey().equalsIgnoreCase("admin@email.it")) {
                response.sendRedirect("http://localhost:8080/Guida-tivu/admin");
            } else {
                response.sendRedirect("http://localhost:8080/Guida-tivu/home");
            }

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        try {

            request.setAttribute("loginerror", "Username e/o password errati");

            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("login.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

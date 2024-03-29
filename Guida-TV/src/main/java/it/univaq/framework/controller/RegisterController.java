package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
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
public class RegisterController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        HttpSession s = request.getSession(false);
        User loggedUser = null;
        if (s != null) {
            loggedUser = (User) s.getAttribute("user");
        }
        if (loggedUser != null && !(loggedUser.getKey()).isEmpty()) {
                logged(request, response);
        } else{
                if (request.getParameter("email") == null) {
                    action_default(request, response);
                } else {
                        register_action(request, response);
                }
          }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("register.ftl.html", request, response);
            /* TODO output your page here. You may use following sample code. */

 /*
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Registrazione</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<center>");
                out.println("<h1> REGISTRATI </h1>");
                out.println("<br><br>");
                out.println("<form method=\"post\" action=\"register\">");
                out.println("<input type=\"email\" id=\"email\" name=\"email\" placeholder=\"Email\"/>");
                out.println("<br><br>");
                out.println("<input type=\"password\" id=\"password\" name=\"password\" placeholder=\"Password\"/>");
                out.println("<br><br>");
                out.println("<label for=\"confermapw\">");
                out.println("<input type=\"password\" id=\"confermapw\" name=\"confermapw\" placeholder=\"Conferma Password\"/>");
                out.println("<br><br>");
                out.println("<input type=\"submit\" name=\"register\" value=\"REGISTRATI\"/>");
                out.println("<br><br>");
                out.println("<a href=\"login\"> LOGIN </a>");
                out.println(" </center>");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");  */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void register_action(HttpServletRequest request, HttpServletResponse response) {

        if (request.getParameter("email") != null && request.getParameter("password") != null && request.getParameter("confermapw") != null) {
            if (request.getParameter("password").equals(request.getParameter("confermapw"))) {
                try (PrintWriter out = response.getWriter()) {

                    User user = ((GuidatvDataLayer) request.getAttribute("datalayer")).getUserDAO().createUser();
                    user.setKey(request.getParameter("email"));
                    String hashPass = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt());
                    user.setPassword(hashPass);
                    String URI = UUID.randomUUID().toString();
                    user.setUri(URI);

                    ((GuidatvDataLayer) request.getAttribute("datalayer")).getUserDAO().storeUser(user);
                    user = ((GuidatvDataLayer) request.getAttribute("datalayer")).getUserDAO().getUser(request.getParameter("email"));
                    /*out.println("<h1> ciao " + user.getKey() + "</h1>");
                    out.println("<a href=\"login\"> LOGIN </a>");
                    out.println("<br>");
                    out.println("<br>");
                    out.println("<a href=\"confirm?URI="+URI+"\"> CONFERMA EMAIL </a>");
                    out.println("<a href=\"profile\"> Vai al profilo per confermare l'email </a>");*/
                    HttpSession s = request.getSession(true);
                    s.setAttribute("URI", URI);
                    TemplateResult res = new TemplateResult(getServletContext());
                    //request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                    request.setAttribute("info", "Registrazione effettuata! Per confermare l'email, vai sul profilo");
                    res.activate("login.ftl.html", request, response);
                    
                } catch (DataException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TemplateManagerException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                request.setAttribute("registererror", "Le password inserite non coincidono");      
            }
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
    
    }


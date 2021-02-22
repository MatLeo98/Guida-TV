package it.univaq.framework.controller;

import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.model.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author giorg
 */
public class ConfirmController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        
        try {
         TemplateResult res = new TemplateResult(getServletContext());
        HttpSession s = request.getSession(false);
        User user = (User) s.getAttribute("user");
        if (s != null && user.getKey() != null && !(user.getKey().isEmpty()) && request.getParameter("URI") != null){
            if(user.getUri().equals(request.getParameter("URI")))
                ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().setConfirmed(user.getKey());
            res.activate("confirmcontroller.ftl.html", request, response);
            
        }else{

                res.activate("loginrequired.ftl.html", request, response);
                /*
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

                */
                }
            } catch (TemplateManagerException ex) {
                Logger.getLogger(ConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }

    
}

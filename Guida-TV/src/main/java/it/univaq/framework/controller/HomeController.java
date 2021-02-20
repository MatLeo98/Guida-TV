package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
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
public class HomeController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {

            TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            //add to the template a wrapper object that allows to call the stripslashes function

            HttpSession session = request.getSession(false);

            if (session != null) {
                User user = (User) session.getAttribute("user");
                String email = user.getKey();
                request.setAttribute("email", email);
            }

            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("genres", Genre.values());

            request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
            request.setAttribute("onAirPrograms", ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getOnAirPrograms());
            res.activate("home.ftl.html", request, response);

            //action_home(request, response);   
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Home key not specified");
        } catch (DataException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ProgramImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Matteo
 */
public class ChannelDetailController extends BaseController {

    LocalDate date = LocalDate.now();

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("genres", ProgramImpl.Genre.values());

        try {
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));
            Channel channel = ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannel(id);
            request.setAttribute("channel", channel);
            request.setAttribute("schedule", ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getScheduleByChannel(channel, date));
            request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
            request.setAttribute("genres", ProgramImpl.Genre.values());
        } catch (DataException ex) {
            Logger.getLogger(ChannelDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        action_channel(request, response);

    }

    private void action_channel(HttpServletRequest request, HttpServletResponse response) {
        Channel channel = (Channel) request.getAttribute("channel");
        List<Schedule> schedule = (List<Schedule>) request.getAttribute("schedule");
        response.setContentType("text/html;charset=UTF-8");
        //request.setAttribute("image", channel.getImage());

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                String email = user.getKey();
                request.setAttribute("email", email);
            }
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("date", date);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channeldetails.ftl.html", request, response);
            //try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            //  out.println("<!DOCTYPE html>");
            /*  out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChannelDetailController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + channel.getName() + "</h1>");
            out.println("<h2>Palinsesto del " + date + "</h2>");
            for(Schedule s : schedule){
            out.println("<h3> <a href = 'program?id=" + s.getProgram().getKey() + "'>"  + s.getProgram().getName() + "</a></h3>");
            out.println("<h3>Ora di inizio: " + s.getStartTime() + "</h3>");
            out.println("<h3>Ora di fine: " + s.getEndTime() + "</h3>");
            out.println("<h3>Genere: " + s.getProgram().getGenre() + "</h3>");
            }
            out.println("</body>");
            out.println("</html>");
            } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

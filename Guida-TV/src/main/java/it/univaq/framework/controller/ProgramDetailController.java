package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ProgramImpl.Genre;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.util.List;
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
public class ProgramDetailController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        request.setAttribute("genres", Genre.values());

        try {
            request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));
            request.setAttribute("program", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(id));
            Program program = (Program) request.getAttribute("program");
            if (program.IsSerie()) {
                request.setAttribute("episodes", ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getLastMonthEpisodes(program));
                serie_detail(request, response);
            } else {
                program_detail(request, response);
            }
        } catch (DataException ex) {
            Logger.getLogger(ProgramDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void program_detail(HttpServletRequest request, HttpServletResponse response) {

        Program program = (Program) request.getAttribute("program");
        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                String email = user.getKey();
                request.setAttribute("email", email);
            }
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("programdetail.ftl.html", request, response);

            //try (PrintWriter out = response.getWriter()) {
            /*
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProgramDetailController</title>");            
            out.println("</head>");
            out.println("<body>");
            //out.println("<p><img src='image?imgid=2' alt='image1'/></p>");
            
            Image image = program.getImage();
            if(image != null){
            out.println("<p><img style='width:10%; height:5%;' src='"+program.getImage().getLink()+"' alt='image1'/></p>");
            }
            out.println("<h1> " + program.getName() + "</h1>");
            out.println("<p>" + program.getDescription() + "</p>");
            out.println("<p> Genere: " + program.getGenre() + "</p>");
            out.println("<a href=\"" + program.getLink() + "\"> Approfondisci  </a>");
            out.println("</body>");
            out.println("</html>");
             */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProgramDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void serie_detail(HttpServletRequest request, HttpServletResponse response) {

        Program program = (Program) request.getAttribute("program");
        List<Schedule> episodes = (List<Schedule>) request.getAttribute("episodes");

        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                String email = user.getKey();
                request.setAttribute("email", email);
            }
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("after", null);
            res.activate("programdetail.ftl.html", request, response);
            /*
            try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProgramDetailController</title>");            
            out.println("</head>");
            out.println("<body>");
            Image image = program.getImage();
            if(image != null){
            out.println("<p><img style='height:10%; width:10%;' src='"+program.getImage().getLink()+"' alt='image1'/></p>");
            }
            out.println("<h1> " + program.getName() + "</h1>");
            out.println("<p>" + program.getDescription() + "</p>");
            out.println("<p> Genere: " + program.getGenre() + "</p>");
            out.println("<a href=\"" + program.getLink() + "\"> Approfondisci  </a>");
            out.println("<h2> Episodi dell'ultimo mese </h2>");
            for(Schedule s : episodes){
            out.println("<h3> Episodio numero " + s.getEpisode().getNumber() + " - " + s.getEpisode().getName() + "</h3>");
            out.println("<p> Andato in onda: " + s.getDate() + " dalle ore: " + s.getStartTime() + " alle ore "+ s.getEndTime() + " ( <a href = 'channel?id=" + s.getChannel().getKey() + "'>" + s.getChannel().getName() + "</a> )</p>");
            }
            out.println("</body>");
            out.println("</html>");
             */
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProgramDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

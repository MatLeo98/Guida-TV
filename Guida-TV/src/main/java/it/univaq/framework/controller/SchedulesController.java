package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ProgramImpl;
import it.univaq.guidatv.data.impl.ScheduleImpl.TimeSlot;
import it.univaq.guidatv.data.model.User;
import java.time.LocalDate;
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
public class SchedulesController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            request.setAttribute("genres", ProgramImpl.Genre.values());
            request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());

            request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());

            if (request.getParameter("tsSelect") == null) {
                //TimeSlot timeSelected = TimeSlot.valueOf("sera");
                TimeSlot timeslot = ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getCurTimeSlot();
                request.setAttribute("timeslot", timeslot);
                request.setAttribute("schedules", ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getScheduleByTimeSlotDate(timeslot, LocalDate.now()));
                action_schedule(request, response);

            } else {

                TimeSlot timeSelected = TimeSlot.valueOf(request.getParameter("tsSelect"));
                request.setAttribute("timeslot", timeSelected);
                LocalDate dateSelected = LocalDate.parse(request.getParameter("dateSelect"));
                request.setAttribute("schedules", ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getScheduleByTimeSlotDate(timeSelected, dateSelected));
                action_schedule(request, response);

            }
            //request.setAttribute("schedule", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(1));

        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Article key not specified");

        } catch (DataException ex) {
            Logger.getLogger(SchedulesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void action_schedule(HttpServletRequest request, HttpServletResponse response) {
        /*List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
        TimeSlot[] timeslots = TimeSlot.class.getEnumConstants(); //VA FATTO IN UN METODO CREDO*/
        //Schedule schedule = (Schedule) request.getAttribute("schedule");
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate thirdDay = today.plusDays(2);
        request.setAttribute("today", today);
        request.setAttribute("tomorrow", tomorrow);
        request.setAttribute("thirdDay", thirdDay);
        TimeSlot[] timeslots = TimeSlot.class.getEnumConstants();
        request.setAttribute("timeslots", timeslots);
        //TimeSlot timeslot = (TimeSlot)request.getAttribute("timeslot");

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                String email = user.getKey();
                request.setAttribute("email", email);
            }
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("schedules.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(SchedulesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //response.setContentType("text/html;charset=UTF-8");
        /*try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Lista</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Palinsesto completo per ogni canale </h1>");
            out.println(" <form method=\"get\" action=\"schedules\">");
            out.println("<label for=\"tsSelect\">Scegli una fascia oraria:</label>");
            out.println("<select name=\"tsSelect\" id=\"tsSelect\">");
            out.println("<option value=\""+timeslot+"\">"+timeslot.toString()+"</option>");
            for(TimeSlot ts : timeslots){
                out.println("<option value=\""+ts+"\">"+ts.toString()+"</option>");
            }
            out.println("</select>");
            out.println("<br><br>");
            out.println("<label for=\"dateSelect\">Scegli un giorno:</label>");
            out.println("<select name='dateSelect' id='dateSelect'>");
            out.println("<option value='"+ today +"'> Oggi </option>");
            out.println("<option value='"+ tomorrow +"'> Domani </option>");
            out.println("<option value='"+ thirdDay +"'> Dopodomani </option>");
             out.println("</select>");
            out.println("<br><br>");
            out.println("<input type=\"submit\" name=\"s\"/>");
            out.println("</form>");
            
           // out.println("<h1>Servlet Lista at " + request.getContextPath() + "</h1>");
            for(Channel c : channels){
               
                out.println("<h2> <a href = 'channel?id=" + c.getKey() + "'>" + c.getName() + " </a> </h2>");
            
                for(Schedule s : schedules){
                    if( s.getChannel().getName().equals(c.getName())){
                        //out.println("<h1>Canale: " + s.getChannel().getName() + "</h1>");
                        out.println("<p>Programma: <a href = 'program?id=" + s.getProgram().getKey() + "'>" + s.getProgram().getName() +"</a></p>");
                        if(s.getEpisode() != null){
                            out.println("<p>Episodio numero: "+ s.getEpisode().getNumber() + " - "+ s.getEpisode().getName() +"</p>");
                        }
                        
                       
                        //String currentDate = Date.format(cals.getTime());
                        out.println("<p> Data: " + s.getDate() + "</p>");
                        out.println("<p> Ora inizio: " + s.getStartTime() + "</p>");
                        out.println("<p> Ora fine: " + s.getEndTime() + "</p>");
                        out.println("<br>");
                    }
                }
            }
            //out.println("<h1>Canale " + schedule.getChannel().getName() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleList.class.getName()).log(Level.SEVERE, null, ex);
        
        //response.setContentType("text/html;charset=UTF-8");
        /*try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Lista</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Palinsesto completo per ogni canale </h1>");
            out.println(" <form method=\"get\" action=\"schedules\">");
            out.println("<label for=\"tsSelect\">Scegli una fascia oraria:</label>");
            out.println("<select name=\"tsSelect\" id=\"tsSelect\">");
            out.println("<option value=\""+timeslot+"\">"+timeslot.toString()+"</option>");
            for(TimeSlot ts : timeslots){
                out.println("<option value=\""+ts+"\">"+ts.toString()+"</option>");
            }
            out.println("</select>");
            out.println("<br><br>");
            out.println("<label for=\"dateSelect\">Scegli un giorno:</label>");
            out.println("<select name='dateSelect' id='dateSelect'>");
            out.println("<option value='"+ today +"'> Oggi </option>");
            out.println("<option value='"+ tomorrow +"'> Domani </option>");
            out.println("<option value='"+ thirdDay +"'> Dopodomani </option>");
             out.println("</select>");
            out.println("<br><br>");
            out.println("<input type=\"submit\" name=\"s\"/>");
            out.println("</form>");
            
           // out.println("<h1>Servlet Lista at " + request.getContextPath() + "</h1>");
            for(Channel c : channels){
               
                out.println("<h2> <a href = 'channel?id=" + c.getKey() + "'>" + c.getName() + " </a> </h2>");
            
                for(Schedule s : schedules){
                    if( s.getChannel().getName().equals(c.getName())){
                        //out.println("<h1>Canale: " + s.getChannel().getName() + "</h1>");
                        out.println("<p>Programma: <a href = 'program?id=" + s.getProgram().getKey() + "'>" + s.getProgram().getName() +"</a></p>");
                        if(s.getEpisode() != null){
                            out.println("<p>Episodio numero: "+ s.getEpisode().getNumber() + " - "+ s.getEpisode().getName() +"</p>");
                        }
                        
                       
                        //String currentDate = Date.format(cals.getTime());
                        out.println("<p> Data: " + s.getDate() + "</p>");
                        out.println("<p> Ora inizio: " + s.getStartTime() + "</p>");
                        out.println("<p> Ora fine: " + s.getEndTime() + "</p>");
                        out.println("<br>");
                    }
                }
            }
            //out.println("<h1>Canale " + schedule.getChannel().getName() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(SchedulesController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}

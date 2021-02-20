package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.Schedule;
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
public class ScheduleController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);

        try {
            request.setCharacterEncoding("UTF-8");
             User user = (User) s.getAttribute("user");
            if(user.getKey().equals("admin@email.it")){
                if (request.getParameter("insert") != null) {
                    if (s.getAttribute("channelSelected") == null) {
                        try {
                            if (request.getParameter("ch") != null) {
                                try {
                                    Integer id = Integer.parseInt(request.getParameter("ch"));
                                    Channel cha = ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannel(id);
                                    request.setAttribute("channelSelected", cha);
                                    s.setAttribute("channelSelected", cha);
                                    request.setAttribute("episodes", ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().getAllEpisodes());
                                    request.setAttribute("programs", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                                } catch (DataException ex) {
                                    Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
                            schedule_insert(request, response);
                        } catch (DataException ex) {
                            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        insert_done(request, response);
                    }
                }

                if (request.getParameter("editdel") != null) {
                    int schedule_key;
                    LocalDate today = LocalDate.now();
                    if (request.getParameter("pName") == null) {
                        if (request.getParameter("ch") != null) { //verifico se ho scelto un elemento dal selettore 

                            Integer id = Integer.parseInt(request.getParameter("ch"));
                            Channel channel = ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannel(id);
                            request.setAttribute("channelSelected", channel);
                            request.setAttribute("schedules", ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().getScheduleByChannelAdmin(channel, today));
                            request.setAttribute("episodes", ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().getAllEpisodes());

                        }

                        if (request.getParameter("delSched") != null) {
                            delete_done(request, response);
                        }

                        request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
                        schedule_edit(request, response);

                    } else {
                        schedule_key = SecurityLayer.checkNumeric(request.getParameter("sk"));
                        request.setAttribute("key", schedule_key);
                        edit_done(request, response);
                    }
                }           
            }else{
                notAuth(request,response);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void schedule_insert(HttpServletRequest request, HttpServletResponse response) throws DataException {

        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());

            if (request.getParameter("ln") != null) {
                int ln = Integer.parseInt(request.getParameter("ln"));
                request.setAttribute("lNum", ln);
                List<Integer> rows = new ArrayList();
                for (int i = 1; i <= ln; i++) {
                    rows.add(i);
                }
                request.setAttribute("rows", rows);
            }
            res.activate("inseriscipalinsesto.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException n) {
            System.out.println("Programma ancora non selezionato");
        } catch (NumberFormatException nf) {
            System.out.println("Numero elementi ancora non selezionato");
        }
    }

    private void insert_done(HttpServletRequest request, HttpServletResponse response) {

        try {

            HttpSession s = request.getSession(false);
            int ln = Integer.parseInt(request.getParameter("nElem"));
            for (int i = 1; i <= ln; i++) {
                Schedule schedule = ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().createSchedule();
                Program program = null;
                if (request.getParameter("pr" + i).substring(0, 1).equals("e")) {
                    Integer e = Integer.parseInt(request.getParameter("pr" + i).substring(1));
                    Episode episode = ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(e);
                    schedule.setEpisode(episode);
                    program = episode.getProgram();
                } else {
                    Integer p = Integer.parseInt(request.getParameter("pr" + i));
                    program = ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(p);
                }
                Channel channel = (Channel) s.getAttribute("channelSelected");
                schedule.setProgram(program);
                schedule.setChannel(channel);

                schedule.setStartTime(LocalTime.parse(request.getParameter("start" + i)));
                schedule.setEndTime(LocalTime.parse(request.getParameter("end" + i)));
                schedule.setDate(LocalDate.parse(request.getParameter("date" + i)));

                ((GuidatvDataLayer) request.getAttribute("datalayer")).getScheduleDAO().storeSchedule(schedule);
            }

            s.setAttribute("channelSelected", null);
            request.setAttribute("var", 4);

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inserimentoriuscito.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void schedule_edit(HttpServletRequest request, HttpServletResponse response) throws DataException {

        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modificaschedule.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void edit_done(HttpServletRequest request, HttpServletResponse response) {
        try {

            int key = (int)request.getAttribute("key");
            Schedule schedule = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(key);
            if(schedule.getProgram().IsSerie()){
                int ep = Integer.parseInt(request.getParameter("ep"));
                Episode episode = ((GuidatvDataLayer)request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(ep);
                schedule.setEpisode(episode);
            }
            schedule.setDate(LocalDate.parse(request.getParameter("d")));            
            schedule.setStartTime(LocalTime.parse(request.getParameter("st")));
            schedule.setEndTime(LocalTime.parse(request.getParameter("et")));
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().storeSchedule(schedule);
            request.setAttribute("var", 4);

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modificariuscita.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void delete_done(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {

            Integer id = Integer.parseInt(request.getParameter("delSched"));
            Schedule schedule = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getSchedule(id);
            ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().deleteSchedule(schedule);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Delete</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Eliminazione effettuata </h1>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin/schedules?editdel=0'> Elimina di nuovo </a>");
            out.println("<br>");
            out.println("<br>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin'> Torna alla pagina admin </a>");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void notAuth(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("sonoin.ftl.html", request, response);
            response.setContentType("text/html;charset=UTF-8");

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

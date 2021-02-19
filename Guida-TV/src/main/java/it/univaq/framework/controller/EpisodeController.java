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
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.impl.ProgramImpl;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Episode;
import it.univaq.guidatv.data.model.Image;
import it.univaq.guidatv.data.model.Program;
import it.univaq.guidatv.data.model.Schedule;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author giorg
 */
public class EpisodeController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);

        try {
            request.setCharacterEncoding("UTF-8");
            if (request.getParameter("insert") != null) {
                if (s.getAttribute("programSelected") == null) {
                    try {
                        if (request.getParameter("pr") != null) {
                            
                                Integer id = Integer.parseInt(request.getParameter("pr"));
                                Program pro = ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(id);
                                request.setAttribute("programSelected", pro);
                                s.setAttribute("programSelected", pro);
                            
                        }
                        request.setAttribute("programs", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                        episode_insert(request, response);
                    } catch (DataException ex) {
                        Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    insert_done(request, response);
                }
            }

            if (request.getParameter("editdel") != null) {
                int episode_key;
                if (request.getParameter("sn") == null) {

                    if (request.getParameter("pr") != null) { //verifico se ho scelto un elemento dal selettore 

                        Integer id = Integer.parseInt(request.getParameter("pr"));
                        Program pro = ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(id);

                        request.setAttribute("programSelected", pro);
                        request.setAttribute("episodes", ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().getProgramEpisodes(pro));

                    }

                    if (request.getParameter("delEp") != null) {
                        delete_done(request, response);
                    }

                    request.setAttribute("programs", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                    episode_edit(request, response);

                } else {
                    episode_key = SecurityLayer.checkNumeric(request.getParameter("ek"));
                    request.setAttribute("key", episode_key);
                    edit_done(request, response);
                }
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void episode_insert(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
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
            res.activate("inserisciepisodio.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insert_done(HttpServletRequest request, HttpServletResponse response) {

        try {

            HttpSession s = request.getSession(false);
            int ln = Integer.parseInt(request.getParameter("nElem"));
            for (int i = 1; i <= ln; i++) {
                Episode episode = ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().createEpisode();
                episode.setName(request.getParameter("episodeName" + i));
                episode.setSeasonNumber(Integer.parseInt(request.getParameter("seasonNumber" + i)));
                episode.setNumber(Integer.parseInt(request.getParameter("episodeNumber" + i)));
                Program program = (Program) s.getAttribute("programSelected");
                episode.setProgram(program);
                ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().storeEpisode(episode);
            }
            s.setAttribute("programSelected", null);
            request.setAttribute("var", 3);

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inserimentoriuscito.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void episode_edit(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modificaepisodio.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void edit_done(HttpServletRequest request, HttpServletResponse response) {
        try {

            int key = (int) request.getAttribute("key");
            Episode episode = ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(key);
            episode.setName(request.getParameter("nm"));
            episode.setSeasonNumber(Integer.parseInt(request.getParameter("sn")));
            episode.setNumber(Integer.parseInt(request.getParameter("en")));
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().storeEpisode(episode);
            request.setAttribute("var", 3);

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modificariuscita.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void delete_done(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {

            Integer id = Integer.parseInt(request.getParameter("delEp"));
            Episode episode = ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().getEpisode(id);
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getEpisodeDAO().deleteEpisode(episode);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Delete</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Eliminazione effettuata </h1>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin/episodes?editdel=0'> Elimina di nuovo </a>");
            out.println("<br>");
            out.println("<br>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin'> Torna alla pagina admin </a>");
        } catch (IOException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(EpisodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

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
public class ProgramController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);

        try {
            request.setCharacterEncoding("UTF-8");
            if (request.getParameter("insert") != null) {
                if (request.getParameter("programName") == null) {
                    program_insert(request, response);
                } else {
                    insert_done(request, response);
                }
            }

            if (request.getParameter("edit") != null) {
                int program_key;
                if (request.getParameter("prog") == null) {

                    if (request.getParameter("pr") != null) { //verifico se ho scelto un elemento dal selettore 

                        Integer id = Integer.parseInt(request.getParameter("pr"));
                        request.setAttribute("programSelected", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(id));

                    }

                    request.setAttribute("programs", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                    program_edit(request, response);

                } else {
                    program_key = SecurityLayer.checkNumeric(request.getParameter("prog"));
                    request.setAttribute("key", program_key);
                    edit_done(request, response);
                }

            }

            if (request.getParameter("delete") != null) {
                if (request.getParameter("delPr") != null) {
                    delete_done(request, response);
                } else {
                    request.setAttribute("programs", ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getPrograms());
                    program_delete(request, response);
                }
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void program_insert(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        ProgramImpl.Genre[] genres = ProgramImpl.Genre.values();
        request.setAttribute("genres", genres);

        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inserisciprogramma.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insert_done(HttpServletRequest request, HttpServletResponse response) {

        try {

            Program program = ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().createProgram();
            program.setName(request.getParameter("programName"));
            program.setDescription(request.getParameter("programDescription"));
            program.setGenre(ProgramImpl.Genre.valueOf(request.getParameter("genre")));
            program.setLink(request.getParameter("link"));
            Image image = ((GuidatvDataLayer) request.getAttribute("datalayer")).getImageDAO().createImage();
            image.setLink(request.getParameter("image"));
            image = ((GuidatvDataLayer) request.getAttribute("datalayer")).getImageDAO().storeImage(image);
            program.setImage(image);
            program.setIsSerie(Boolean.valueOf(request.getParameter("serie")));
            if (program.IsSerie()) {
                program.setSeasonsNumber(Integer.parseInt(request.getParameter("nSeasons")));
            }
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().storeProgram(program);
            request.setAttribute("var", 2);

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("inserimentoriuscito.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void program_edit(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        ProgramImpl.Genre[] genres = ProgramImpl.Genre.values();
        request.setAttribute("genres", genres);

        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modificaprogramma.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void edit_done(HttpServletRequest request, HttpServletResponse response) {
        try {

            int key = (int) request.getAttribute("key");
            Program program = ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(key);
            program.setName(request.getParameter("programName"));
            program.setDescription(request.getParameter("programDescription"));
            program.setGenre(ProgramImpl.Genre.valueOf(request.getParameter("genre")));
            program.setLink(request.getParameter("link"));
            if (!(request.getParameter("image").equals(program.getImage().getLink()))) {
                Image image = program.getImage();
                image.setLink(request.getParameter("image"));
                image = ((GuidatvDataLayer) request.getAttribute("datalayer")).getImageDAO().storeImage(image);
            }
            if (program.IsSerie()) {
                program.setSeasonsNumber(Integer.parseInt(request.getParameter("nSeasons")));
            }
            System.out.println("Numero stagioni:" + program.getSeasonsNumber());
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().storeProgram(program);
            request.setAttribute("var", 2);

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modificariuscita.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void program_delete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        List<Program> programs = (List<Program>) request.getAttribute("programs");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Delete</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Elimina un programma: </h1>");
            out.println("<table style='width:100%'>");
            out.println("<tr>");
            out.println("<th>Nome programma</th>");
            out.println("<th>Descrizione</th>");
            out.println("<th>Genere</th>");
            out.println("<th>Link</th>");
            out.println("<th>Elimina</th>");
            out.println("</tr>");
            if (programs != null) {
                for (Program p : programs) {
                    out.println("<tr>");

                    out.println("<td style='text-align:center' hidden>" + p.getKey() + "  </td>");
                    out.println("<td style='text-align:center'> " + p.getName() + " </td>");
                    out.println("<td style='text-align:center'> " + p.getDescription() + " </td>");
                    out.println("<td style='text-align:center'> " + p.getGenre() + " </td>");
                    out.println("<td style='text-align:center'> " + p.getLink() + " </td>");

                    out.println("<td style='text-align:center'>");
                    out.println("<form method='post' action='programs?delete=0&delPr=" + p.getKey() + "'>");
                    out.println("<input type='submit' name='delete' value='ELIMINA'/>");
                    out.println("</form>");
                    out.println("</td>");

                    out.println("</tr>");
                }
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void delete_done(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {

            Integer n = Integer.parseInt(request.getParameter("delPr"));
            Program program = ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().getProgram(n);
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getProgramDAO().deleteProgram(program);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Delete</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Eliminazione effettuata </h1>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin/programs?delete=0'> Elimina di nuovo </a>");
            out.println("<br>");
            out.println("<br>");
            out.println("<a href='http://localhost:8080/Guida-tivu/admin'> Torna alla pagina admin </a>");
        } catch (IOException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

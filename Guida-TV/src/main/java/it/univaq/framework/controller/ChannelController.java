package it.univaq.framework.controller;

import it.univaq.framework.data.DataException;
import it.univaq.framework.result.SplitSlashesFmkExt;
import it.univaq.framework.result.TemplateManagerException;
import it.univaq.framework.result.TemplateResult;
import it.univaq.framework.security.SecurityLayer;
import it.univaq.guidatv.data.dao.GuidatvDataLayer;
import it.univaq.guidatv.data.model.Channel;
import it.univaq.guidatv.data.model.Image;
import it.univaq.guidatv.data.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
public class ChannelController extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);

        try {
            request.setCharacterEncoding("UTF-8");
            User user = (User) s.getAttribute("user");
            if (user.getKey().equals("admin@email.it")) {
                if (request.getParameter("insert") != null) {
                    if (request.getParameter("channelNumber") == null) {
                        channel_insert(request, response);
                    } else {
                        insert_done(request, response);
                    }
                }

                if (request.getParameter("edit") != null) {
                    int channel_key;
                    if (request.getParameter("channelNumber") == null) {
                        if (request.getParameter("ch") != null) { //verifico se ho scelto un elemento dal selettore 

                            Integer id = Integer.parseInt(request.getParameter("ch"));
                            request.setAttribute("channelSelected", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannel(id));

                        }

                        request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
                        channel_edit(request, response);

                    } else {
                        channel_key = SecurityLayer.checkNumeric(request.getParameter("channelNumber"));
                        request.setAttribute("key", channel_key);
                        edit_done(request, response);
                    }
                }

                if (request.getParameter("delete") != null) {
                    if (request.getParameter("delCh") != null) {
                        delete_done(request, response);
                    } else {
                        request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());
                        channel_delete(request, response);
                    }
                }
            } else {
                notAuth(request, response);
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void channel_insert(HttpServletRequest request, HttpServletResponse response) {

        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channelinsert.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insert_done(HttpServletRequest request, HttpServletResponse response) {

        try {

            Integer n = Integer.parseInt(request.getParameter("channelNumber"));
            Channel channel = ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().createChannel();
            channel.setKey(n);
            channel.setName(request.getParameter("channelName"));
            Image image = ((GuidatvDataLayer) request.getAttribute("datalayer")).getImageDAO().createImage();
            image.setLink(request.getParameter("image"));
            image = ((GuidatvDataLayer) request.getAttribute("datalayer")).getImageDAO().storeImage(image);
            channel.setImage(image);
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().storeChannel(channel);
            request.setAttribute("var", 1);
            
            request.setAttribute("insertSuccess", "Canale inserito");

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channelinsert.ftl.html", request, response);
           
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void channel_edit(HttpServletRequest request, HttpServletResponse response) {

        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channeledit.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void edit_done(HttpServletRequest request, HttpServletResponse response) {
        try {

            Integer n = Integer.parseInt(request.getParameter("channelNumber"));
            Channel channel = ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannel(n);
            channel.setName(request.getParameter("channelName"));
            if (!(request.getParameter("image").equals(channel.getImage().getLink()))) {
                if(request.getParameter("image") != ""){
                    Image image = channel.getImage();
                    image.setLink(request.getParameter("image"));
                    image = ((GuidatvDataLayer) request.getAttribute("datalayer")).getImageDAO().storeImage(image);
                }
            }
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().storeChannel(channel);
            
             request.setAttribute("editSuccess", "Canale modificato");
             request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channeledit.ftl.html", request, response);


        } catch (DataException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void channel_delete(HttpServletRequest request, HttpServletResponse response) {

        try {

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channeldelete.ftl.html", request, response);

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void delete_done(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {

            Integer n = Integer.parseInt(request.getParameter("delCh"));
            Channel channel = ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannel(n);
            ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().deleteChannel(channel);

          
             request.setAttribute("channels", ((GuidatvDataLayer) request.getAttribute("datalayer")).getChannelDAO().getChannels());

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("channeldelete.ftl.html", request, response);
            
        } catch (IOException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(ChannelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void notAuth(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("notauthorized.ftl.html", request, response);
            response.setContentType("text/html;charset=UTF-8");

        } catch (TemplateManagerException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

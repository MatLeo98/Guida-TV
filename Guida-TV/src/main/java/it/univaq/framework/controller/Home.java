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
import it.univaq.guida.tv.data.dao.GuidatvDataLayer;
import it.univaq.guida.tv.data.impl.ProgramImpl.Genre;
import it.univaq.guida.tv.data.model.Channel;
import it.univaq.guida.tv.data.model.FavouriteChannel;
import it.univaq.guida.tv.data.model.FavouriteProgram;
import it.univaq.guida.tv.data.model.Schedule;
import it.univaq.guida.tv.data.model.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Job;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.SimpleTrigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author giorg
 */
public class Home extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
  
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{  
        
      /*HttpSession s = request.getSession(true);
       s.setAttribute("datalayer",((GuidatvDataLayer)request.getAttribute("datalayer")));*/
      
      
       /* Timer timer = new Timer();
        timer.schedule(sendEmail(request,response), 0, 5000);    */
        
       
       //ULTIMO FUNZIONANTE
        /*ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        try {
            executor.scheduleAtFixedRate(sendEmail(request,response), 0, 5, TimeUnit.SECONDS);
         ///////////  
            
            /*JobDetail job = new JobDetail();
            job.setName("dummyJobName");
            job.setJobClass(HelloJob.class);
            
            //configure the scheduler time
            SimpleTrigger trigger = new SimpleTrigger();
            trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
            trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
            trigger.setRepeatInterval(30000);
            
            //schedule it
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);*/
       /* } catch (DataException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
       
        
        try {  
            
        
           TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            //add to the template a wrapper object that allows to call the stripslashes function

            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("genres", Genre.values());
            request.setAttribute("channels", ((GuidatvDataLayer)request.getAttribute("datalayer")).getChannelDAO().getChannels());
            request.setAttribute("onAirPrograms", ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getOnAirPrograms());
            res.activate("home.ftl.html", request, response);
            
            //action_home(request, response);   
            
            
            
        
        
        
            
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Home key not specified");           
        } catch (DataException ex) { 
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
    
    

    private void action_home(final HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        System.out.println("hola");
        
        
          
          
          
          
          
        
       
        /* Scheduler scheduler;
         
         
            scheduler = StdSchedulerFactory.getDefaultScheduler();
           HttpSession s = request.getSession(false);
           s.setAttribute("datalayer", ((GuidatvDataLayer)request.getAttribute("datalayer")));
            
          
            scheduler.getContext().put("session", s);
            scheduler.getContext().put("request", request);
            
            scheduler.start();
        
            JobDetail job = newJob(SendEmailJob.class)
                            .withIdentity("SendEmail")
                            .build();
        
            SimpleTrigger trigger = newTrigger().withIdentity("trigger1")
                                    .startNow()
                                    .withSchedule(simpleSchedule().withIntervalInSeconds(30).repeatForever())
                                    .build();
            
            
            scheduler.scheduleJob(job, trigger);
            
            
            
            /*JobDetail job = new JobDetail();
    	job.setName("dummyJobName");
    	job.setJobClass(HelloJob.class);
    	
    	//configure the scheduler time
    	SimpleTrigger trigger = new SimpleTrigger();
    	trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
    	trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    	trigger.setRepeatInterval(30000);
    	
    	//schedule it
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);
                    
                    
        /*Genre[] genres = Genre.values();
        List<Schedule> onAirPrograms = (List<Schedule>) request.getAttribute("onAirPrograms");
        List<Channel> channels = (List<Channel>) request.getAttribute("channels");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Home</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<form action='searchresults'>");
            out.println("<input type='text' placeholder='Titolo' name='title'>");
            out.println("<select name='genre' id='genre'>");
            out.println("<option value = ''> Scegli il genere </option>");
            for(Genre g : genres){
            out.println("<option value = '" + g + "'>" + g + "</option>");
            }
            out.println("</select>");
            out.println("<select name='channel' id='channel'>");
            out.println("<option value = ''> Scegli i canali </option>");
            for(Channel c : channels){
            out.println("<option value = '" + c.getName() + "'>" + c.getName() + "</option>");
            }
            out.println("</select>");
            out.println("Ora Min:");
            out.println("<input type='time' id='min' name='min'>");
            out.println("Ora Max:");
            out.println("<input type='time' id='max' name='max'>");
            out.println("Da:");
            out.println("<input type='date' id='date1' name='date1'>");
            out.println("A:");
            out.println("<input type='date' id='date2' name='date2'>");
            out.println("<button type='submit'>Search</button>");
            out.println("</form>");
            out.println("<h1>Programmi in onda </h1>");
            for(Schedule s : onAirPrograms){
            out.println("<h3> Ora in onda: <a href = 'program?id=" + s.getProgram().getKey() + "'>" + s.getProgram().getName() + "</a></h3>");
            out.println("<h1> <a href = 'channel?id=" + s.getChannel().getKey()+ "'>" + s.getChannel().getName() + " </a></h1>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

   /* public static class SendEmailJob extends BaseController implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException{
            
            
             SchedulerContext schedulerContext = null;
            try {
                schedulerContext = context.getScheduler().getContext();
                HttpSession session =(HttpSession) schedulerContext.get("session");
                
                HttpServletRequest request =(HttpServletRequest) schedulerContext.get("request");
                request.setAttribute("datalayer",(GuidatvDataLayer)session.getAttribute("datalayer"));
                request.setAttribute("bella","bella");
                
                System.out.println("request:" +request.getAttribute("bella"));
                System.out.println("datalayer:" +(GuidatvDataLayer)request.getAttribute("datalayer"));
            
             //users = null;
             request.setAttribute("users",((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getSubUsers());
            List<User> users = (List<User>) request.getAttribute("users");
            System.out.println(users.get(0).getKey());
            for(User u : users){
                System.out.println(u.getKey());
                FileWriter fw=new FileWriter("S:\\Programmi\\xampp\\htdocs\\univaq-Guida-TV\\Guida-TV\\Guida-TV\\src\\main\\java\\it\\univaq\\guida\\tv\\data\\files\\emailto"+u.getKey()+LocalDate.now()+".txt");
                fw.write("Ciao "+u.getKey()+"\r\n\r\n");
                
                //schedules di oggi per canali preferiti
                List<FavouriteChannel> channels = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().getFavouriteChannels(u);
                if(!channels.isEmpty()){
                    fw.write("Ecco i programmi che andranno in onda oggi in base ai tuoi canali preferiti: \r\n");
                    for(FavouriteChannel fc : channels){
                        List<Schedule> todayByC = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByFavChannel(fc.getChannel(),LocalDate.now(),fc.getTimeSlot());
                        for(Schedule s : todayByC){


                              fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+ "\r\n");  
                               if(s.getProgram().IsSerie())
                                    fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");
                                    System.out.println("Ciao "+u.getKey()+"Ecco i programmi che andranno in onda oggi in base ai tuoi canali preferiti: "+s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+ "" );
                        } 
                        
                    }
                }
                //schedules di oggi per programmi preferiti che hanno la newsletter per la ricerca salvata
                List<FavouriteProgram> programs = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().getFavouritePrograms(u);
                
                if(!programs.isEmpty()){
                    fw.write("\r\nEcco i programmi che andranno in onda oggi in base alle tue ricerche salvate: \r\n");
                
                
                    for(FavouriteProgram fp : programs){
                        if(fp.getSavedSearch().getSendEmail()){
                            List<Schedule> todayByP = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByProgram(fp.getProgram(),LocalDate.now());
                            for(Schedule s : todayByP){

                                fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+"\r\n"); 
                                if(s.getProgram().IsSerie())
                                    fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");

                            }
                            
                        }
                    }
                }
                
                fw.close();
                
            }
      
             
        }catch (DataException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchedulerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        }
        
        public void sendemail(HttpServletRequest request){
            
            
        }

        @Override
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }*/

    /*private TimerTask sendEmail(HttpServletRequest request, HttpServletResponse response) {
        
        System.out.println("ciao");
        try {
            List<User> users = null;
            users = (List<User>) ((GuidatvDataLayer)request.getAttribute("datalayer")).getUserDAO().getSubUsers();
            for(User u : users){
                FileWriter fw=new FileWriter("S:\\Programmi\\xampp\\htdocs\\univaq-Guida-TV\\Guida-TV\\Guida-TV\\src\\main\\java\\it\\univaq\\guida\\tv\\data\\files\\emailto"+u.getKey()+LocalDate.now()+".txt");
                fw.write("Ciao "+u.getKey()+"\r\n\r\n");
                
                //schedules di oggi per canali preferiti
                List<FavouriteChannel> channels = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteChannelDAO().getFavouriteChannels(u);
                if(!channels.isEmpty()){
                    fw.write("Ecco i programmi che andranno in onda oggi in base ai tuoi canali preferiti: \r\n");
                    for(FavouriteChannel fc : channels){
                        List<Schedule> todayByC = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByFavChannel(fc.getChannel(),LocalDate.now(),fc.getTimeSlot());
                        for(Schedule s : todayByC){


                              fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+ "\r\n");  
                               if(s.getProgram().IsSerie())
                                    fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");

                        }    
                    }
                }
                //schedules di oggi per programmi preferiti che hanno la newsletter per la ricerca salvata
                List<FavouriteProgram> programs = ((GuidatvDataLayer)request.getAttribute("datalayer")).getFavouriteProgramDAO().getFavouritePrograms(u);
                
                if(!programs.isEmpty()){
                    fw.write("\r\nEcco i programmi che andranno in onda oggi in base alle tue ricerche salvate: \r\n");
                
                
                    for(FavouriteProgram fp : programs){
                        if(fp.getSavedSearch().getSendEmail()){
                            List<Schedule> todayByP = ((GuidatvDataLayer)request.getAttribute("datalayer")).getScheduleDAO().getScheduleByProgram(fp.getProgram(),LocalDate.now());
                            for(Schedule s : todayByP){

                                fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+"\r\n"); 
                                if(s.getProgram().IsSerie())
                                    fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");

                            }
                        }
                    }
                }
                
                fw.close();
            }
            System.out.println("fatto");
        } catch (DataException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    private Runnable sendEmail(final HttpServletRequest request, HttpServletResponse response) throws DataException {
        
        HttpSession s = request.getSession(false);
        final GuidatvDataLayer datalayer = (GuidatvDataLayer) s.getAttribute("datalayer");
        final List<User> users = (List<User>) datalayer.getUserDAO().getSubUsers();
        
        Runnable helloRunnable;
        helloRunnable = new Runnable() {
            public void run() {
                System.out.println("ciao");
                try {
                    
                    if(!users.isEmpty())
                    for(User u : users){
                        FileWriter fw=new FileWriter("S:\\Programmi\\xampp\\htdocs\\univaq-Guida-TV\\Guida-TV\\Guida-TV\\src\\main\\java\\it\\univaq\\guida\\tv\\data\\files\\emailto"+u.getKey()+LocalDate.now()+".txt");
                        fw.write("Ciao "+u.getKey()+"\r\n\r\n");
                        System.out.println("email" + u.getKey());
                        //schedules di oggi per canali preferiti
                        List<FavouriteChannel> channels = datalayer.getFavouriteChannelDAO().getFavouriteChannels(u);
                        if(!channels.isEmpty()){
                            fw.write("Ecco i programmi che andranno in onda oggi in base ai tuoi canali preferiti: \r\n");
                            for(FavouriteChannel fc : channels){
                                List<Schedule> todayByC = datalayer.getScheduleDAO().getScheduleByFavChannel(fc.getChannel(),LocalDate.now(),fc.getTimeSlot());
                                for(Schedule s : todayByC){
                                    
                                    
                                    fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+ "\r\n");
                                    if(s.getProgram().IsSerie())
                                        fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");
                                    
                                }
                            }
                        }
                        //schedules di oggi per programmi preferiti che hanno la newsletter per la ricerca salvata
                        List<FavouriteProgram> programs = datalayer.getFavouriteProgramDAO().getFavouritePrograms(u);
                        
                        if(!programs.isEmpty()){
                            fw.write("\r\nEcco i programmi che andranno in onda oggi in base alle tue ricerche salvate: \r\n");
                            
                            
                            for(FavouriteProgram fp : programs){
                                if(fp.getSavedSearch().getSendEmail()){
                                    List<Schedule> todayByP = datalayer.getScheduleDAO().getScheduleByProgram(fp.getProgram(),LocalDate.now());
                                    for(Schedule s : todayByP){
                                        
                                        fw.write(s.getChannel().getName() + " alle "+ s.getStartTime() +" - "+ s.getProgram().getName()+"\r\n");
                                        if(s.getProgram().IsSerie())
                                            fw.write("Episodio numero: "+s.getEpisode().getNumber()+" "+s.getEpisode().getName()+"\r\n");
                                        
                                    }
                                }
                            }
                        }
                        
                        fw.close();
                    }
                    System.out.println("fatto");
                } catch (DataException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        };
                
            
        return helloRunnable;
    }
      
    

}
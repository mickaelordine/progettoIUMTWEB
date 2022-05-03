package com.example.progetto1;

import com.example.progetto1.DBInteraction.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/*
getParameter() returns http request parameters. Those passed from the client to the server.
For example http://example.com/servlet?parameter=1. Can only return String

getAttribute() is for server-side usage only - you fill the request with attributes that
you can use within the same request. For example - you set an attribute in a servlet, and
read it from a JSP. Can be used for any object, not just string

la HttpSession usa solo getAttribute
 */


@WebServlet(name = "servlet1", value = "/servlet1")
public class Servlet1 extends HttpServlet{
    Gson jsonIzer = new Gson();
    String user, psw;
    DBA dao;
    String initExc = "no";
    boolean initExcChecked = true;
    PrintWriter out = null;

    void manageServletException(ServletException e){
        String method = (e.getStackTrace())[0].getMethodName();
        // todo gestire le eccezioni meglio perchè dio fa,
        //  per farlo mettere dei messaggi semplici nelle
        //  eccezioni e poi quando chiamo i metodi catturarle
        //  e leggere i messaggi nella servlet e in base a
        //  cosa leggo mettere valori diversi nella response
        //  e poi interperetarli nel frontend
    }

    void dp(String text){
        System.out.println(text);
        if(out!=null){
            out.print(text);
        }
    }

    public void init(){
        System.out.println("servlet 1 - init");
        try {
            dao = new DBA();
            } catch (ServletException e) {
            //todo questa dovrebbe poi essere sostituita dal manageException
            initExc = e.getMessage();
            initExcChecked = false;
        }
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("____________________________________________________________________________");
        System.out.println("\t/\t/\t/\t/\t/\t/\t/\tNEW GET REQUEST\t\\\t\\\t\\\t\\\t\\\t\\\t\\");
        HttpSession s = req.getSession();
        processRequest(req, resp);
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("____________________________________________________________________________");
        System.out.println("\t/\t/\t/\t/\t/\t/\t/\tNEW POST REQUEST\t\\\t\\\t\\\t\\\t\\\t\\\t\\");
        HttpSession s = req.getSession();
        processRequest(req, resp);
    }

    //todo aggiornare con i nuovi metodi

    //l'azione da fare va inserita in un parameter "action" e il risultato viene messo nella response
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        out = resp.getWriter();
        resp.setContentType("text/plain");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        if(!initExcChecked){
            System.out.println("Exc init ");
            dp(initExc);
            initExcChecked = true;
        } else {

        Enumeration<String> params = req.getParameterNames();
        System.out.println("Request parameters:");
        while(params.hasMoreElements()){
            String current = params.nextElement();
            System.out.println("\t" + current + ": " + req.getParameter(current));
        }

        String action = req.getParameter("action");
        System.out.println("in processRequest: action "+ action);

        switch(action){
            case "viewPrenotabili":{
                resp.setContentType("application/json");
                resp.setStatus(200);
                out.print(jsonIzer.toJson(dao.prenotabili()));
                break;
            }
            case "viewFiltered":{
                String filter = req.getParameter("filter");
                if(filter!=null){
                    resp.setContentType("application/json");
                    resp.setStatus(200);
                    out.print(jsonIzer.toJson(dao.jsonQryRip(filter)));
                } else System.out.println("no filter received");
                break;
            }
            case "login":{      //login - FATTO
                String un = req.getParameter("username");
                String psw = req.getParameter("password");
                String authOut = dao.auth(un, psw);
                resp.setStatus(200);
                out.print(authOut);
                if(dao.getRole().equals("user") || dao.getRole().equals("admin")){
                    HttpSession s = req.getSession();
                    s.setAttribute("username", dao.getUsername());
                    s.setAttribute("role", dao.getRole());
//                    String sessionID = s.getId();
//                    String url = resp.encodeURL("servlet1");
//                    System.out.println(url);
                    System.out.println("session tracked: role " + s.getAttribute("role"));
                    dao.updMyArchive();
                }
                break;
            }
            case "prenota":{    //prenota rip - FATT0
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("user") || role.equals("admin")){
                    String rdpJson = req.getParameter("rdp");
                    if(rdpJson != null){
                        Ripetizioni rdp = jsonIzer.fromJson(rdpJson, Ripetizioni.class);
                        System.out.println(dao.getUsername() + " vuole prenotare la ripetizione " + rdp);
                        int insTuple = 0;
                        try{
                            insTuple = dao.prenota(rdp);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        if(insTuple == 1){
                            dp("prenota: OK");
                        } else {
                            resp.setStatus(500);
                            dp("prenota: FAIL");
                            //todo secondo me qua c'è da inserire la gestione per bene delle
                            // eccezioni, tipo che stampo anche quello che dice l'eccezione
                            // in modo che il frontend sa bene cosa fare e cosa dire all'user
                            // (tutta la parte di gestione del feedback che deve arrivare all'user)
                        }
                    } else System.out.println("ripetizione mancante");
                } else System.out.println("no permission");
                break;
            }
            case "disdici":{    //disdici pren - FATTO
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    String pddJson = req.getParameter("pdd");
                    if(pddJson != null){
                        Prenotazione pdd = jsonIzer.fromJson(pddJson, Prenotazione.class);
                        if(dao.disdici(pdd) == 1){
                            dp("disdici: OK");
                        } else {
                            dp("disdici: FAIL");
                            //eccezioni
                        }
                    } else dp("prenotazione mancante");
                } else dp("no permit to do this op");
                break;
            }
            case "setDone":{    //disdici pren - FATTO
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    String prenJson = req.getParameter("pdd");
                    if(prenJson != null){
                        Prenotazione pren = jsonIzer.fromJson(prenJson, Prenotazione.class);
                        if(dao.setDone(pren) == 1){
                            dp("setDone: OK");
                        } else {
                            dp("setDone: FAIL");
                            //eccezioni
                        }
                    } else dp("prenotazione mancante");
                } else dp("no permit to do this op");
                break;
            }

            case "myPrenotazioni":{  //mostra le mie prenotazioni - FATTO
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    resp.setContentType("application/json");
                    resp.setStatus(200);
                    dao.updMyArchive();
                    out.print(jsonIzer.toJson(dao.getMyArchive()));
                } else dp("no permission");
                break;
            }
            case "addProfessore":{       //admin: inserisci prof
                String role = dao.getRole();
                if(role.equals("admin")){
                    String profNome = req.getParameter("profNome");
                    String profCogn = req.getParameter("profCogn");
                    if(profNome == null || profCogn == null){
                        System.out.println("nome o cognome null");
                    } else {
                        if(dao.insertProf(profNome, profCogn) == 1)
                            dp("insProf: OK");
                        else
                            dp("insProf: FAIL");
                    }
                } else {
                    dp("not an admin");
                }
                break;
            }
            case "removeProfessore":{       //admin: remove prof
                String role = dao.getRole();
                if(role.equals("admin")){
                    String profNome = req.getParameter("profNome");
                    String profCogn = req.getParameter("profCogn");
                    if(profNome == null || profCogn == null){
                        System.out.println("nome o cognome null");
                    } else {
                        if(dao.removeProf(profNome, profCogn) == 1)
                            dp("removeProf: OK");
                        else
                            dp("removeProf: FAIL");
                    }
                } else {
                    dp("not an admin");
                }
                break;
            }
            case "addCorso":{       //admin: inserisci corso
                String role = dao.getRole();
                if(role.equals("admin")){
                    String corso = req.getParameter("corso");
                    if(corso == null){
                        System.out.println("nome null");
                    } else {
                        if(dao.insertCorso(corso) == 1)
                            dp("insCorso: OK");
                        else
                            dp("insCorso: FAIL");
                    }
                } else {
                    dp("not an admin");
                }
                break;
            }
            case "removeCorso":{       //admin: remove corso
                String role = dao.getRole();
                if(role.equals("admin")){
                    String corso = req.getParameter("corso");
                    if(corso == null){
                        System.out.println("nome null");
                    } else {
                        if(dao.removeCorso(corso) == 1)
                            dp("removeCorso: OK");
                        else
                            dp("removeCorso: FAIL");
                    }
                } else {
                    dp("not an admin");
                }
                break;
            }
            case "addInsegnamento":{       //admin: inserisci insegnamneto
                String role = dao.getRole();
                if(role.equals("admin")){
                    String prof = req.getParameter("prof");
                    String corso = req.getParameter("corso");
                    if(corso == null || prof == null){
                        dao.insertInsegnamento(jsonIzer.fromJson(corso, String.class) ,
                                            jsonIzer.fromJson(prof, Professore.class));
                    } else {
                        System.out.println("corso o prof null");
                    }
                } else {
                    dp("not an admin");
                }

                break;
            }
            case "removeInsegnamento":{       //admin: inserisci insegnamneto
                String role = dao.getRole();
                if(role.equals("admin")){
                    String prof = req.getParameter("prof");
                    String corso = req.getParameter("corso");
                    if(corso == null || prof == null){
                        int result = dao.removeInsegnamento(jsonIzer.fromJson(corso, String.class) ,
                                jsonIzer.fromJson(prof, Professore.class));
                        if(result == 1)
                            dp("removeInsegnamento: OK");
                        else
                            dp("removeInsegnamento: OK");
                    } else {
                        System.out.println("corso o prof null");
                    }
                } else {
                    dp("not an admin");
                }

                break;
            }
            case "viewAllPrenot":{      //admin: view all prenotazioni
                String role = dao.getRole();
                if(role.equals("admin")){
                    out.print(jsonIzer.toJson(dao.getAllPrenot()));
                    dp("allPrenot: sent");
                } else {
                    dp("no permission");
                }

                break;
            }
        }
        System.out.println("\t\\\t\\\t\\\t\\\t\\\t\\\t\\\tEND REQUEST\t/\t/\t/\t/\t/\t/\t/\t " +
                "\n____________________________________________________________________________");

        out.close();
        }
    }
}


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

    void manageServletException(ServletException e){
        String method = (e.getStackTrace())[0].getMethodName();
    }

    void dp(String text, PrintWriter pw){
        System.out.println(text);
        pw.print(text);
    }

    public void init(){
        System.out.println("servlet 1 - init");
        try {
            dao = new DBA();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("servlet1 - in doGet");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        HttpSession s = req.getSession();
        processRequest(req, resp);
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("servlet1 - in doPost");
        HttpSession s = req.getSession();
        processRequest(req, resp);
    }

    //l'azione da fare va inserita in un attribute "action" e il risultato viene messo in un attribute "result"
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");

        Enumeration<String> params = req.getParameterNames();
        while(params.hasMoreElements()){
            String current = params.nextElement();
            System.out.println(current + ": " + req.getParameter(current));
        }
        System.out.println("in processRequest: action "+ action);

        PrintWriter out = resp.getWriter();
        resp.setStatus(200);
        resp.setContentType("text/plain");


        switch(action){
            case "viewPrenotabili":{
                resp.setContentType("application/json");
                out.print(jsonIzer.toJson(dao.prenotabili()));
                break;
            }
            case "viewFiltered":{
                String filter = req.getParameter("filter");
                if(filter!=null){
                    resp.setContentType("application/json");
                    out.print(jsonIzer.toJson(dao.jsonQryRip(filter)));
                } else System.out.println("no filter received");
                break;
            }
            case "login":{      //login - FATTO
                String un = req.getParameter("username");
                String psw = req.getParameter("password");
                HttpSession s = req.getSession();
                String authOut = dao.auth(un, psw);
                out.print(authOut);
                if(dao.getRole().equals("user") || dao.getRole().equals("admin")){
                    s.setAttribute("username", dao.getUsername());
                    s.setAttribute("role", dao.getRole());
                    System.out.println("session tracked");
                    //dao.updMyArchive();
                }
                break;
            }

            case "getToken":{
                HttpSession s = req.getSession();
                String token = s.getId();
                System.out.println(token);
                out.print(token);
                break;
            }

            case "prenota":{    //prenota rip - FATT0
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    String rdpJson = req.getParameter("rdp");
                    if(rdpJson != null){
                        Ripetizioni rdp = jsonIzer.fromJson(rdpJson, Ripetizioni.class);
                        if(dao.prenota(rdp) == 1){
                            out.print("prenota: OK");
                            dp("", out);
                        } else {
                            dp("prenota: FAIL", out);
                            //todo secondo me qua c'è da inserire la gestione per bene delle
                            // eccezioni, tipo che stampo anche quello che dice l'eccezione
                            // in modo che il frontend sa bene cosa fare e cosa dire all'user
                            // (tutta la parte di gestione del feedback che deve arrivare all'user)
                        }
                    } else dp("ripetizione mancante", out);
                } else dp("no permission", out);
                break;
            }

            case "disdici":{    //disdici pren - FATTO
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    String pddJson = req.getParameter("pdd");
                    if(pddJson != null){
                        Prenotazione pdd = jsonIzer.fromJson(pddJson, Prenotazione.class);
                        System.out.println(pdd.toString());
                        if(dao.disdici(pdd) == 1){
                            dp("disdici: OK", out);
                        } else {
                            dp("disdici: FAIL", out);
                            //eccezioni
                        }
                    } else dp("prenotazione mancante", out);
                } else dp("no permit to do this op", out);
                break;
            }

            case "myPrenots":{  //mostra le mie prenotazioni - FATTO
                    resp.setContentType("application/json");
                    dao.updMyArchive();
                    out.print(jsonIzer.toJson(dao.getMyArchive()));
                break;
            }

            case "addProfessore":{       //admin: inserisci prof
                String role = (String) req.getSession().getAttribute("role");
                String response = "Professore aggiunto";
                if(role.equals("admin")){
                    String pN = (String) req.getParameter("profNome");
                    String pC = (String) req.getParameter("profCogn");
                    if(pN == null || pC == null){
                        System.out.println("nome o cognome null");
                    } else {
                        dao.insertProf(pN, pC);
                        out.print(response);
                    }
                } else {
                    System.out.println("no permit to do this op");
                }
                break;
            }
            case "removeProfessore":{       //admin: inserisci prof
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("admin")){
                    String pN = (String) req.getParameter("profNome");
                    String pC = (String) req.getParameter("profCogn");
                    if(pN == null || pC == null){
                        System.out.println("nome o cognome null");
                    } else {
                        dao.removeProf(pN, pC);
                    }
                } else {
                    System.out.println("no permit to do this op");
                }
                break;
            }
            case "addCorso":{       //admin: inserisci corso
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("admin")){
                    String cN = (String) req.getParameter("corsoNome");
                    if(cN == null){
                        System.out.println("course name null");
                    } else {
                        dao.insertCorso(cN);
                    }
                } else {
                    System.out.println("no permit to do this op");
                }
                break;
            }
            case "removeCorso":{       //admin: inserisci corso
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("admin")){
                    String cN = (String) req.getParameter("corsoNome");
                    if(cN == null){
                        System.out.println("course name null");
                    } else {
                        dao.removeCorso(cN);
                    }
                } else {
                    System.out.println("no permit to do this op");
                }
                break;
            }
            case "addInsegnamento":{       //admin: inserisci insegnamneto
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("admin")){
                    String pdaN = (String) req.getParameter("pdaN");
                    String pdaC = (String) req.getParameter("pdaC");
                    String cda = (String) req.getParameter("cda");
                    if(cda != null && pdaN != null && pdaC != null){
                        dao.insertInsegnamento(jsonIzer.fromJson(cda, String.class) ,
                                jsonIzer.fromJson(pdaN, String.class), jsonIzer.fromJson(pdaC, String.class));
                    } else {
                        System.out.println("corso o prof null");
                    }
                } else {
                    System.out.println("no permit to do this op");
                }

                break;
            }
            case "removeInsegnamento":{       //admin: inserisci insegnamneto
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("admin")){
                    String pdaN = (String) req.getParameter("pdaN");
                    String pdaC = (String) req.getParameter("pdaC");
                    String cda = (String) req.getParameter("cda");
                    if(cda != null && pdaN != null && pdaC != null){
                        dao.removeInsegnamento(jsonIzer.fromJson(cda, String.class) ,
                                jsonIzer.fromJson(pdaN, String.class), jsonIzer.fromJson(pdaC, String.class));
                    } else {
                        System.out.println("corso o prof null");
                    }
                } else {
                    System.out.println("no permit to do this op");
                }

                break;
            }
            case "viewAllPrenot":{      //admin: view all prenotazioni
                String role = (String) req.getSession().getAttribute("role");
                resp.setContentType("application/json");
                if(role.equals("admin")){
                    out.print(jsonIzer.toJson(DBA.getAllPrenot()));
                } else {
                    System.out.println("no permit to do this op");
                }

                break;
            }
            case "setDone":{
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    String rdpJson = req.getParameter("psd");
                    if(rdpJson != null){
                        Prenotazione psd = jsonIzer.fromJson(rdpJson, Prenotazione.class);
                        if(dao.setDone(psd) == 1){
                            out.print("setDone: OK");
                            dp("", out);
                        } else {
                            dp("prenota: FAIL", out);
                            //todo secondo me qua c'è da inserire la gestione per bene delle
                            // eccezioni, tipo che stampo anche quello che dice l'eccezione
                            // in modo che il frontend sa bene cosa fare e cosa dire all'user
                            // (tutta la parte di gestione del feedback che deve arrivare all'user)
                        }
                    } else dp("ripetizione mancante", out);
                } else dp("no permission", out);
                break;
            }
            case "logOut":{
                HttpSession s = req.getSession();
                s.invalidate();
            }
        }
        out.close();
    }
}
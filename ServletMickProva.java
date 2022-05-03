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
        // todo gestire le eccezioni meglio perchè dio fa
    }

    void dp(String text, PrintWriter pw){
        System.out.println(text);
        pw.print(text);
    }

    public void init(){
        System.out.println("servlet 1 - init");
        try {
            dao = new DBA();
//            DBA.registerDriver();
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


    // todo ogni sout va sostituito con un write sul printwriter
    //  (? si o no? o devo farlo in altro modo?) o con le eccezioni

    //todo aggiornare con i nuovi metodi

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
                String authOut = dao.auth(un, psw);
                out.print(authOut);
                if(dao.getRole().equals("user") || dao.getRole().equals("admin")){
                    HttpSession s = req.getSession();
                    s.setAttribute("username", dao.getUsername());
                    s.setAttribute("role", dao.getRole());
                    System.out.println("session tracked");
                    //inserire le cose da fare a livello di dao una volta che hai fatto il login
                    dao.updMyArchive();
                }
                break;
            }
            case "prenota":{    //prenota rip - FATT0
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    String rdpJson = req.getParameter("rdp");
                    if(rdpJson != null){
                        Ripetizioni rdp = jsonIzer.fromJson(rdpJson, Ripetizioni.class);
                        //System.out.println(dao.prenota(rdp));
                        if(dao.prenota(rdp) == 1){
                            out.print("prenota: OK");
                            dp("prenota: OK", out);
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
//TODO DA QUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            case "addProfessore":{       //admin: inserisci prof
                String role = (String) req.getSession().getAttribute("role");
                if(role.equals("admin")){
                    String pN = (String) req.getParameter("profNome");
                    String pC = (String) req.getParameter("profCogn");
                    if(pN == null || pC == null){
                        System.out.println("nome o cognome null");
                    } else {
                        dao.insertProf(pN, pC);
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
                    String pda = (String) req.getParameter("pda");
                    String cda = (String) req.getParameter("cda");
                    if(cda == null || pda == null){
                        dao.insertInsegnamento(jsonIzer.fromJson(cda, String.class) ,
                                jsonIzer.fromJson(pda, Professore.class));
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
                    String pda = (String) req.getParameter("pda");
                    String cda = (String) req.getParameter("cda");
                    if(cda == null || pda == null){
                        dao.removeInsegnamento(jsonIzer.fromJson(cda, String.class) ,
                                jsonIzer.fromJson(pda, Professore.class));
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
                if(role.equals("admin")){
                    req.setAttribute("result", jsonIzer.toJson(dao.getAllPrenot()));
                } else {
                    System.out.println("no permit to do this op");
                }

                break;
            }
        }
        out.close();
    }
}
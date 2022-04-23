package com.example.progetto1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "TestServletMick", value = "/TestServletMick")
public class TestServletMick {
    DBA dao;

    public void init(){
        System.out.println("TestServletMick - init");
        try {
            dao = new DBA();
            //per com'è adesso non serve passargli i parametri dato che automaticamente nasce come guest - vuoto
            DBA.registerDriver();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession s = req.getSession();
        processRequest(req, resp);
    }
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession s = req.getSession();
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = (String) req.getParameter("action");
        if(action.equals("login")){
            String un = req.getParameter("username");
            String psw = req.getParameter("password");
            if(un !=null && psw!=null){
                dao.auth(un, psw);
                String role = dao.getRole();
                if(role.equals("user") || role.equals("admin")){
                    HttpSession s = req.getSession();
                    s.setAttribute("username", un);
                    s.setAttribute("role", role);
                    System.out.println("session tracked");
                } else System.out.println("wrong un/psw: " + role);
            } else System.out.println("un or psw not received");
        }
        if(action.equals("hi")){
            resp.getOutputStream().println("hi");
            resp.getOutputStream().close();
        }
    }

}

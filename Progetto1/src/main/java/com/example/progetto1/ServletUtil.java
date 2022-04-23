package com.example.progetto1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class ServletUtil {
    public static <T> void logPrint(String title, ArrayList<T> table) {
        Iterator tab1 = table.iterator();
        System.out.println("\n-  -   -   -   -    "+title.toUpperCase()+"   -   -   -   -   -\n");
        while (tab1.hasNext()) {
            T c = (T) tab1.next();
            System.out.println(c);
        }
        System.out.println("\n-  -   -   -   -    FINE "+title.toUpperCase()+"  -   -   -   -   \n");
    }

    public static void printUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        if(s.getAttribute("username")!=null) {
            out.println("ciao " + s.getAttribute("username") + " - ruolo: " + s.getAttribute("role"));
        } else {out.println("non ancora autenticato");}
    }
}

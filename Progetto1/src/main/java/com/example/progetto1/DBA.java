package com.example.progetto1;
import com.example.progetto1.DBInteraction.*;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import java.sql.*;
import java.util.ArrayList;

public class DBA {


    // i metodi per il DB devono essere private o non devo indicarlo in
    // modo che rimangano package private e la servlet li possa chiamare senza problemi?
    // o per fare così dovrebbero essere public dato che sono in package diversi
    // , o li metto in uno stesso package

    /*
    * todo
    *   private o package private
    *   fixare servlet
    *   gestire le eccezioni
    */

    public static final String urlDB = "jdbc:mysql://localhost:3306/test";
    private String userDB = "guest1";
    private String pswDB = "";
    //credenziali per collegarsi al database (127.0.0.1 > Account Utenti) , NON per loggarsi nel sito/app

    private String username  ="";
    public String getUsername() {
        return username;
    }

    private String role = "admin";
    public String getRole() {
        return role;
    }

    private static Gson jsonizer = new Gson();

    //  tutte le ripetizioni
    private static ArrayList<Ripetizioni> allRipet = new ArrayList<>();
    public static ArrayList<Ripetizioni> getAllRipet() {
        return allRipet;
    }

    //  tutte le prenotazioni
    private static ArrayList<Prenotazione> allPrenot = new ArrayList<>();
    public static ArrayList<Prenotazione> getAllPrenot() {
        return allPrenot;
    }

    //  le mie prenotazioni
    private ArrayList<Prenotazione> myArchive = new ArrayList<>();


    //DBA Object
    public DBA() throws ServletException {
        registerDriver();
        updRip();
    }
    public void printDBAInfo(){
        System.out.println("DBA: userDB= " + userDB + "\t| username= "+ username + "\t| role= " + role);
    }

    //DRIVER
    public static void registerDriver() throws ServletException {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    //todo dovrei togliere tutti i public e lasciarli package private

    //LOGIN
    String auth(String username, String password) throws ServletException {
        ArrayList<Account> matchingAccounts = null;
        String result = "exc thrown";
        try {
            role = "admin";         // per poter chiamare qryUsers, tanto viene sovrascritto in ogni case dello switch
            matchingAccounts = qryUsers(username);
            switch(matchingAccounts.size()){
                case(0):{
                    System.out.println("No account registered with this username");
                    role = "guest";
                    result = "no matches";
                    break;
                }
                case (1): {
                    if (!password.equals(matchingAccounts.get(0).getPassword())) {
//                        System.out.println("expected psw= "+ matchingAccounts.get(0).getPassword()+ ", sent psw = " + password);
                        role = "guest";
                        result = "wrong psw";
                    } else {
                        System.out.println("login successful");
                        role = matchingAccounts.get(0).getRole();
                        this.username = username;
                        this.userDB = "user";
                        result = "logged in";
                    }
                    break;
                }
                default:{
                    result = "bug: " + matchingAccounts.size() + " matches";
                    break;
                }
            }
        } catch (ServletException e) {
            throw new ServletException(e.getMessage() + "authRes: "+result);
        }
        return result;
    }

    //USERS
    //      /!\ è private, dev'essere chiamato solo da metodi di questa classe
    //      used in:  auth()
    private ArrayList<Account> qryUsers(String reqName) throws ServletException {
        if(role.equals("admin")) {
            Connection conn1 = null;
            ArrayList<Account> accounts = new ArrayList<>();
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("\nConnected to the database test - qryUsers");
                    String qry = "select * from users where username = ?";
                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, reqName);
                    System.out.println(st);
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        Account c = new Account(rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("ruolo"));
                        accounts.add(c);
                    }
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in qryUsers: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return accounts;
        } else System.out.println("admin role needed for this op");
        return null;
    }

    //RIPETIZIONI

    //  -inizializza il JDB
    //      /!\ è private, dev'essere chiamato solo da metodi di questa classe
    //      used in:  DBA(), insert/removeInsegnamento()
    private void updRip() throws ServletException {
        ArrayList<Ripetizioni> rips = new ArrayList<>();
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
            if (conn1 != null) {
                System.out.println("Connected to the database test - updRip");
                updPren();
                String qry = "SELECT professore.id, professore.cognome, corso.id, corso.nome " +
                                "from (insegnamenti join corso on insegnamenti.corso_id = corso.id) " +
                                "join professore on professore.id = insegnamenti.prof_id";

                PreparedStatement st = conn1.prepareStatement(qry);
                ResultSet rs = st.executeQuery();
                ArrayList<Insegnamenti> ins = new ArrayList<>();
                while (rs.next()) {
                    String prof = rs.getString("professore.cognome");
                    String corso = rs.getString("corso.nome");
                    int profID = rs.getInt("professore.id");
                    int corsoID = rs.getInt("corso.id");
                    ins.add(new Insegnamenti(prof, corso, profID, corsoID));
                }
                String[] giorni = {"LUN", "MAR", "MER", "GIO", "VEN"};
                int[] oraInizio = {15, 16, 17, 18};
                for (Insegnamenti i: ins){
                    String p = i.getProfCognome();
                    String c = i.getCorso();
                    for(String g : giorni) {
                        for (Integer h : oraInizio) {
                            Ripetizioni newR = new Ripetizioni(p, c, g, h, 0);
                            for(Prenotazione pr : allPrenot){
                                if(pr.getRip().getStatus() != 4) {          //4=disdetta, che è come se non ci fosse
                                    if (pr.getRip().equals(newR)) {
                                        newR.setStatus(1);
                                    } else if (pr.getRip().overlaps(newR)) {
                                        newR.setStatus(2);
                                    }
                                }
                            }
                            rips.add(newR);
                        }
                    }
                }
                allRipet = rips;
                System.out.println("JDB-Rip popolato");
            }
        } catch (SQLException e) {
            throw new ServletException("SQL exc in updRip: "+ e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    //  -prenotabile?
    //      /!\ è private, dev'essere chiamato solo da metodi di questa classe
    //      used in:  DBA(), insert/removeInsegnamento()
    private boolean prenotabile(Ripetizioni r){
        for (Prenotazione p: allPrenot) {
            Ripetizioni ripPren = p.getRip();
            if (ripPren.overlaps(r) || ripPren.equals(r)){
                return false;
            }
        }
        return true;
    }

    //  -cerca per json
    //      used in servlet - public with roleCheck
    public ArrayList<Ripetizioni> jsonQryRip(String jsonParameters) throws ServletException {
        if(this.role.equals("admin") || this.role.equals("user")) {
            Ripetizioni jsonRip = new Ripetizioni();
            ArrayList<Ripetizioni> filtered = new ArrayList<>(), list = allRipet;
            jsonRip = jsonizer.fromJson(jsonParameters, jsonRip.getClass());
            for (Ripetizioni r : list) {
                if (r.fitsReq(jsonRip)) {
                    filtered.add(r);
                }
            }
            return filtered;
        } else {
            System.out.println("role needed - jsonQryRip");
            throw new ServletException("role needed - jsonQryRip");
        }
    }

    //  -visualizza prenotabili
    //      public ma giusto così
    public ArrayList<Ripetizioni> prenotabili(){
        ArrayList<Ripetizioni> prenotabili = new ArrayList<>();
        for(Ripetizioni r : allRipet){
            if(r.getStatus()==0){
                prenotabili.add(r);
            }
        }
        return prenotabili;
    }

    //PRENOTAZIONI

    //  - mostra tutte
    //      getAllRipet() sopra

    //  - mantieni consistente
    //      public con roleCheck
    public ArrayList<Prenotazione> updAllPrenot() throws ServletException {
        if (role.equals("admin")) {
            Connection conn1 = null;
            ArrayList<Prenotazione> prenots = new ArrayList<>();
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in allPrenot");
                    String qry = "SELECT user, professore.cognome, corso.nome, giorno, ora, status " +
                            "from ((prenotazioni AS p join professore on p.prof_id=professore.id) " +
                            "join corso on corso.id=p.corso_id ) where 1 ";

                    PreparedStatement st = conn1.prepareStatement(qry);
                    //  System.out.println(st);
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        Prenotazione p = new Prenotazione(rs.getString("user"),
                                new Ripetizioni(
                                        rs.getString("professore.cognome"),
                                        rs.getString("corso.nome"),
                                        rs.getString("giorno"),
                                        rs.getInt("ora"),
                                        rs.getInt("status")
                                )
                        );
                        prenots.add(p);
                    }
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in allPrenot: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return allPrenot;
        } else {
            System.out.println("role needed - allPrenot");
            throw new ServletException("role needed - allPrenot");
        }
    }

    //  -mostra per user
    //      public con roleCheck
    public ArrayList<Prenotazione> myPrenot() throws ServletException {
        if(this.role.equals("user")) {
            Connection conn1 = null;
            ArrayList<Prenotazione> myPrens = new ArrayList<>();
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in allPrenot");
                    String qry = "SELECT user, professore.cognome, corso.nome, giorno, ora , status " +
                            "from ((prenotazioni AS p join professore on p.prof_id=professore.id) " +
                            "join corso on corso.id=p.corso_id ) where user=? ";

                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, this.userDB);
                    //  System.out.println(st);
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        Prenotazione p = new Prenotazione(rs.getString("user"),
                                new Ripetizioni(
                                        rs.getString("professore.cognome"),
                                        rs.getString("corso.nome"),
                                        rs.getString("giorno"),
                                        rs.getInt("ora"),
                                        rs.getInt("status")
                                )
                        );
                        myPrens.add(p);
                    }
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in userPrenot: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return myPrens;
        } else {
            System.out.println("user role needed - myPrenot");
            throw new ServletException("user role needed - myPrenot");
        }
    }

    //  -mantiene consistenti JDB con mySQL
    void updPren() throws ServletException {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
            if (conn1 != null) {
                System.out.println("Connected to the database test - in updPren - calling allPrenot");
                allPrenot = updAllPrenot();
            }
        } catch (SQLException e) {
            throw new ServletException("SQL exc in updPren: "+ e.getMessage());
        } catch (ServletException e){
            throw e;
        }finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    //  -prenota
    public int prenota(Ripetizioni ripetDP) throws ServletException {
        if(this.role.equals("user") || this.role.equals("admin")) {
            Connection conn1 = null;
            int nIns = 0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in prenota");
                    if (prenotabile(ripetDP)) {
                        // aggiunta nel JDB
                        Ripetizioni rDP = ripetDP;
                        rDP.setStatus(1);
                        allPrenot.add(new Prenotazione(this.userDB, rDP));
                        myArchive.add(new Prenotazione(this.userDB, rDP));
                        // aggiunta nel DB MySql
                        String profDP = rDP.getProf();
                        String corsoDP = rDP.getCorso();
                        String giornoDP = rDP.getGiorno();
                        int oraDP = rDP.getOra();
                        String insQry = "INSERT INTO `prenotazioni`(`user`, `corso_id`, `prof_id`, `giorno`, `ora`, `status`) " +
                                "(SELECT ? , r.corso_id,r.prof_id, ? , ? , 1 " +          //?s=user, g, h, c, p
                                "FROM ((insegnamenti AS r JOIN corso on r.corso_id=corso.id) JOIN professore on r.prof_id=professore.id) " +
                                "WHERE corso.nome= ? AND professore.cognome=?)";

                        try {
                            PreparedStatement insSt = conn1.prepareStatement(insQry);

                            insSt.setString(1, this.userDB);
                            insSt.setString(2, giornoDP);
                            insSt.setInt(3, oraDP);
                            insSt.setString(4, corsoDP);
                            insSt.setString(5, profDP);
                            nIns = insSt.executeUpdate();
                            if (nIns == 1) {
                                System.out.println(nIns + " tupla inserita");
                            } else {
                                System.out.println("something went wrong");
                            }
                        } catch (SQLException e) {
                            System.out.println("exc in prenota: " + e.getMessage());
                        }

                        //aggiorno le altre ripetizioni nel JDB
                        for (Ripetizioni r : allRipet) {
                            if (r.equals(ripetDP)) {
                                r.setStatus(1);
                            } else if (r.overlaps(ripetDP)) {
                                r.setStatus(2);
                            }
                        }

                    } else {
                        System.out.println("ripetizione non prenotabile");
                    }
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in prenota: " + e.getMessage());
            } finally {
                //  updPren();
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return nIns;
        } else {
            System.out.println("role required - prenota");
            throw new ServletException("role required - prenota");
        }
    }

    //  -disdici
    public int disdici(Prenotazione prenotDC) throws ServletException {
        if(this.role.equals("user") || this.role.equals("admin")) {
            Connection conn1 = null;
            int nUpd = 0;

            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in disdici");
                    if (allPrenot.contains(prenotDC)) {

                        //disdico in java
                        allPrenot.get(allPrenot.indexOf(prenotDC)).getRip().setStatus(4);

                        Ripetizioni ripetDC = prenotDC.getRip();

                        //lavoro sul DB
                        //cancello la prenotaz
                        String qryDel = "UPDATE `prenotazioni` SET `status`='4' WHERE " +
                                "user=? and giorno=? and corso_id=(select id from corso where nome=?) " +
                                "and prof_id=(select id from professore where cognome=?) and ora=? ";
                        String userDC = prenotDC.getUser();
                        String giornoDC = ripetDC.getGiorno();
                        String corsoDC = ripetDC.getCorso();
                        String profDC = ripetDC.getProf();
                        int oraDC = ripetDC.getOra();
                        PreparedStatement stDel = conn1.prepareStatement(qryDel);
                        stDel.setString(1, userDC);
                        stDel.setString(2, giornoDC);
                        stDel.setString(3, corsoDC);
                        stDel.setString(4, profDC);
                        stDel.setInt(5, oraDC);
                        nUpd = stDel.executeUpdate();

                        //aggiorno le altre ripetizioni
                        for (Ripetizioni r : allRipet) {
                            if (r.equals(ripetDC)) {
                                r.setStatus(0);
                            }
                            if (r.overlaps(ripetDC)) {
                                if (r.getStatus() == 2) {
                                    r.setStatus(0);
                                }
                            }
                        }
                        System.out.println("disdetta finita: " + nUpd + " prenotazioni modificate ");
                    } else {
                        System.out.println("prenotazione inesistente");
                    }
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in disdici: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return nUpd;
        } else {
            System.out.println("user/admin role needed");
            throw new ServletException("role required - disdici");
        }
    }
    //  -segna come svolta
    public int setDone(Prenotazione pSD) throws ServletException {
        if(role.equals("user")) {
            //rimozione JDB
            if (myArchive.contains(pSD)) {
                myArchive.get(myArchive.indexOf(pSD)).getRip().setStatus(3);
                System.out.println("setDone in JDB: ok");
            } else {
                System.out.println("prenotazione not found in JDB");
            }
            //rimozione DB
            Connection conn1 = null;
            int nUpd = 0;

            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("\nConnected to the database test - in setDone");
                    Ripetizioni rSD = pSD.getRip();
                    String qry = "UPDATE `prenotazioni` SET `status`='3' WHERE " +
                            "user=? and giorno=? and corso_id=(select id from corso where nome=?) " +
                            "and prof_id=(select id from professore where cognome=?) and ora=? ";
                    String userDC = pSD.getUser();
                    String giornoDC = rSD.getGiorno();
                    String corsoDC = rSD.getCorso();
                    String profDC = rSD.getProf();
                    int oraDC = rSD.getOra();
                    PreparedStatement stUpd = conn1.prepareStatement(qry);
                    stUpd.setString(1, userDC);
                    stUpd.setString(2, giornoDC);
                    stUpd.setString(3, corsoDC);
                    stUpd.setString(4, profDC);
                    stUpd.setInt(5, oraDC);
                    nUpd = stUpd.executeUpdate();
                    System.out.println("DB upd done: " + nUpd + " tuple modificate");
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in setDone: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return nUpd;
        } else {
            System.out.println("user role needed");
            throw new ServletException("user role needed - setDone");
        }
    }


    //ADMIN OPS

    //  - inserire prof
    public int insertProf(String nome, String cognome) throws ServletException {
        if(this.role.equals("admin")) {
            Connection conn1 = null;
            int numInvolved=0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in insProf");
                    String qry = "INSERT INTO `professore`(`id`, `nome`, `cognome`) VALUES (NULL, ?,?)";
                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, nome);
                    st.setString(2,cognome);
                    System.out.println(st);
                    numInvolved = st.executeUpdate();
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in insProf: "+ e.getMessage());
            }
            finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return numInvolved;
        } else {
            System.out.println("admin role needed - insProf");
            throw new ServletException("admin role needed - insProf");
        }
    }
        //  - inserire corso
    public int insertCorso(String nome) throws ServletException {
        if(this.role.equals("admin")) {
            Connection conn1 = null;
            int numInvolved = 0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in insCorso");
                    String qry = "INSERT INTO `corso`(`id`, `nome`) VALUES (NULL, ?)";
                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, nome);
                    System.out.println(st);
                    numInvolved = st.executeUpdate();
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in insCorso: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return numInvolved;
        } else {
            System.out.println("admin role needed");
            throw new ServletException("admin role needed");
        }
    }
    //  - inserire insegnamento
    public int insertInsegnamento(String corso, Professore professore) throws ServletException {
        if(this.role.equals("admin")) {
            Connection conn1 = null;
            int numInvolved=0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in insInsegnamento");
                    String npDI = professore.getNome();
                    String cpDI = professore.getCognome();

                    String qry = "INSERT INTO `insegnamenti`(`prof_id`, `corso_id`) " +
                            "(SELECT professore.id, corso.id " +
                            "FROM professore JOIN corso " +
                            "WHERE corso.nome = ? AND professore.nome = ? AND professore.cognome = ?)";

                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, corso);
                    st.setString(2, npDI);
                    st.setString(3, cpDI);
                    System.out.println(st);
                    numInvolved = st.executeUpdate();
                    System.out.println("qry sent: tuple inserted= " + numInvolved);
                }
                updRip();
            } catch (SQLException e) {
                throw new ServletException("SQL exc in insInsegn: "+ e.getMessage());
            }
            finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return numInvolved;
        } else {
            System.out.println("admin role needed");
            throw new ServletException("admin role needed");
        }
    }
    //  - rimuovere corso
    public int removeCorso(String nome) throws ServletException {
        if(this.role.equals("admin")) {
            Connection conn1 = null;
            int numInvolved = 0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in insCorso");
                    String qry = "DELETE FROM `corso`where nome = ? ";
                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, nome);
                    System.out.println(st);
                    numInvolved = st.executeUpdate();
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in removeCorso: " + e.getMessage());
            } finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return numInvolved;
        } else {
            System.out.println("admin role needed");
            throw new ServletException("admin role needed");
        }
    }
    //  - rimuovere prof
    public int removeProf(String nome, String cognome) throws ServletException {
        if(this.role.equals("admin")) {
            Connection conn1 = null;
            int numDel=0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in insProf");
                    String qry = "DELETE FROM `professore` WHERE nome= ? AND cognome = ?";
                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, nome);
                    st.setString(2,cognome);
                    System.out.println(st);
                    numDel = st.executeUpdate();
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in removeProf: "+ e.getMessage());
            }
            finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return numDel;
        } else {
            System.out.println("admin role needed - removeProf");
            throw new ServletException("admin role needed - removeProf");
        }
    }
    //  - rimuovere insegnamento
    public int removeInsegnamento(String corso, Professore professore) throws ServletException {
        if(this.role.equals("admin")) {
            Connection conn1 = null;
            int numInvolved=0;
            try {
                conn1 = DriverManager.getConnection(urlDB, userDB, pswDB);
                if (conn1 != null) {
                    System.out.println("Connected to the database test - in insInsegnamento");
                    String npDR = professore.getNome();
                    String cpDR = professore.getCognome();

                    String qry = "delete FROM `insegnamenti` where " +
                            "prof_id = (select id from professore where nome=? and cognome =?) and " +
                            "corso_id= (select id from corso where nome=?)";

                    PreparedStatement st = conn1.prepareStatement(qry);
                    st.setString(1, npDR);
                    st.setString(2, cpDR);
                    st.setString(3, corso);
                    System.out.println(st);
                    numInvolved = st.executeUpdate();
                    System.out.println("qry sent: deleted records= " + numInvolved);
                    updRip();
                }
            } catch (SQLException e) {
                throw new ServletException("SQL exc in removeInsegnamento: "+ e.getMessage());
            }
            finally {
                if (conn1 != null) {
                    try {
                        conn1.close();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            return numInvolved;
        } else {
            System.out.println("admin role needed");
            throw new ServletException("admin role needed");
        }
    }




}


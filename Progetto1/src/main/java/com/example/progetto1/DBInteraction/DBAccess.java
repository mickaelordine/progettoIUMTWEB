//package DBInteraction;
//
//import com.google.gson.*;
//
//import javax.servlet.ServletException;
//import java.sql.*;
//import java.util.ArrayList;
//
////tengo l'array rip per velocità nel mostrare e vedere cosa c'è
//
///*
//        RISPETTO AL VECCHIO HO TOLTO:
//
//    qryRip          - messa tramite json
//    insRip          - non ho più le rip sul DB
//    allRip          - ""
//    updRip          - lo tengo sempre updatato
// */
//
////questa classe si interfaccia con il DB per ottenere ArrayList di valori che sarebbero le tabelle del DB
//
//
//public class DBAccess {
//    private static ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
//    private static ArrayList<Ripetizioni> ripetizioni = new ArrayList<>();
//    public static ArrayList<Prenotazione> getPrenotazioni() {
//        return prenotazioni;
//    }
//    public static ArrayList<Ripetizioni> getRipetizioni() {
//        return ripetizioni;
//    }
//    public static Gson jsonizer = new Gson();
//
//    private static final String url1 = "jdbc:mysql://localhost:3306/test";
//    private static final String user = "root";
//    private static final String password = "";
//
//
//    public static void registerDriver() {
//        try {
//            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//            System.out.println("Driver correttamente registrato");
//        } catch (SQLException e) {
//            System.out.println("Errore: " + e.getMessage());
//        }
//    }
//
//    void SQLEtoSE(SQLException e) throws ServletException {
//        throw new ServletException(e.getMessage());
//    }
//    //USERS
//    public static ArrayList<Account> qryUsers(String where) throws ServletException {
//        Connection conn1 = null;
//        ArrayList<Account> accounts = new ArrayList<>();
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("\nConnected to the database test - qryUsers");
//                String qry = "select * from users where username = ?";
//                PreparedStatement st = conn1.prepareStatement(qry);
//                st.setString(1, where);
//                System.out.println(st);
//                ResultSet rs = st.executeQuery();
//                while (rs.next()) {
//                    Account c = new Account(rs.getString("username"), rs.getString("password"), rs.getString("ruolo"));
//                    accounts.add(c);
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in qryUsers: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return accounts;
//    }
//    //LOGIN
//    public static String auth(String username, String password) throws ServletException {
//        String role = "not logged";
//        ArrayList<Account> matchingAccounts = null;
//        try {
//            matchingAccounts = qryUsers(username);
//            switch(matchingAccounts.size()){
//                case(0):{
//                    System.out.println("No account registered with this username");
//                    role="no account found";
//                    break;
//                }
//                case (1): {
//                    if (!password.equals(matchingAccounts.get(0).getPassword())) {
//                        System.out.println("wrong username or password");
//                        System.out.println("expected psw= "+ matchingAccounts.get(0).getPassword()+ ", sent psw = " + password);
//                        role="wrong un/psw";
//                    } else {
//                        System.out.println("login successful");
//                        role = matchingAccounts.get(0).getRole();
//                    }
//                    break;
//                }
//                default:{
//                    role = "DB bug";
///*
//                    System.out.println(role);
//*/
//                    break;
//                }
//            }
//        } catch (ServletException e) {
//            throw e;
//        }
//        return role;
//    }
//
//
//    //RIPETIZIONI
//
//    //  -inizializza il JDB
//    public static void popolaRipetiz() throws ServletException {
//        ArrayList<Ripetizioni> rips = new ArrayList<>();
//        Connection conn1 = null;
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - popolaRipetiz");
//                updPren();
//                String qry = "SELECT professore.id, professore.cognome, corso.id, corso.nome " +
//                        "from (insegnamenti join corso on insegnamenti.corso_id = corso.id) " +
//                        "join professore on professore.id = insegnamenti.prof_id";
//
//                PreparedStatement st = conn1.prepareStatement(qry);
//                ResultSet rs = st.executeQuery();
//                ArrayList<Insegnamenti> ins = new ArrayList<>();
//                while (rs.next()) {
//                    String prof = rs.getString("professore.cognome");
//                    String corso = rs.getString("corso.nome");
//                    int profID = rs.getInt("professore.id");
//                    int corsoID = rs.getInt("corso.id");
//                    ins.add(new Insegnamenti(prof, corso, profID, corsoID));
//                }
//
//
//                String[] settimana = {"LUN", "MAR", "MER", "GIO", "VEN"};
//                int[] oraInizio = {15, 16, 17, 18};
//                for (Insegnamenti i: ins){
//                    String p = i.getProfessore();
//                    String c = i.getCorso();
//                    for(String g : settimana) {
//                        for (Integer h : oraInizio) {
//                            Ripetizioni newR = new Ripetizioni(p, c, g, h, 0);
//                            for(Prenotazione pr : prenotazioni){
//                                if(pr.getRip().equals(newR)){
//                                    newR.setStatus(1);
//                                } else if(pr.getRip().overlaps(newR)){
//                                    newR.setStatus(2);
//                                }
//                            }
//                            rips.add(newR);
//                            ripetizioni.add(newR);
//                        }
//                    }
//                }
//                ripetizioni = rips;
//                System.out.println("JDB-Rip popolato");
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in popolaRip: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//    }
//    //  -prenotabile?
//    public static boolean prenotabile(Ripetizioni r){
//        for (Prenotazione p: prenotazioni) {
//            Ripetizioni ripPren = p.getRip();
//            if (ripPren.overlaps(r) || ripPren.equals(r)){
//                return false;
//            }
//        }
//        return true;
//    }
//    //  -cerca per json
//    public static ArrayList<Ripetizioni> jsonQryRip(String jsonParameters) {
//        System.out.println("in jsonQryR");
//        Ripetizioni jsonRip = new Ripetizioni();
//        ArrayList<Ripetizioni> filtered = new ArrayList<>();
//        jsonRip = jsonizer.fromJson(jsonParameters, jsonRip.getClass());
//        for(Ripetizioni r: ripetizioni) {
//            if(r.fitsReq(jsonRip)) {
//                filtered.add(r);
//            }
//        }
//        return filtered;
//    }
//
//    //PRENOTAZIONI
//
//    //  -mostra tutte
//    public static ArrayList<Prenotazione> allPrenot() throws ServletException {
//        Connection conn1 = null;
//        ArrayList<Prenotazione> prenots = new ArrayList<>();
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in allPrenot");
//                String qry = "SELECT user, professore.cognome, corso.nome, giorno, ora, status " +
//                        "from ((prenotazioni AS p join professore on p.prof_id=professore.id) " +
//                        "join corso on corso.id=p.corso_id ) where 1 ";
//
//                PreparedStatement st = conn1.prepareStatement(qry);
//            //  System.out.println(st);
//                ResultSet rs = st.executeQuery();
//                while (rs.next()) {
//                    Prenotazione p = new Prenotazione(rs.getString("user"),
//                            new Ripetizioni(
//                                    rs.getString("professore.cognome"),
//                                    rs.getString("corso.nome"),
//                                    rs.getString("giorno"),
//                                    rs.getInt("ora"),
//                                    rs.getInt("status")
//                            )
//                    );
//                    prenots.add(p);
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in allPrenot: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return prenots;
//    }
//    //  -mostra per user
//    public static ArrayList<Prenotazione> userPrenot(String username) throws ServletException {
//        Connection conn1 = null;
//        ArrayList<Prenotazione> myPrens = new ArrayList<>();
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in allPrenot");
//                String qry = "SELECT user, professore.cognome, corso.nome, giorno, ora , status " +
//                        "from ((prenotazioni AS p join professore on p.prof_id=professore.id) " +
//                        "join corso on corso.id=p.corso_id ) where user=? ";
//
//                PreparedStatement st = conn1.prepareStatement(qry);
//                st.setString(1, username);
//                //  System.out.println(st);
//                ResultSet rs = st.executeQuery();
//                while (rs.next()) {
//                    Prenotazione p = new Prenotazione(rs.getString("user"),
//                            new Ripetizioni(
//                                    rs.getString("professore.cognome"),
//                                    rs.getString("corso.nome"),
//                                    rs.getString("giorno"),
//                                    rs.getInt("ora"),
//                                    rs.getInt("status")
//                            )
//                    );
//                    myPrens.add(p);
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in userPrenot: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return myPrens;
//    }
//    //  -mantiene consistenti JDB con mySQL
//    public static void updPren() throws ServletException {
//        Connection conn1 = null;
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in updPren - calling allPrenot");
//                prenotazioni = allPrenot();
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in updPren: "+ e.getMessage());
//        } catch (ServletException e){
//            throw e;
//        }finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//    }
//    //  -prenota
//    public static int prenota(String stud, Ripetizioni ripetDP) throws ServletException {
//        Connection conn1 = null;
//        int nIns = 0;
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in prenota");
//                if (prenotabile(ripetDP)) {
//                    // aggiunta nel JDB
//                    Ripetizioni rDP = ripetDP;
//                    rDP.setStatus(1);
//                    prenotazioni.add(new Prenotazione(stud, rDP));
//                    // aggiunta nel DB MySql
//                    String profDP = rDP.getProf();
//                    String corsoDP = rDP.getCorso();
//                    String giornoDP = rDP.getGiorno();
//                    int oraDP = rDP.getOra();
//                    String insQry = "INSERT INTO `prenotazioni`(`user`, `corso_id`, `prof_id`, `giorno`, `ora`, `status`) " +
//                            "(SELECT ? , r.corso_id,r.prof_id, ? , ? , 1 " +          //?s=user, sett, g, h, c, p
//                            "FROM ((insegnamenti AS r JOIN corso on r.corso_id=corso.id) JOIN professore on r.prof_id=professore.id) " +
//                            "WHERE corso.nome= ? AND professore.cognome=?)";
//
//                    try {
//                        PreparedStatement insSt = conn1.prepareStatement(insQry);
//
//                        insSt.setString(1, stud);
//                        insSt.setString(2, giornoDP);
//                        insSt.setInt(3, oraDP);
//
//                        insSt.setString(4, corsoDP);
//                        insSt.setString(5, profDP);
//                        nIns = insSt.executeUpdate();
//                        if (nIns == 1) {
//                            System.out.println(nIns +" tupla inserita");
//                        } else {
//                            System.out.println("something went wrong");
//                        }
//                    } catch (SQLException e) {
//                        System.out.println("exc in prenota: " + e.getMessage());
//                    }
//
//                    //aggiorno le altre ripetizioni nel JDB
//                    for (Ripetizioni r : ripetizioni) {
//                        if (r.equals(ripetDP)) {
//                            r.setStatus(1);
//                        } else if (r.overlaps(ripetDP)) {
//                            r.setStatus(2);
//                        }
//                    }
//
//                } else {
//                    System.out.println("ripetizione non prenotabile");
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in prenota: "+ e.getMessage());
//        } finally {
//            //  updPren();
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return nIns;
//    }
//    //  -disdici
//    public static int disdici(Prenotazione prenotDC) throws ServletException {
//        Connection conn1 = null;
//        int nUpd=0;
//
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in disdici");
//                if(prenotazioni.contains(prenotDC)){
//                    //disdico in java
//                    prenotazioni.get(prenotazioni.indexOf(prenotDC)).getRip().setStatus(4);
//
//                    //aggiorno le altre ripetizioni
//                    Ripetizioni ripetDC = prenotDC.getRip();
//                    for(Ripetizioni r:ripetizioni){
//                        if(r.equals(ripetDC)){
//                            r.setStatus(0);
//                        }
//                        if(r.overlaps(ripetDC)){
//                            if(r.getStatus() == 2){
//                                r.setStatus(0);
//                            }
//                        }
//                    }
//                    //lavoro sul DB
//                    //cancello la prenotaz
//                    String qryDel = "UPDATE `prenotazioni` SET `status`='4' WHERE " +
//                            "user=? and giorno=? and corso_id=(select id from corso where nome=?) " +
//                            "and prof_id=(select id from professore where cognome=?) and ora=? ";
//                    String userDC = prenotDC.getUser();
//                    String giornoDC = ripetDC.getGiorno();
//                    String corsoDC = ripetDC.getCorso();
//                    String profDC = ripetDC.getProf();
//                    int oraDC = ripetDC.getOra();
//                    PreparedStatement stDel = conn1.prepareStatement(qryDel);
//                    stDel.setString(1, userDC);
//                    stDel.setString(2, giornoDC);
//                    stDel.setString(3, corsoDC);
//                    stDel.setString(4, profDC);
//                    stDel.setInt(5, oraDC);
//                    try{
//                        nUpd = stDel.executeUpdate();
//                    }catch (SQLException e){
//                        System.out.println(e.getMessage());
//                    }
//
//                    System.out.println("disdetta finita: " + nUpd + " prenotazioni modificate ");
//                } else {
//                    System.out.println("prenotazione inesistente");
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in disdici: "+ e.getMessage());
//        } finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return nUpd;
//
//    }
//    //  -segna come svolta
//    public static int setDone(Prenotazione pSD) throws ServletException {
//        //rimozione JDB
//        if(prenotazioni.contains(pSD)){
//            prenotazioni.get(prenotazioni.indexOf(pSD)).getRip().setStatus(3);
//            System.out.println("setDone in JDB: ok");
//        } else {
//            System.out.println("prenotazione not found in JDB");
//        }
//        //rimozione DB
//        Connection conn1 = null;
//        int nUpd=0;
//
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("\nConnected to the database test - in setDone");
//                Ripetizioni rSD = pSD.getRip();
//                String qry = "UPDATE `prenotazioni` SET `status`='3' WHERE " +
//                        "user=? and giorno=? and corso_id=(select id from corso where nome=?) " +
//                        "and prof_id=(select id from professore where cognome=?) and ora=? ";
//                String userDC = pSD.getUser();
//                String giornoDC = rSD.getGiorno();
//                String corsoDC = rSD.getCorso();
//                String profDC = rSD.getProf();
//                int oraDC = rSD.getOra();
//                PreparedStatement stUpd = conn1.prepareStatement(qry);
//                stUpd.setString(1, userDC);
//                stUpd.setString(2, giornoDC);
//                stUpd.setString(3, corsoDC);
//                stUpd.setString(4, profDC);
//                stUpd.setInt(5, oraDC);
//                nUpd = stUpd.executeUpdate();
//                System.out.println("DB upd done: "+ nUpd + " tuple modificate");
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in setDone: "+ e.getMessage());
//        } finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return nUpd;
//    }
//
//
//    //ADMIN OPS
//
//    //  - inserire prof
//    public static int insProf(String nome, String cognome) throws ServletException {
//        Connection conn1 = null;
//        int numInvolved=0;
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in insProf");
//                String qry = "INSERT INTO `professore`(`id`, `nome`, `cognome`) VALUES (NULL, ?,?)";
//                PreparedStatement st = conn1.prepareStatement(qry);
//                st.setString(1, nome);
//                st.setString(2,cognome);
//                System.out.println(st);
//                numInvolved = st.executeUpdate();
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in insProf: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return numInvolved;
//    }
//    //  - inserire corso
//    public static int insCorso(String nome) throws ServletException {
//        Connection conn1 = null;
//        int numInvolved=0;
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in insCorso");
//                String qry = "INSERT INTO `corso`(`id`, `nome`) VALUES (NULL, ?)";
//                PreparedStatement st = conn1.prepareStatement(qry);
//                st.setString(1, nome);
//                System.out.println(st);
//                numInvolved = st.executeUpdate();
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in insCorso: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return numInvolved;
//    }
//    //  - inserire insegnamento
//    public static int insInsegn(Corso cDI, Professore pDI) throws ServletException {
//        Connection conn1 = null;
//        int numInvolved=0;
//        try {
//            conn1 = DriverManager.getConnection(url1, user, password);
//            if (conn1 != null) {
//                System.out.println("Connected to the database test - in insInsegnamento");
//                String ncDI = cDI.getNome();
//                String npDI = pDI.getNome();
//                String cpDI = pDI.getCognome();
//
//                String qry = "INSERT INTO `insegnamenti`(`prof_id`, `corso_id`) " +
//                        "(SELECT professore.id, corso.id " +
//                        "FROM professore JOIN corso " +
//                        "WHERE corso.nome = ? AND professore.nome = ? AND professore.cognome = ?)";
//
//                PreparedStatement st = conn1.prepareStatement(qry);
//                st.setString(1, ncDI);
//                st.setString(2, npDI);
//                st.setString(3, cpDI);
//                System.out.println(st);
//                numInvolved = st.executeUpdate();
//                System.out.println("qry sent: tuple inserted= " + numInvolved);
//            }
//        } catch (SQLException e) {
//            throw new ServletException("SQL exc in insInsegn: "+ e.getMessage());
//        }
//        finally {
//            if (conn1 != null) {
//                try {
//                    conn1.close();
//                } catch (SQLException e2) {
//                    System.out.println(e2.getMessage());
//                }
//            }
//        }
//        return numInvolved;
//    }
//
//}
//

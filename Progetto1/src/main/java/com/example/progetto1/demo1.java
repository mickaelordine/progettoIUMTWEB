package com.example.progetto1;

import javax.servlet.ServletException;

public class demo1 {
    public static void main(String[] args) {

        //DBACCESS TEST

        DBA dao = null;
        try {
            dao = new DBA();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        dao.printDBAInfo();


        //todo non funziona, errore:
        //      Caused by: java.lang.NoClassDefFoundError: javax/servlet/ServletException

//        Random rng = new Random();
//        int toPren = rng.nextInt(20);
//        System.out.println("prenoteremo la rip #"+ toPren);
//        String p2 = "{\"corso\":\"prog3\",\"ora\":16}";
//
//            DBAccess.updPren();
//            DBAccess.popolaRipetiz();
//            Ripetizioni f = new Ripetizioni("ardissono", null, null, 15, 1);
//            DBAccess.prenota("gtessore", DBAccess.getRipetizioni().get(toPren));
//            DBAccess.disdici(DBAccess.getPrenotazioni().get(0));
//            logPrint("prenotazioni", DBAccess.getPrenotazioni());
//            logPrint("ripetizioni", DBAccess.getRipetizioni());
//            logPrint("filtered", DBAccess.jsonQryRip(p2));

        //FILTRI
/*
        Ripetizioni f = new Ripetizioni("ardissono", "tweb", 1, null, 15, 1);
        Ripetizioni r1 = new Ripetizioni("ardissono", "tweb", 1, "GIO", 15, 1);
        Ripetizioni r2 = new Ripetizioni("ardissono", "prog3", 1, "GIO", 15, 1);
        System.out.println("f1.fits(f)? "+r1.fitsReq(f));
        System.out.println("f2.fits(f)? "+r2.fitsReq(f));
*/

        //ES GSON

/*
        Ripetizioni r2 = new Ripetizioni(); //(null, "prog3", (Integer) null, null, (Integer) null, 1);
        Gson json = new Gson();
        String p2 = "{\"corso\":\"prog3\",\"ora\":16}";
        Ripetizioni r1 = json.fromJson(p2, r2.getClass());
        System.out.println(r1 + "\n" + p2);
        try {
            DBAccess.popolaRipetiz();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        ArrayList<Ripetizioni> res = DBAccess.jsonQryRip(p2) ;
        String finale = json.toJson(res);
        System.out.println(finale);
        Ripetizioni[] arr = json.fromJson(finale, Ripetizioni[].class);
        for(Ripetizioni r : arr){
            System.out.println(r);
        }
*/


        //TEST CONTAINS
        //    prendo la rip di una pren (=> di per se ha stato 1),
        //    setto stato a 0 e verifico che allRip contiene la rip modificata
/*
        DBAccess.popolaRipetiz();
        Ripetizioni r = DBAccess.getPrenotazioni().get(0).getRip();
        System.out.println(r);
        r.setStatus(0);
        System.out.println(r);
        logPrint("rips", DBAccess.getRipetizioni());
        System.out.println(DBAccess.getRipetizioni().contains(r));
*/

        //TEST SETDONE/DISDICI

/*
        DBAccess.popolaRipetiz();
        stampaPren();
        Random rng = new Random();
        for(int i=0;i<3;i++){
            int toPren = rng.nextInt(20);
            System.out.println("prenoto #"+toPren);
            DBAccess.prenota("gtessore", DBAccess.getRipetizioni().get(toPren));
        }
        stampaPren();
        stampaRip();
        System.out.println("set done pren #0");
        DBAccess.setDone(DBAccess.getPrenotazioni().get(0));
        System.out.println("disdico pren #1");
        DBAccess.disdici(DBAccess.getPrenotazioni().get(1));
        stampaPren();
        stampaRip();
*/


    }

//    public static void stampaRip(){
//        logPrint("ripetizioni", DBAccess.getRipetizioni());
//    }
//    public static void stampaPren(){
//        logPrint("prenotazioni", DBAccess.getPrenotazioni());
//    }
}

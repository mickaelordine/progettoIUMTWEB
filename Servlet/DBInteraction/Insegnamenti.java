package com.example.progetto1.DBInteraction;

public class Insegnamenti {
    private String profCognome;
    private String corso;
    private int profID;
    private int corsoID;


    public Insegnamenti(String profCognome, String corso, int pID, int cID) {
        this.profCognome = profCognome;
        this.corso = corso;
        this.corsoID = cID;
        this.profID = pID;
    }

    public String getProfCognome() {
        return profCognome;
    }

    public String getCorso() {
        return corso;
    }

    public int getProfID() {
        return profID;
    }

    public int getCorsoID() {
        return corsoID;
    }
}

package com.example.progetto1.DBInteraction;

import java.util.Objects;

public class Prenotazione {
    private String user;
    private Ripetizioni rip;

    /*
    nel DB hanno uno status:
        - 0 : prenotata
        - 1 : svolta
        - 2 : disdetta
    nel JDB uso il truschino di modificare lo stato della ripetizione
    nella prenotazione (non della ripetizione in se)
     */

    public Prenotazione() {}

    public Prenotazione(String user, Ripetizioni rip) {
        this.user = user;
        this.rip = rip;
    }

    public String getUser() {
        return user;
    }

    public Ripetizioni getRip() {
        return rip;
    }

    @Override
    public String toString() {
        return "ripetizione: [ " + this.rip + " ] \n\t|-->\tprenotata da "+ this.user;
    }

    @Override
    public boolean equals(Object pren2) {
        if (this == pren2) return true;
        if (pren2 == null || this.getClass() != pren2.getClass()) return false;
        Prenotazione that = (Prenotazione) pren2;
        return Objects.equals(this.user, that.user) && Objects.equals(this.rip, that.rip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, rip);
    }
}

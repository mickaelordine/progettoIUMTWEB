package com.example.progetto1.DBInteraction;

import java.lang.reflect.*;
import java.util.Objects;

public class Ripetizioni {

    private String prof;
    private String corso;
    private String giorno;
    private int ora;
    private int status;
/*
    status = 0 => prenotabile
    status = 1 => prenotata
    status = 2 => non prenotabile
        questi sotto sono solo nelle prenotazioni.ripetizioni
    status = 3 => effettuata
    status = 4 => disdetta
*/


    public Ripetizioni() {}

    public Ripetizioni(String prof, String corso, String giorno, int ora, int status) {
        this.prof = prof;
        this.corso = corso;
        this.giorno = giorno;
        this.ora = ora;
        this.status = status;
    }

    public String stato(){
        switch(this.status){
            case (0):{
                return "prenotabile";
            }
            case (1):{
                return "prenotata";
            }
            case (2):{
                return "non prenotabile";
            }
            case (3):{
                return "effettuata";
            }
            case (4):{
                return "disdetta";
            }
            default: {
                return "bug";
            }
        }
    }

    public String getGiorno() {
        return giorno;
    }

    public int getOra() {
        return ora;
    }

    public String getProf() {
        return prof;
    }

    public String getCorso() {
        return corso;
    }

    public int getStatus() {
        return status;
    }

    public boolean overlaps(Ripetizioni r){
        if(r.getProf().equals(this.prof) &&
            r.getGiorno().equals(this.giorno) &&
            r.getOra()==this.ora &&
            !(r.getCorso()==this.corso)){
            return true;
        } else {return false;}
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        //L'EQUALS NON TIENE CONTO DELLO STATUS !!!
        //      quindi non ne tiene conto nemmeno prenotabile e fitsreq
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ripetizioni that = (Ripetizioni) o;
        return ora == that.ora && Objects.equals(prof, that.prof) && Objects.equals(corso, that.corso) && Objects.equals(giorno, that.giorno);
    }

    public boolean fitsReq(Ripetizioni filter) {
        //gli passo una ripetizione come filtro, mi dice se this ha gli stessi valori assegnati di filter
        try{
            Field[] fl = filter.getClass().getDeclaredFields();
            for(int i=0;i<fl.length;i++){
                Field f = fl[i];
                if(f.get(filter) == null ||                                         // SE: filter.f è null  OR
                        f.getName().equals("status") ||                             // f == status          OR
                        ((f.getType().equals(int.class)) && (f.getInt(filter)==0))  // ( f è int AND filter.f == 0 )
                    ){                                                                      //questo perchè jsonando un int vuoto viene settato a 0
                                                                                        //non far nulla
                } else {                                                            //ELSE
                    if(!(f.get(filter).equals(f.get(this)))){                           //compara e se sono diversi
                        return false;
                    }
                }
            }
        } catch (IllegalAccessException e){
            System.out.println(e.getMessage());
        }
        return true;
    }


    @Override
    public int hashCode() {
        return Objects.hash(prof, corso, giorno, ora, status);
    }

    @Override
    public String toString() {
            return prof + "\t| " + corso + "\t| "+ giorno + ", "+ ora +"-" + (ora +1) + ", stato: " + this.stato() + "("+status+")";
    }
}
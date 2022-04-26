package com.ium.example.progetto.BookingTest;

public class Lesson {
//        ArrayList<String> date = new ArrayList<>();
//        ArrayList<String> name = new ArrayList<>();
//        ArrayList<String> materia = new ArrayList<>();
//        ArrayList<String> orario = new ArrayList<>();
        String corso, name, giorno,ora;

        public Lesson(String corso, String name, String ora, String giorno){
            this.name = name;
            this.corso = corso;
            this.giorno = giorno;
            this.ora = ora;
        }

        public void clear(){
            this.name = null;
            this.corso = null;
            this.giorno = null;
            this.ora = null;
        }
}

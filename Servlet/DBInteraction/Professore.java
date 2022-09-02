package com.example.progetto1.DBInteraction;

public class Professore {
    private int id;
    private String nome;
    private String cognome;


    public Professore(int id, String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "| " + id + "\t| " + nome + "\t| " + cognome;
    }
}
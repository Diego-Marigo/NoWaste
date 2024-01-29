package com.example.nowaste;

public class ListaItem {
    private String nomeLista;
    private String userId;

    /**
     * Costruttore vuoto.
     */
    public ListaItem() {
    }

    public ListaItem(String nomeLista, String userId) {
        this.nomeLista = nomeLista;
        this.userId = userId;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public String getUserId() {
        return userId;
    }
}


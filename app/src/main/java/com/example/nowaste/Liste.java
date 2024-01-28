package com.example.nowaste;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Classe che rappresenta le Liste all'interno del database.
 *
 * @author martinaragusa
 */
@IgnoreExtraProperties
public class Liste {
    /*
    una lista avrà un nome e un id; poi avrà l'userid dell'utente che crea la lista
     */
    public String nomeLista, userId;

    /**
     * Costruttore vuoto.
     */
    public Liste() {
    }

    /**
     * Costruttore per la creazione di un oggetto di tipo Lista.
     * @param nomeLista Nome della lista dell'utente
     */
    public Liste(String nomeLista, String userId) {
        this.nomeLista = nomeLista;
        this.userId = userId;
    }
}

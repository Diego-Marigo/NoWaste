package com.example.nowaste;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Classe che rappresenta gli alimenti all'interno del database.
 */
@IgnoreExtraProperties
public class Alimenti {
    /*
    un alimento avrà nome, quantità, data di scadenza
    devo avere anche l'id della lista a cui lo sto aggiungendo
     */

    public String nomeAlimento;
    public int quantity;
    public Date dataScadenza;
    public String listId;
    /**
     * Costruttore vuoto.
     */
    public Alimenti() {
    }

    /**
     * Costruttore per la creazione di un oggetto di tipo Alimento.
     * @param nomeAlimento Nome dell'alimento
     * @param quantity Quantità dell'alimento
     * @param dataScadenza Data di scadenza dell'alimento
     */
    public Alimenti(String nomeAlimento, int quantity, Date dataScadenza, String listId) {
        this.nomeAlimento = nomeAlimento;
        this.quantity = quantity;
        this.dataScadenza = dataScadenza;
        this.listId = listId;
    }
}

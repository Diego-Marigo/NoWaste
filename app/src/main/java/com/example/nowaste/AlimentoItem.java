package com.example.nowaste;

public class AlimentoItem {
    private String nomeAlimento;
    private String dataScadenza;
    private int quantity;
    private String alimentoId;

    /**
     * Costruttore vuoto.
     */
    public AlimentoItem() {
    }

    public AlimentoItem(String nomeAlimento, String alimentoId, int quantity, String data) {
        this.nomeAlimento = nomeAlimento;
        this.alimentoId = alimentoId;
        this.dataScadenza = data;
        this.quantity = quantity;
    }

    public String getNomeAlimento() {
        return nomeAlimento;
    }

    public String getAlimentoId() {
        return alimentoId;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getDate() {
        return dataScadenza;
    }
}

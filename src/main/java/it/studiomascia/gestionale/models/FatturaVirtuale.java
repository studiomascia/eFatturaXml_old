/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.Date;

/**
 *
 * @author luigi
 */
public class FatturaVirtuale {
   private Integer id;
   private Date dataReg;
   private String nReg;
   private Date dataFattura;
   private String nFattura;
   private String pIva;
   private String denominazione;
   private String imponibile;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the dataReg
     */
    public Date getDataReg() {
        return dataReg;
    }

    /**
     * @param dataReg the dataReg to set
     */
    public void setDataReg(Date dataReg) {
        this.dataReg = dataReg;
    }

    /**
     * @return the nReg
     */
    public String getnReg() {
        return nReg;
    }

    /**
     * @param nReg the nReg to set
     */
    public void setnReg(String nReg) {
        this.nReg = nReg;
    }

    /**
     * @return the dataFattura
     */
    public Date getDataFattura() {
        return dataFattura;
    }

    /**
     * @param dataFattura the dataFattura to set
     */
    public void setDataFattura(Date dataFattura) {
        this.dataFattura = dataFattura;
    }

    /**
     * @return the nFattura
     */
    public String getnFattura() {
        return nFattura;
    }

    /**
     * @param nFattura the nFattura to set
     */
    public void setnFattura(String nFattura) {
        this.nFattura = nFattura;
    }

    /**
     * @return the pIva
     */
    public String getpIva() {
        return pIva;
    }

    /**
     * @param pIva the pIva to set
     */
    public void setpIva(String pIva) {
        this.pIva = pIva;
    }

    /**
     * @return the denominazione
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * @param denominazione the denominazione to set
     */
    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    /**
     * @return the imponibile
     */
    public String getImponibile() {
        return imponibile;
    }

    /**
     * @param imponibile the imponibile to set
     */
    public void setImponibile(String imponibile) {
        this.imponibile = imponibile;
    }
   
}

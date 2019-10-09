/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Admin
 */
@Entity
public class ControlloFattura {

    /**
     * @return the centroDiCosto
     */
    public String getCentroDiCosto() {
        return centroDiCosto;
    }

    /**
     * @param centroDiCosto the centroDiCosto to set
     */
    public void setCentroDiCosto(String centroDiCosto) {
        this.centroDiCosto = centroDiCosto;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;    

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_controllo")
    private Date dataControllo;
      
    @Column(name = "note")
    private  String note;
    
    @Column(name = "centro_di_costo")
    private  String centroDiCosto;
      
    @Column(name = "controllata")
    private  Boolean controllata;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DBFile> fileControlloFattura;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the dataControllo
     */
    public Date getDataControllo() {
        return dataControllo;
    }

    /**
     * @param dataControllo the dataControllo to set
     */
    public void setDataControllo(Date dataControllo) {
        this.dataControllo = dataControllo;
    }

       /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the controllata
     */
    public Boolean isControllata() {
        return this.getControllata();
    }

    /**
     * @param controllata the controllata to set
     */
    public void setControllata(Boolean x) {
        this.controllata = x;
    }

    /**
     * @return the fileControlloFattura
     */
    public Set<DBFile> getFilesControlloFattura() {
        return fileControlloFattura;
    }

    public List<DBFile> getListaFilesControlloFattura() {
        return new ArrayList(fileControlloFattura);
    }
    /**
     * @param fileControlloFattura the fileControlloFattura to set
     */
    public void setFilesControlloFattura(Set<DBFile> filesControlloFattura) {
        this.fileControlloFattura = filesControlloFattura;
    }

    /**
     * @return the controllata
     */
    public Boolean getControllata() {
        return controllata;
    }

    
}

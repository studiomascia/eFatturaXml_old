/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;


import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author luigi
 */
@Entity
@Table(name = "xmlfatturabase")

public class XmlFatturaBase {
    
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    
    @Temporal(TemporalType.TIMESTAMP)    
    @Column(name = "data_inserimento")
    private Date dataInserimento;
    
    @Size(max = 255)
    @Column(name = "numero_fattura")
    private String numeroFattura;
    
    @Temporal(TemporalType.TIMESTAMP)    
    @Column(name = "data_fattura")
    private Date dataFattura;
    
    @Lob
    private String xmlString;

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
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the dataInserimento
     */
    public Date getDataInserimento() {
        return dataInserimento;
    }

    /**
     * @param dataInserimento the dataInserimento to set
     */
    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    /**
     * @return the numeroFattura
     */
    public String getProtocolloFattura() {
        return numeroFattura;
    }

    /**
     * @param protocolloFattura the numeroFattura to set
     */
    public void setProtocolloFattura(String protocolloFattura) {
        this.numeroFattura = protocolloFattura;
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
     * @return the stringaXml
     */
    public String getXmlData() {
        return xmlString;
    }

    /**
     * @param data the data to set
     */
    public void setXmlData(String data) {
        this.xmlString = data;
    }
    
    public XmlFatturaBase(Integer id) {
        this.id = id;
    }

    public XmlFatturaBase() {
    }
    
}

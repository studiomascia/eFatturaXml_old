/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;


import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author luigi
 */
@Entity
@Table(name = "xmlfatturabase")
@NamedQueries({
//    @NamedQuery(name = "XmlFatturaBase.findAllPassive", query = "SELECT x FROM XmlFatturaBase x WHERE x.attiva IS FALSE"),
    @NamedQuery(name = "XmlFatturaBase.findPassiveNotRegistered", query = "SELECT x FROM XmlFatturaBase x WHERE (x.attiva IS FALSE) AND (x.numeroRegistrazione IS NULL) "),
    @NamedQuery(name = "XmlFatturaBase.findAttiveNotRegistered", query = "SELECT x FROM XmlFatturaBase x WHERE (x.attiva IS TRUE) AND (x.numeroRegistrazione IS NULL) ")
//    @NamedQuery(name = "XmlFatturaBase.findAllAttive", query = "SELECT x FROM XmlFatturaBase x WHERE x.attiva IS TRUE")
})

public class XmlFatturaBase {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_inserimento")
    private Date dataInserimento;
    
    @Size(max = 255)
    @Column(name = "numero_registrazione")
    private String numeroRegistrazione;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_registrazione")
    private Date dataRegistrazione;
    
    @Column(name = "creatore")
    private  String creatore;
    
    @Lob
    private String xmlString;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn( name="idAnagraficaSocieta" )
    private AnagraficaSocieta anagraficaSocieta;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Pagamento> pagamenti;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ControlloFattura> controlli;
    

    @OneToMany(mappedBy="xmlFatturaBase", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Ddt> ddt;
    
    public Set<Ddt> getDDT() {
        return ddt;
    }
     
    public void setDDT(Set<Ddt> listaDDT) {
        this.ddt = listaDDT;
    }
     
    public AnagraficaSocieta getAnagraficaSocieta() {
        return anagraficaSocieta;
    }
    public void setAnagraficaSocieta(AnagraficaSocieta provider) {
        this.anagraficaSocieta = provider;
    }
     
    
    /**
     * @return the pagamenti
     */
    public Set<Pagamento> getPagamenti() {
        return pagamenti;
    }
    /**
     * @param pagamenti the pagamenti to set
     */
    public void setPagamenti(Set<Pagamento> listaPagamenti) {
        this.pagamenti = listaPagamenti;
    }
     
    public boolean isSaldata() {
        Boolean ret=false;
        for (Pagamento x : pagamenti){
        	if (x.isSaldata()==Pagamento.SALDATA) ret =true;
        }
     return ret;   
    }
    public boolean isSaldataParz() {
        Boolean ret=false;
        for (Pagamento x : pagamenti){
        	if (x.isSaldata()==Pagamento.PARZ_SALDATA) ret =true;
        }
     return ret;   
    }

    public boolean isSaldataAuto() {
        Boolean ret=false;
        for (Pagamento x : pagamenti){
        	if (x.isSaldata()==Pagamento.SALDATA_AUTO) ret =true;
        }
     return ret;   
    }

    
    // MODIFICARE I METODI E FARE IN MODO CHE SI VALUTI SOLO IL SALDO PIU RECENTE OVVERO CON ID PIU ALTO
    public int getTipoSaldo(){
    int ret=0;
     if (this.isSaldata()) ret = Pagamento.SALDATA; else 
     if (this.isSaldataParz()) ret = Pagamento.PARZ_SALDATA; else
     if (this.isSaldataAuto()) ret = Pagamento.SALDATA_AUTO;
    
    return ret;
    }
    
    // MODIFICARE I METODI E FARE IN MODO CHE SI VALUTI SOLO IL CONTROLLO PIU RECENTE OVVERO CON ID PIU ALTO
    public int getTipoControllo(){
        int ret=0;
        if (controlli.size()>0){
            if (this.isControllataOK()) ret = ControlloFattura.VALIDATA; else 
                if (this.isControllataNOK()) ret = ControlloFattura.BLOCCATA; else
                    if (this.isControllataWAIT()) ret = ControlloFattura.PENDING; 
        }
        return ret;
    }
    
    // MODIFICARE I METODI E FARE IN MODO CHE SI VALUTI SOLO IL CONTROLLO PIU RECENTE OVVERO CON ID PIU ALTO
    public boolean isControllataOK() {
        Boolean ret=false;
        if (controlli.size()>0)
            for (ControlloFattura x : controlli){
                if (x.isControllata()== ControlloFattura.VALIDATA) ret =true;
        }
     return ret;   
    }
    
    // MODIFICARE I METODI E FARE IN MODO CHE SI VALUTI SOLO IL CONTROLLO PIU RECENTE OVVERO CON ID PIU ALTO
    public boolean isControllataNOK() {
        Boolean ret=false;
        for (ControlloFattura x : controlli){
        	if (x.isControllata()== ControlloFattura.BLOCCATA) ret =true;
        }
     return ret;   
    }
    // MODIFICARE I METODI E FARE IN MODO CHE SI VALUTI SOLO IL CONTROLLO PIU RECENTE OVVERO CON ID PIU ALTO
    public boolean isControllataWAIT() {
        Boolean ret=false;
        for (ControlloFattura x : controlli){
        	if (x.isControllata()== ControlloFattura.PENDING) ret =true;
        }
     return ret;   
    }


    
      /**
     * @return the controlli
     */
    
//    private ControlloFattura getLastControlloFattura() {
//        
//        return controlli.
//    }
     
    public Set<ControlloFattura> getControlli() {
        return controlli;
    }

    /**
     * @param controlli the controlli to set
     */
    public void setControlli(Set<ControlloFattura> controlli) {
        this.controlli = controlli;
    }
     
   
    

    
    private boolean attiva;
    
    /**
     * @return the attiva status
     */
    public boolean isAttiva() {
        return this.attiva;
    }

    /**
     * @param attiva the id to set
     */
    public void setAttiva(boolean x ) {
        this.attiva = x;
    }
    
    
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
     * @return the numeroRegistrazione
     */
    public String getNumeroRegistrazione() {
        return this.numeroRegistrazione;
    }

    /**
     * @param protocolloFattura the numeroRegistrazione to set
     */
    public void setNumeroRegistrazione(String protocolloFattura) {
        this.numeroRegistrazione = protocolloFattura;
    }

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return this.dataRegistrazione;
    }

    /**
     * @param dataRegistrazione the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @return the stringaXml
     */
    public String getXmlData() {
        return this.xmlString;
    }

    /**
     * @param data the data to set
     */
    public void setXmlData(String data) {
        this.xmlString = data;
    }
    
    public XmlFatturaBase(Integer id) {
        this.id = id;
        this.attiva=false;
    }

    public XmlFatturaBase() {
        this.attiva=false;
    }
    
     public XmlFatturaBase(boolean isAttiva) {
        this.attiva=isAttiva;
    }

    /**
     * @return the creatore
     */
    public String getCreatore() {
        return creatore;
    }

    /**
     * @param creatore the creatore to set
     */
    public void setCreatore(String creatore) {
        this.creatore = creatore;
    }

    public FatturaElettronicaType getFatturaElettronica()
    {
        FatturaElettronicaType item = new FatturaElettronicaType();
        byte[] byteArr;
        try 
        {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
            // Unmarshaller serve per convertire il file in un oggetto
            Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
            // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
              byteArr = this.getXmlData().getBytes("UTF-8");
                sw = new StringWriter();
          
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                item = root.getValue();
                //jaxbMarshaller.marshal(root, sw);
        }
        catch (JAXBException e) 
        {
            e.printStackTrace();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        return item;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.models.XmlFatturaBasePredicate;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.xml.AnagraficaType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
@Slf4j
public class XmlFatturaBaseService {
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
        
     /* INIZIO Metodi comuni per tutti i mapping */
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");
    
    // Prepara la Map da aggiungere alla view 
    public List<String> getHeaders (){
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("TD");
        headers.add("Data Reg.");
        headers.add("N.Reg.");
        headers.add("Data");
        headers.add("Numero");
        headers.add("P.IVA");
        headers.add("Denominazione");
        headers.add("Importo");  
        return headers;
    }

    public List<Map<String, Object>> getRows (){
        List<XmlFatturaBase> listaFatture = xmlFatturaBaseRepository.findByAttivaFalse();
        return getRows (listaFatture);
    }
    
        
        
    public List<Map<String, Object>> getRows (List<XmlFatturaBase> listaFatture){
        
//        List<XmlFatturaBase> listaFatture = XmlFatturaBasePredicate.filterXmlFatturaBase(xmlFatturaBaseRepository.findAll(), XmlFatturaBasePredicate.isPassiva());
        
 
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        String  strData = null;
        byte[] byteArr;
        try {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
            // Unmarshaller serve per convertire il file in un oggetto
            Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
            // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            int conta =0;
            
            for (XmlFatturaBase xmlFattura:listaFatture) {
//                listaFatture.parallelStream().forEach(xmlFattura ->{ 
                //log.info("Conteggio fatture elaborate= " + conta++);

                byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                strData = "";
                sw = new StringWriter();
            
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);
                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione="";
                if (item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione()!=null)
                {
                    denominazione=item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                }else{
                    AnagraficaType  anagrafica = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica();
                    if (anagrafica.getTitolo()!=null)  denominazione+= anagrafica.getTitolo() +" ";
                    if (anagrafica.getNome()!=null)  denominazione+= anagrafica.getNome() +" ";
                    if (anagrafica.getCognome()!=null)  denominazione+= anagrafica.getCognome() +" ";
                }
//                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String importoFattura= "0";
                    // Alcune fatture con importo 0 hanno un valore null 
                    if (item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento() !=null){
                        importoFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                    }
                strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());

                String descrizione = "N/A";
                if (item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getCausale()!=null ){
                   descrizione= item.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDettaglioLinee().get(0).getDescrizione();
                }

                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("TD", item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getTipoDocumento().value() );   
                riga.put("Data Reg.",  strData);
                riga.put("N.Reg.", xmlFattura.getNumeroRegistrazione());
//                riga.put("Data",  formattaData.format(dataFattura));
                riga.put("Data",  formattaData.format(dataFattura));
                riga.put("Numero", numeroFattura);
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Descrizione",descrizione );
                riga.put("Importo", importoFattura);
                riga.put("Saldata", xmlFattura.getStatoPagamento());
                riga.put("Controllata", xmlFattura.getEsitoUltimoControllo());
                righe.add(riga); 
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 

       return righe;
    }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
}

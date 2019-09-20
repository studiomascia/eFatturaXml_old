/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.FatturaVirtuale;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.models.XmlFatturaBasePredicate;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luigi
 */
@RestController
public class InvoiceRestController {

@Autowired
private XmlFatturaBaseRepository xmlFatturaBaseRepository;

 
 @GetMapping("/getFattureIn")
    public List<FatturaVirtuale> FatturePassiveList(HttpServletRequest request,Model model){
        
       
        //FINE:: BLOCCO PER LA PAGINAZIONE
        List<XmlFatturaBase> listaFatture = XmlFatturaBasePredicate.filterXmlFatturaBase(xmlFatturaBaseRepository.findAll(), XmlFatturaBasePredicate.isPassiva());
        List<FatturaVirtuale> listaFattureVirtuali = new ArrayList<FatturaVirtuale>();
     
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
       
            for (XmlFatturaBase xmlFattura:listaFatture) {
                byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                sw = new StringWriter();
            
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
             
                FatturaVirtuale riga = new FatturaVirtuale();
                riga.setId(xmlFattura.getId());   
                riga.setDataReg(xmlFattura.getDataRegistrazione()); 
                riga.setnReg(xmlFattura.getNumeroRegistrazione()); 
                
                riga.setDataFattura(dataFattura);
                riga.setnFattura(numeroFattura);
                riga.setpIva(partitaIVA );
                riga.setDenominazione(denominazione );
                riga.setImponibile( importoFattura);
                listaFattureVirtuali.add(riga); 
        
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return listaFattureVirtuali;
    }
  
 
}

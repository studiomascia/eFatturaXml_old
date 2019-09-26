/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.DBFileRepository;
import it.studiomascia.gestionale.repository.PagamentoRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.DBFileStorageService;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import static org.springframework.ui.ModelExtensionsKt.set;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * @author luigi
 */
@Controller
public class PagamentoController {
    
    private static final Logger logger = LoggerFactory.getLogger(PagamentoController.class);

    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private DBFileStorageService DBFileStorageService;

    @Autowired
    private DBFileRepository dbFileRepository;
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaRepository;
    
    @GetMapping("/InvoiceIn/{fatturaId}/Payments")
    public String PagamentiFattura(Model model, @PathVariable String fatturaId){
        List<String> headers = new  ArrayList<>();
            headers.add("Id");
            headers.add("Registro IVA");
            headers.add("Data");
            headers.add("Numero");
            headers.add("P.IVA");
            headers.add("Denominazione");
            headers.add("Imponibile");
            
            String strData="N/A";
            Integer id = Integer.valueOf(fatturaId);
            XmlFatturaBase xmlFattura = xmlFatturaRepository.findById(id).get();
            try {
                byte[] byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String partitaIVA =  item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
            
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("Registro IVA",xmlFattura.getNumeroRegistrazione()+ " - " +  LocalDateTime.ofInstant(xmlFattura.getDataRegistrazione().toInstant(), ZoneId.systemDefault()).toLocalDate());
                 strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());
            
                riga.put("Data", strData);
                riga.put("Numero", numeroFattura);
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Imponibile", importoFattura);
             
                model.addAttribute("fattura", riga);
                model.addAttribute("headers", headers);
 
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }     
            
        Set<Pagamento> listapagamenti= xmlFattura.getPagamenti();
        
        List<DBFile> listaFile = new ArrayList<DBFile>();
        for (Pagamento x : listapagamenti){
//            List<DBFile> files = dbFileRepository.findAll();
//            int k = files.size();
//            for (DBFile f:  x.getFilesPagamenti())
//            {
//                listaFile.add(f);
//            }
            listaFile.addAll(x.getFilesPagamenti());
        	
        }
        
        List lista = new ArrayList(listapagamenti);
        model.addAttribute("listafiles", listaFile);
        model.addAttribute("listapagamenti", lista);
        return "lista_pagamenti_fattura";
    }
    
   
 
   
      
    

}

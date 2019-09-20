/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luigi
 */
@RestController
public class InvoiceRestController {

@Autowired
private XmlFatturaBaseRepository xmlFatturaBaseRepository;

 
 @GetMapping("/findOne")
    public XmlFatturaBase createFindOne(@RequestParam("id") Integer id) {
        XmlFatturaBase item = new XmlFatturaBase();
        XmlFatturaBase tmp = xmlFatturaBaseRepository.findById(id).get();
        //init
        item.setId(tmp.getId());
        item.setAttiva(tmp.isAttiva());
        item.setDataInserimento(tmp.getDataInserimento());
        item.setFileName(tmp.getFileName());
        item.setDataRegistrazione(tmp.getDataRegistrazione());
        item.setNumeroRegistrazione(tmp.getNumeroRegistrazione());
//        item.setXmlString(tmp.getXmlString());
        

        return item;
    }
 
}

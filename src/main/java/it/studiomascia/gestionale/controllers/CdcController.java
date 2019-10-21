/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.Cdc;
import it.studiomascia.gestionale.repository.CdcRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author luigi
 */
@Controller
public class CdcController {

    
    @Autowired
    private CdcRepository cdcRepository;
    


    
    @GetMapping("/Cdc")
    public String cdcList(HttpServletRequest request,Model model){
//     RestTemplate    restTemplate = new RestTemplate();
//     ResponseEntity<Cdc[]> responseEntity;
//    responseEntity = restTemplate.getForEntity("http://localhost:8080/CdcRestList", Cdc[].class);
//    List<Cdc> lista = Arrays.asList(responseEntity.getBody());
    List<Cdc> lista = cdcRepository.findAll();
    model.addAttribute("lista", lista);
    return "cdc_lista";
    }
    
    @GetMapping("/SetCdc")
    public String setCdcList(HttpServletRequest request,Model model){
        
        Cdc cdc1 = new Cdc();
        cdc1.setAttivo(Boolean.TRUE);
        cdc1.setText("CDC 1");
        cdcRepository.save(cdc1);
        
        Cdc cdc2 = new Cdc();
        cdc2.setAttivo(Boolean.TRUE);
        cdc2.setText("CDC 2");
        cdcRepository.save(cdc2);

        Cdc cdc0 = new Cdc();
        cdc0.setAttivo(Boolean.TRUE);
        cdc0.setText("CDC E1");
        cdc0.addNode(cdc1);
        cdc0.addNode(cdc2);
        cdcRepository.save(cdc0);
           
        return "cdc_lista";
    }
   
}

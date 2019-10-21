/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.UserRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author luigi
 */
@Controller
public class UserDashboardController {
 
   @Autowired
   UserRepository utenti_repository;
   
   @Autowired
   XmlFatturaBaseRepository xmlfattura_repository;
   
    @GetMapping("/Dashboard")
    public String Dashboard(Model model){
        
        model.addAttribute("numUtenti", utenti_repository.findAll().size());
        model.addAttribute("nFatturePassive", xmlfattura_repository.findByAttivaFalse().size());
        model.addAttribute("nFattureAttive", xmlfattura_repository.findByAttivaTrue().size());  

        model.addAttribute("nFatturePassiveNR", xmlfattura_repository.findPassiveNotRegistered().size());
        model.addAttribute("nFattureAttiveNR", xmlfattura_repository.findAttiveNotRegistered().size());

        return "user_dashboard";
    }
    
}
   
   


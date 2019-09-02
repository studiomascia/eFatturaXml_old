/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.Utenti;
import it.studiomascia.gestionale.repository.UtentiRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
/**
 *
 * @author luigi
 */
@Controller
public class UtentiController {
    
   @Autowired
   UtentiRepository utenti_repository;
    

      
    @GetMapping("/Utenti")
    public String UtentiList(HttpServletRequest request,Model model){
        
        //INIZIO:: BLOCCO PER LA PAGINAZIONE
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 3; //default page size is 10
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        //FINE:: BLOCCO PER LA PAGINAZIONE
       Page<Utenti> lista = utenti_repository.findAll(PageRequest.of(page, size));
//       lista.getPageable().getPageNumber()
        model.addAttribute("lista_utenti", lista);
        model.addAttribute("currentPage", page);
        model.addAttribute("messaggio", "messaggio da mostrare");
    return "lista_utenti";
    }
    
    @GetMapping("/Utenti/Nuovo")
    public String NuovoUtente(Model model){
        Utenti x = new Utenti();
        model.addAttribute("utente",x);
        return "/nuovo_utente";
    }
    
    @PostMapping("/Utenti/Nuovo")
    public String NuovoUtente(@Valid @ModelAttribute("utente") Utenti utente, BindingResult bindingResult,Model model)
    {
        model.addAttribute("utente",utente);
        if (bindingResult.hasErrors()) {
            return "/nuovo_utente";
        }else{
        utente.setUltimoAccesso(new Date());
        utenti_repository.save(utente);
        return "redirect:/Utenti";
        }
    }
    
    @GetMapping("/Utenti/Edit/{id}")
    public String NuovoUtente(Model model,@PathVariable Integer id){
        Utenti x = utenti_repository.findById(id).get();
        model.addAttribute("utente",x);
        return "/modifica_utente";
    }
    
    @PostMapping("/Utenti/Edit")
    public String AggiornaUtente(@Valid @ModelAttribute("utente") Utenti utente, BindingResult bindingResult,Model model)
    {
        System.out.println("Utente.id=" + utente.getId());        
//model.addAttribute("utente",utente);
        if (bindingResult.hasErrors()) {
            return "/modifica_utente";
        }else{
        utente.setUltimoAccesso(new Date());
        utenti_repository.save(utente);
        return "redirect:/Utenti";
        }
    }
    
  
      
}

package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.Role;
import  it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.UserRepository;
import  it.studiomascia.gestionale.service.SecurityService;
import  it.studiomascia.gestionale.service.UserService;
import it.studiomascia.gestionale.validator.UserValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/Admin/SetDefaultUsers")
    public String insertDefaultUsers() {
                
        User u1 = new User();
        u1.setUsername("admin@admin.it");
        u1.setPassword("123123");
        u1.setStato(1);
        userService.save(u1);
        
        return "login";
    }
    
    @GetMapping("/Admin/Users")
    public String UtentiList(HttpServletRequest request,Model model){
        System.out.println("GetMapping(/Admin/Users) INIZIO");
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
       Page<User> lista = userRepository.findAll(PageRequest.of(page, size));
//       lista.getPageable().getPageNumber()
        model.addAttribute("utente_lista", lista);
        model.addAttribute("currentPage", page);
        model.addAttribute("messaggio", "messaggio da mostrare");
        System.out.println("GetMapping(/Admin/Users) FINE");
    return "utente_lista";
    }
  
    @GetMapping("/Admin/User/{id}")
    public String EditUtente(Model model,@PathVariable Long id){
        User x = userRepository.findById(id).get();
        model.addAttribute("utente",x);
        return "/utente_modifica";
    }
    
    @PostMapping("/Admin/User/{id}")
    public String AggiornaUtente(@Valid @ModelAttribute("utente") User utente, BindingResult bindingResult,Model model)
    {
        System.out.println("Utente.id=" + utente.getId());        
//model.addAttribute("utente",utente);
        if (bindingResult.hasErrors()) {
            return "/utente_modifica";
        }else{
//        utente.setUltimoAccesso(new Date());
        userRepository.save(utente);
        return "redirect:/Utenti";
        }
    }
    
    
    @GetMapping("Admin/User/Registration")
    public String NuovoUtente(Model model){
        User x = new User();
        model.addAttribute("utente",x);
        return "/utente_registrazione";
    }
    
    
  
    
    @PostMapping("Admin/User/Registration")
    public String NuovoUtente(@Valid @ModelAttribute("utente") User utente, BindingResult bindingResult,Model model)
    {
        userValidator.validate(utente, bindingResult);
        model.addAttribute("utente",utente);
        if (bindingResult.hasErrors()) {
            return "/utente_registrazione";
        }else{
        userRepository.save(utente);
            return "redirect:/Admin/Users";
        }
    }
    
   
}
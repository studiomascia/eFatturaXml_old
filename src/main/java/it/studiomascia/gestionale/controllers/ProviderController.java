package it.studiomascia.gestionale.controllers;

import  it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.UserRepository;
import it.studiomascia.gestionale.service.AnagraficaSocietaService;
import  it.studiomascia.gestionale.service.SecurityService;
import  it.studiomascia.gestionale.service.UserService;
import it.studiomascia.gestionale.validator.UserValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProviderController {
    
    @Autowired
    AnagraficaSocietaService  providerService;
    
    @GetMapping("/ProvidersList")
    public String providersList(HttpServletRequest request,Model model){

        
        model.addAttribute("headers", providerService.getHeaders());
        model.addAttribute("rows", providerService.getRows());

    return "providers_lista";
    }
   
}
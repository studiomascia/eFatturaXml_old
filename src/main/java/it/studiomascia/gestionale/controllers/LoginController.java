/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.Utenti;
import it.studiomascia.gestionale.repository.UtentiRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author luigi
 */
@Controller
public class LoginController {
    
    @Autowired
    UtentiRepository utenti_repository;
    
    @GetMapping("/Login")
    public String Login(HttpServletRequest request, Model model) 
    {
        return "login";
    }

    @PostMapping("/Login")
    public String doLogin(HttpServletRequest request, Model model)
    {
        String email = "";
        if (request.getParameterMap().get("inputEmail") != null && request.getParameterMap().get("inputEmail").length > 0) {
            email = request.getParameter("inputEmail");
        }

        String pwd = "";
        if (request.getParameterMap().get("inputPassword") != null && request.getParameterMap().get("inputPassword").length > 0) {
            pwd = request.getParameter("inputPassword");
        }
        Utenti u = utenti_repository.findByEmailAndPassword(email, pwd).get();
        if (u != null) {
            return "lista_utenti";
        } else {
            return "login";
        }

    }
}

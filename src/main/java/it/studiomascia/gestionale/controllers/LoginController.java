/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author luigi
 */
@Controller
public class LoginController {
    
    @Autowired
    UserRepository utenti_repository;
     
    @GetMapping("/login")
    public String Login(Model model, String error, String logout) 
    {
        if (error!= null)
            model.addAttribute("error","Username e Password non validi");
        if (logout!= null)
            model.addAttribute("message","Il Logout è avvenuto correttamente");
        
        return "login";
    }
 
    @PostMapping("/login")   
    public String doLogin(HttpServletRequest request, Model model)
    { 
        String email = "";
        if (request.getParameterMap().get("username") != null && request.getParameterMap().get("username").length > 0) {
            email = request.getParameter("username");
        }

        String pwd = "";
        if (request.getParameterMap().get("password") != null && request.getParameterMap().get("password").length > 0) {
            pwd = request.getParameter("password");
        }
        User u = utenti_repository.findByUsername(email);
        if (u != null) {
            return "lista_utenti";
        } else {
            return "login";
        }

    }
    
    
    @GetMapping("/perform_login")
    @PostMapping("/perform_login")
    public String perform_login(HttpServletRequest request, Model model)
    {
        return "DashboardLogin";
    }
    
//     @PostMapping("/login")
//    public String doLogin2(HttpServletRequest request, Model model)
//    {
//        String email = "";
//        if (request.getParameterMap().get("inputEmail") != null && request.getParameterMap().get("inputEmail").length > 0) {
//            email = request.getParameter("inputEmail");
//        }
//
//        String pwd = "";
//        if (request.getParameterMap().get("inputPassword") != null && request.getParameterMap().get("inputPassword").length > 0) {
//            pwd = request.getParameter("inputPassword");
//        }
//        User u = utenti_repository.findByUsername(email);
//        if (u != null) {
//            return "lista_utenti";
//        } else {
//            return "login2";
//        }
//
//    }
//    
}

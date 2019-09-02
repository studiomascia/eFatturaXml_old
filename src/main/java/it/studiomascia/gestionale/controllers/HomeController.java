/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author luigi
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
            return "template_vuoto";
    }
    @GetMapping("/index")
    public String home2(){
            return home();
    }
}

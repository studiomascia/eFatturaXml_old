/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.Cdc;
import it.studiomascia.gestionale.repository.CdcRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luigi
 */
@RestController
public class CdcRestController {
    
    @Autowired
    private CdcRepository cdcRepository;
   
    @RequestMapping(value = "/CdcRestList",		method = RequestMethod.GET,
		produces = { MimeTypeUtils.APPLICATION_JSON_VALUE },
		headers = "Accept=application/json"
	)
    public @ResponseBody  List<Cdc> cdcRestList(){
        
        List<Cdc> lista = cdcRepository.findAll();
         String ret="[";
        for (Cdc item : lista)
        {
            ret += "{'text:'" + item.getText() + ", 'nodes' : " +item.getNodes() + "}";
        }
        ret+= "]";
        return lista;
    }
    
}

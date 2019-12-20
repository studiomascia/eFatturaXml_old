package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import  it.studiomascia.gestionale.models.Ddt;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.DBFileRepository;
import it.studiomascia.gestionale.repository.DdtRepository;
import it.studiomascia.gestionale.service.AnagraficaSocietaService;
import it.studiomascia.gestionale.service.DBFileStorageService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProviderController {
    
    private static final Logger logger = LoggerFactory.getLogger(PagamentoController.class);
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");
    
    @Autowired
    private AnagraficaSocietaService  providerService;

    @Autowired
    private AnagraficaSocietaRepository  providerRepository;

    @Autowired
    private DBFileStorageService DBFileStorageService;

    @Autowired
    private DBFileRepository dbFileRepository;
    
    @Autowired
    private DdtRepository ddtRepository;
     
    @GetMapping("/ProvidersList")
    public String providersList(HttpServletRequest request,Model model){
        model.addAttribute("headers", providerService.getHeaders());
        model.addAttribute("rows", providerService.getRows());

        return "providers_lista";
    }
   
    @GetMapping("/Provider/{id}/DDT")
    public String DddtListByProviderId(Model model, @PathVariable String id){

        Integer idProvider = Integer.valueOf(id);
        AnagraficaSocieta provider = providerRepository.findById(idProvider).get();
        
        Ddt x = new Ddt();
        
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("Creatore");
        headers.add("Numero");
        headers.add("Data");
        headers.add("Importo");
        headers.add("Note");
        headers.add("Verificato");

        Set<Ddt> lista= provider.getListaDDT();
        
        model.addAttribute("headerProvider", providerService.getHeaders());
        model.addAttribute("provider", provider);
        model.addAttribute("headers", headers);
        model.addAttribute("listaddt", lista);       

        return"lista_ddt_fornitore";
        
        
    }
   
}
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import  it.studiomascia.gestionale.models.Ddt;
import it.studiomascia.gestionale.models.ODA;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.DBFileRepository;
import it.studiomascia.gestionale.repository.DdtRepository;
import it.studiomascia.gestionale.repository.OdaRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    @Autowired
    private AnagraficaSocietaRepository anagraficaSocietaRepository;
     
    @Autowired
    private OdaRepository odaRepository;
     
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
    
    
    @GetMapping("/Provider/{idProvider}/ModalDdt")
    public String ModalAddDdtProvider (ModelMap model,@PathVariable Integer idProvider){
        
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        Ddt ddt = new Ddt();
        model.addAttribute("ddt",ddt);  
        model.addAttribute("provider",provider);  
        return "modalContents :: ddtProvider";
    }
 
    
    @PostMapping("/Provider/{idProvider}/ModalDdt")
    public String registraNuovoPagamentoFatturaIn( @ModelAttribute("ddt") Ddt ddt, Model model,@PathVariable Integer idProvider, RedirectAttributes redirectAttributes)
    {
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        ddt.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        ddt.setAnagraficaSocieta(provider);
        
        ddtRepository.save(ddt);

        //provider.getListaDDT().add(ddt);
        
        //anagraficaSocietaRepository.save(provider);
        return "redirect:/Provider/"+ idProvider +"/DDT";
    }
    
 @GetMapping("/Provider/{id}/ODA")
    public String OdatListByProviderId(Model model, @PathVariable String id){

        Integer idProvider = Integer.valueOf(id);
        AnagraficaSocieta provider = providerRepository.findById(idProvider).get();
        
        ODA x = new ODA();
        
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("Creatore");
        headers.add("Numero");
        headers.add("Data");
        headers.add("Importo");
        headers.add("Note");
        headers.add("Verificato");

        Set<ODA> lista= provider.getListaODA();
        
        model.addAttribute("headerProvider", providerService.getHeaders());
        model.addAttribute("provider", provider);
        model.addAttribute("headers", headers);
        model.addAttribute("listaoda", lista);       

        return"lista_oda_fornitore";
        
        
    }
    
    
    @GetMapping("/Provider/{idProvider}/ModalODA")
    public String ModalAddOdaProvider (ModelMap model,@PathVariable Integer idProvider){
        
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        ODA x = new ODA();
        model.addAttribute("oda",x);  
        model.addAttribute("provider",provider);  
        return "modalContents :: odaProvider";
    }
 
    
    @PostMapping("/Provider/{idProvider}/ModalODA")
    public String registraNuovoODA( @ModelAttribute("oda") ODA oda, Model model,@PathVariable Integer idProvider, RedirectAttributes redirectAttributes)
    {
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        oda.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        oda.setAnagraficaSocieta(provider);
        
        odaRepository.save(oda);

        //provider.getListaDDT().add(ddt);
        
        //anagraficaSocietaRepository.save(provider);
        return "redirect:/Provider/"+ idProvider +"/ODA";
    }
   
}
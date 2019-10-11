/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.Utility;
import it.studiomascia.gestionale.models.CentroDiCosto;
import it.studiomascia.gestionale.models.ControlloFattura;
import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.models.XmlFatturaBasePredicate;
import it.studiomascia.gestionale.repository.CentroDiCostoRepository;
import it.studiomascia.gestionale.repository.ControlloFatturaRepository;
import it.studiomascia.gestionale.repository.PagamentoRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.DBFileStorageService;
import it.studiomascia.gestionale.xml.AnagraficaType;
import it.studiomascia.gestionale.xml.DettaglioPagamentoType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.bouncycastle.cms.CMSSignedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Admin
 */
@Controller
public class InvoiceController {
     
    @Value("${max.rows.table}")
    private int MAX_ROWS_TABLE;
    
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @Autowired
    private ControlloFatturaRepository controlloFatturaRepository;
    
    @Autowired
    private DBFileStorageService DBFileStorageService;
    
    @Autowired
    private CentroDiCostoRepository centroDiCostoRepository;
    
    /* INIZIO Metodi comuni per tutti i mapping */
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");
    
    public byte[] getData( byte[] p7bytes) throws Exception { 
        ByteArrayOutputStream out = new ByteArrayOutputStream();                 
        try{
            CMSSignedData cms = new CMSSignedData(p7bytes);           
            if(cms.getSignedContent() == null) { 
                //Error!!! 
                return null; 
            } 
            cms.getSignedContent().write(out);           
        }catch (Exception ex){
            p7bytes = org.bouncycastle.util.encoders.Base64.decode(p7bytes);
            try{
                CMSSignedData cms = new CMSSignedData(p7bytes);           
                if(cms.getSignedContent() == null) { 
                    //Error!!! 
                    return null; 
                } 
                cms.getSignedContent().write(out);           
            }catch (Exception e){}
        }

        return out.toByteArray(); 
    } 
    
    
    /* METODO CHE PERMETTE DI VISUALIZZARE LA FATTURA SIA IN CHE OUT */
    @GetMapping("/Invoice/Download/{fileId}")
    public ModelAndView downloadFattura(HttpServletRequest request,
        HttpServletResponse response, @PathVariable String fileId) throws IOException {
        
        // Load file from database
        Integer id = Integer.valueOf(fileId); 
        XmlFatturaBase item = xmlFatturaBaseRepository.findById(id).get();
    
       byte[] byteArr = item.getXmlData().getBytes("UTF-8");
       Source source = new StreamSource(new ByteArrayInputStream(byteArr) );
 
        // adds the XML source file to the model so the XsltView can detect
        ModelAndView model = new ModelAndView("XSLTView");
        model.addObject("xmlSource", source);
         
        return model;
    }
        
      
    @GetMapping("/downloadFatturaPdf/{fileId}")
    public ResponseEntity<Resource> downloadFatturaPdf(@PathVariable String fileId) {
         try{
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            //Setup a buffer to obtain the content length
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource("c:\\foglio.xsl"));
            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            //Setup input
            Source src = new StreamSource(new File("c:\\foglio.xml"));

            //Start the transformation and rendering process
            transformer.transform(src, res);

//            Prepare response
//            response.setContentType("application/pdf");
//            response.setContentLength(out.size()); 
//
//            //Send content to Browser
//            response.getOutputStream().write(out.toByteArray());
//            response.getOutputStream().flush();
   
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.pdf\"")
                .body(new ByteArrayResource(out.toByteArray()));
    
                    
                    
                    
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    
        
        
        
        
        
        
        
        
        
    }

    /* METODI PER LE FATTURE IN */
         
    @GetMapping("/InvoicesIn")
    public String FatturePassiveList(HttpServletRequest request,Model model){
        
        //INIZIO:: BLOCCO PER LA PAGINAZIONE
        int page = 0; //default page number is 0 (yes it is weird)
        int size = MAX_ROWS_TABLE;  
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        //FINE:: BLOCCO PER LA PAGINAZIONE
        List<XmlFatturaBase> listaFatture = XmlFatturaBasePredicate.filterXmlFatturaBase(xmlFatturaBaseRepository.findAll(), XmlFatturaBasePredicate.isPassiva());
     
        // Prepara la Map da aggiungere alla view 
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("Data Reg.");
        headers.add("N.Reg.");
        headers.add("Data");
        headers.add("Numero");
        headers.add("P.IVA");
        headers.add("Denominazione");
        headers.add("Imponibile");     
//        headers.add("Saldata");     
 
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        int conta=1;
        String  strData = null;
        byte[] byteArr;
        try {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
            // Unmarshaller serve per convertire il file in un oggetto
            Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
            // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
       
            for (XmlFatturaBase xmlFattura:listaFatture) {
                //System.out.println("conta= " + conta++);
                byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                strData = "";
                sw = new StringWriter();
            
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);
                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione="";
                if (item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione()!=null)
                {
                    denominazione=item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                }else{
                    AnagraficaType  anagrafica = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica();
                    if (anagrafica.getTitolo()!=null)  denominazione+= anagrafica.getTitolo() +" ";
                    if (anagrafica.getNome()!=null)  denominazione+= anagrafica.getNome() +" ";
                    if (anagrafica.getCognome()!=null)  denominazione+= anagrafica.getCognome() +" ";
                }
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
             
                strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());

                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("Data Reg.",  strData);
                riga.put("N.Reg.", xmlFattura.getNumeroRegistrazione());
//                riga.put("Data",  formattaData.format(dataFattura));
                riga.put("Data",  formattaData.format(dataFattura));
                riga.put("Numero", numeroFattura);
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Imponibile", importoFattura);
                riga.put("Saldata", xmlFattura.isSaldata());
                riga.put("Controllata", xmlFattura.isControllata());
                righe.add(riga); 
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 

       
        model.addAttribute("headers", headers);
        model.addAttribute("rows", righe);
        model.addAttribute("currentPage", page);
//        model.addAttribute("messaggio", "Ci sono: " + righe.size() +" ");
        return "fatture_passive_lista";
    }
   
    @GetMapping("/InvoicesIn/New")
    public String nuovaFatturaIn() {
        return "fatture_passive_new";
        
    }
    
    @PostMapping("/InvoicesIn/New")
    public String nuovaFatturaIn(@RequestParam("files") MultipartFile[] files, ModelMap modelMap) {            
        List<FatturaElettronicaType> lista = new ArrayList<FatturaElettronicaType>();
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("P.IVA");
        headers.add("Data");
        headers.add("Denominazione");
        headers.add("Numero"); 
        headers.add("Imponibile");     
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        
        for (int k=0;k<files.length;k++){
            try {
                System.out.println("Carico files[k].getOriginalFilename()= " + files[k].getOriginalFilename());
                byte[] byteArr = files[k].getBytes();
                if (files[k].getOriginalFilename().endsWith("p7m")) {
                    byteArr=getData(files[k].getBytes());
                }
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
        
                XmlFatturaBase xmlFattura = new XmlFatturaBase();
                //xmlFattura.setDataRegistrazione(dataFattura);
                //xmlFattura.setNumeroRegistrazione(numeroFattura);
                xmlFattura.setDataInserimento(new Date());
                xmlFattura.setFileName(files[k].getOriginalFilename());
                xmlFattura.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
                xmlFattura.setXmlData(sw.toString());
                if(item.getFatturaElettronicaBody().get(0).getDatiPagamento().size()>0){
                List<DettaglioPagamentoType>  dettaglioPagamento =  item.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento();
                if (Utility.CheckInvoicePayed(dettaglioPagamento)) {
                    Set<Pagamento> setp = new HashSet<Pagamento>();

                    Pagamento p = new Pagamento();
                    p.setDataVersamento(dataFattura);
                    p.setImportoVersamento(item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().intValue());
                    p.setNote("pagamento impostato automaticamente");
                    p.setCreatore("System");
                    p.setSaldata(true);
                    setp.add(p);
                    xmlFattura.setPagamenti(setp);
                }
            }
                xmlFattura = xmlFatturaBaseRepository.save(xmlFattura);
                xmlFatturaBaseRepository.flush();
                System.out.println("OK");
              
                
                // Perpara la Map da aggiungere alla view 
                Map<String, Object> riga = new HashMap<String, Object>(4);
                riga.put("Id", xmlFattura.getId());   
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Data",  LocalDateTime.ofInstant(dataFattura.toInstant(), ZoneId.systemDefault()).toLocalDate());
                riga.put("Numero", numeroFattura);
                riga.put("Imponibile", importoFattura);
                righe.add(riga);
                
            } catch (JAXBException e) {
                System.out.println("ERRORE 1) CARICAMENTO FILE");
                //e.printStackTrace();
            } catch (IOException e) {
                System.out.println("ERRORE 2) CARICAMENTO FILE");
                //e.printStackTrace();
            } catch (Exception e) {
                System.out.println("ERRORE 3) CARICAMENTO FILE");
                //e.printStackTrace();
            }
            modelMap.addAttribute("headers", headers);
            modelMap.addAttribute("rows", righe);
        } //end for
        return "fatture_passive_caricate";
    }
   
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
    
    @GetMapping("/InvoiceIn/ModalRegister/{id}")
    public String ModalEditFatturaIn(ModelMap model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get(); 
        model.addAttribute("fattura",x);  
        return "modalContents :: registerInvoice";
    }
    
    @GetMapping("/InvoiceIn/Register/{id}")
    public String EditFatturaIn(Model model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        model.addAttribute("fattura",x);  
        model.addAttribute("dataReg",x.getDataRegistrazione());  
        return "fatture_passive_registra";
    }
     
    @PostMapping("/InvoiceIn/Register/{id}")
    public String aggiornaFatturaIn(@Valid @ModelAttribute("fattura") XmlFatturaBase updateFattura, BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) { return "fatture_passive_registra"; }
        XmlFatturaBase vecchiaFattura = xmlFatturaBaseRepository.findById(updateFattura.getId()).get();
        
        vecchiaFattura.setNumeroRegistrazione(updateFattura.getNumeroRegistrazione());
        vecchiaFattura.setDataRegistrazione(updateFattura.getDataRegistrazione());
        redirectAttributes.addFlashAttribute("messaggio","La fattura: è stata registrata");  
        xmlFatturaBaseRepository.save(vecchiaFattura);
        //return "redirect:/InvoiceIn/Register/"+updateFattura.getId();
        return "redirect:/InvoicesIn";
    }
   
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
    
    @GetMapping("/Invoice/{id}/ModalPayment")
    public String ModalAddPaymentFatturaIn(ModelMap model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        Pagamento p = new Pagamento();
        model.addAttribute("fattura",x);  
        model.addAttribute("pagamento",p);  
        return "modalContents :: paymentInvoice";
    }
    
    @PostMapping("/Invoice/{id}/ModalPayment")
    public String registraNuovoPagamentoFatturaIn( @ModelAttribute("pagamento") Pagamento pagamento, Model model,@PathVariable Integer id, RedirectAttributes redirectAttributes)
    {
        XmlFatturaBase vecchiaFattura = xmlFatturaBaseRepository.findById(id).get();
        pagamento.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        vecchiaFattura.getPagamenti().add(pagamento);
        
        xmlFatturaBaseRepository.save(vecchiaFattura);
//        redirectAttributes.addFlashAttribute("messaggio","Pagamento inserito");  
        return "redirect:/InvoiceIn/"+ id +"/Payments";
    }
    
    @GetMapping("/Invoice/{IdFattura}/Payment/{id}/ModalAttachment")
    public String ModalAddAttachmentToPaymentFatturaIn(ModelMap model,@PathVariable Integer IdFattura,@PathVariable Integer id){
//        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        Pagamento p = pagamentoRepository.findById(id).get();
        model.addAttribute("IdFattura",IdFattura);  
        model.addAttribute("pagamento",p);  
        return "modalContents :: attachmentPaymentInvoice";
    }
    
    @PostMapping("/Payment/{id}/ModalAttachment")
    public String postModalAddAttachmentToPaymentFatturaIn( @ModelAttribute("pagamento") Pagamento pagamento,@PathVariable Integer id, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, HttpServletRequest request)
    {
        if (request.getParameterMap().get("txtDescription") != null && request.getParameterMap().get("txtDescription").length > 0) {
            
            Pagamento p = pagamentoRepository.findById(id).get();
            for (int k=0;k<files.length;k++)
            {
               
                DBFile dbFile = DBFileStorageService.storeFile(files[k],request.getParameter("txtDescription").toString());
                 p.getFilesPagamenti().add(dbFile);
                 pagamentoRepository.save(p);
            }
        }
        return "redirect:/InvoiceIn/"+ request.getParameter("IdFattura").toString() +"/Payments";
    }
    
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
   
    @GetMapping("/InvoiceIn/{id}/ModalCheck")
    public String ModalAddCheckFatturaIn(ModelMap model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        ControlloFattura p = new ControlloFattura();
        List<CentroDiCosto> l =  centroDiCostoRepository.findAll();
        model.addAttribute("fattura",x);  
        model.addAttribute("controllo",p);  
        model.addAttribute("listaCdc",l);  
        return "modalContents :: checkInvoice";
    }
    
    @PostMapping("/InvoiceIn/{id}/ModalCheck")
    public String registraNuovoCheckFatturaIn( @ModelAttribute("controllo") ControlloFattura controllo,@PathVariable Integer id, HttpServletRequest request)
    {
        XmlFatturaBase vecchiaFattura = xmlFatturaBaseRepository.findById(id).get();
        controllo.setDataControllo(new Date());
        controllo.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        if (request.getParameter("ddlCentroDiCosto") != null) {
            int id2 = Integer.parseInt(request.getParameter("ddlCentroDiCosto"));
            controllo.setCentroDiCosto(centroDiCostoRepository.findById(id2).get().getText());
        }

        vecchiaFattura.getControlli().add(controllo);
        
        xmlFatturaBaseRepository.save(vecchiaFattura);
//        redirectAttributes.addFlashAttribute("messaggio","Pagamento inserito");  
        return "redirect:/InvoiceIn/"+ id +"/Checks";
    }
    
    @GetMapping("/InvoiceIn/{IdFattura}/Check/{id}/ModalAttachment")
    public String ModalAddAttachmentToCheckFatturaIn(ModelMap model,@PathVariable Integer IdFattura,@PathVariable Integer id){
//        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        ControlloFattura p = controlloFatturaRepository.findById(id).get();
        model.addAttribute("IdFattura",IdFattura);  
        model.addAttribute("controllo",p);  
        return "modalContents :: attachmentCheckInvoice";
    }
    
    @PostMapping("/Check/{id}/ModalAttachment")
    public String postModalAddAttachmentToCheckFatturaIn( @ModelAttribute("controllo") ControlloFattura controllo,@PathVariable Integer id, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, HttpServletRequest request)
    {
        if (request.getParameterMap().get("txtDescription") != null && request.getParameterMap().get("txtDescription").length > 0) {
            
            ControlloFattura p = controlloFatturaRepository.findById(id).get();
            for (int k=0;k<files.length;k++)
            {
               
                DBFile dbFile = DBFileStorageService.storeFile(files[k],request.getParameter("txtDescription").toString());
                 p.getFilesControlloFattura().add(dbFile);
                 controlloFatturaRepository.save(p);
            }
        }
        return "redirect:/InvoiceIn/"+ request.getParameter("IdFattura").toString() +"/Checks";
    }
    
      @GetMapping("/InvoiceIn/{fatturaId}/Checks")
    public String ControlliFattura(Model model, @PathVariable String fatturaId){
        List<String> headers = new  ArrayList<>();
            headers.add("Id");
            headers.add("Registro IVA");
            headers.add("N. Fattura");
            headers.add("Data Fattura");
            headers.add("P.IVA");
            headers.add("Denominazione");
            headers.add("Imponibile");
            
            String strData="N/A";
            Integer id = Integer.valueOf(fatturaId);
            XmlFatturaBase xmlFattura = xmlFatturaBaseRepository.findById(id).get();
            try {
                byte[] byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String partitaIVA =  item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
            
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());
                riga.put("Registro IVA",xmlFattura.getNumeroRegistrazione()+ " - " +  strData);
                riga.put("N. Fattura", numeroFattura);
                riga.put("Data Fattura", formattaData.format(dataFattura));
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Imponibile", importoFattura);
             
                model.addAttribute("fattura", riga);
                model.addAttribute("headers", headers);
 
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }     
            
        Set<ControlloFattura> listacontrolli= xmlFattura.getControlli();
        
        List<DBFile> listaFile = new ArrayList<DBFile>();
        for (ControlloFattura x : listacontrolli){

            listaFile.addAll(x.getFilesControlloFattura());
        	
        }
        
        List lista = new ArrayList(listacontrolli);
        model.addAttribute("listafiles", listaFile);
        model.addAttribute("listacontrolli", lista);
        return "lista_controlli_fattura";
    }
    
   
    
    
    
    
    /* METODI PER LE FATTURE OUT */
    
    @GetMapping("/InvoicesOut")
    public String FattureAttiveList(HttpServletRequest request,Model model){
        
        //INIZIO:: BLOCCO PER LA PAGINAZIONE
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 100; //default page size is 10
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        //FINE:: BLOCCO PER LA PAGINAZIONE
        List<XmlFatturaBase> listaFatture = XmlFatturaBasePredicate.filterXmlFatturaBase(xmlFatturaBaseRepository.findAll(), XmlFatturaBasePredicate.isAttiva());
        


        // Prepara la Map da aggiungere alla view 
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("P.IVA");
        headers.add("Data");
        headers.add("Denominazione");
        headers.add("Numero");
        headers.add("Imponibile");     
       
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        
        for (XmlFatturaBase xmlFattura:listaFatture) {
        try {
                byte[] byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String partitaIVA =  item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice();
                String denominazione = item.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().getDenominazione();
            
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Data",  LocalDateTime.ofInstant(xmlFattura.getDataRegistrazione().toInstant(), ZoneId.systemDefault()).toLocalDate());
                riga.put("Numero", numeroFattura);
                riga.put("Imponibile", importoFattura);
                righe.add(riga);
                                
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
       
       
       model.addAttribute("headers", headers);
        model.addAttribute("rows", righe);
        model.addAttribute("currentPage", page);
        model.addAttribute("messaggio", "Ci sono: " + righe.size() +" ");
    return "fatture_attive_lista";
      
    
    }
    
    @GetMapping("/InvoicesOut/New")
    public String nuovaFatturaOut() {
        return "fatture_attive_new";
    }
    
    @PostMapping("/InvoicesOut/New")
    public String nuovaFatturaOut(@RequestParam("files") MultipartFile[] files, ModelMap modelMap) {            
        List<FatturaElettronicaType> lista = new ArrayList<FatturaElettronicaType>();
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("P.IVA");
        headers.add("Data");
        headers.add("Denominazione");
        headers.add("Numero");
        headers.add("Imponibile");     
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        
        for (int k=0;k<files.length;k++){
            try {
                byte[] byteArr = files[k].getBytes();
                if (files[k].getOriginalFilename().endsWith("p7m")) {
                    byteArr=getData(files[k].getBytes());
                }
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
        
                XmlFatturaBase xmlFattura = new XmlFatturaBase(true);
                xmlFattura.setDataRegistrazione(dataFattura);
                xmlFattura.setNumeroRegistrazione(numeroFattura);
                xmlFattura.setDataInserimento(new Date());
                xmlFattura.setFileName(files[k].getOriginalFilename());
                xmlFattura.setXmlData(sw.toString());
                xmlFattura.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
                xmlFattura = xmlFatturaBaseRepository.save(xmlFattura);
                xmlFatturaBaseRepository.flush();
                
                // Perpara la Map da aggiungere alla view 
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Data",  LocalDateTime.ofInstant(dataFattura.toInstant(), ZoneId.systemDefault()).toLocalDate());
                riga.put("Numero", numeroFattura);
                riga.put("Imponibile", importoFattura);
                righe.add(riga);
                
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelMap.addAttribute("headers", headers);
            modelMap.addAttribute("rows", righe);
        } //end for
        return "fatture_attive_caricate";
    }

  
    
   
}

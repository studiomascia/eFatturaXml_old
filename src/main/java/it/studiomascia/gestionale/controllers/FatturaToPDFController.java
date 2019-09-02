/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;
import it.studiomascia.gestionale.models.FileDbConfig;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.FileDbConfigRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Controller
public class FatturaToPDFController {
	
    @Autowired
    XmlFatturaBaseRepository xmlFatturaBaseRepository;
    
    @Autowired
    FileDbConfigRepository fileDbConfigRepository;
     
    @GetMapping("/DownloadFatturaPDF/{fileId}")
    public String transform(@PathVariable String fileId) throws SAXException, IOException, ParserConfigurationException,
                                                              TransformerFactoryConfigurationError, TransformerException 
    {
		
        Integer id = Integer.valueOf(fileId);
        XmlFatturaBase item = xmlFatturaBaseRepository.findById(id).get();
    
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xmlDocument = null;
       
        FileDbConfig xslDbFile = fileDbConfigRepository.findById("98d9409b-8689-412b-9d0f-fa98480da838").get();
        xslDbFile.getData();

//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer()        
                
                
                
                try {
             
        
            
            URL xsltURL = getClass().getClassLoader().getResource("c:\foglio.xsl");
            Source stylesource = new StreamSource(xsltURL.openStream(), xsltURL.toExternalForm());
            Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);


        
        
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));
            // write to file
            File file = new File("c:/test.html");
        if (!file.exists()) {
                file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(stringWriter.toString());
        bw.close();
	
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "template_vuoto";
    }
       
}
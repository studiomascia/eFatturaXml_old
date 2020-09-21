/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.UserRepository;
import it.studiomascia.gestionale.service.XmlFatturaBaseService;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 /**
 *
 * @author luigi
 */
@Controller
public class ReportController
{
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");

    
    @Autowired
    UserRepository userRepository;
    
    private DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALY));
        
    @Autowired
    private XmlFatturaBaseService xmlFatturaBaseService;
    
    @Autowired
    private AnagraficaSocietaRepository  providerRepository;
    
    // Genera un foglio excel a partire dalla "tabella" utilizzando come le colonne nella lista "header"
    public ByteArrayInputStream GenerateExcelReport( List<String> header, List<Map<String,Object>> tabella,String reportName) throws IOException {

        Workbook workbook = new HSSFWorkbook(); 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet(reportName);
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle;
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
        int rowIdx = 1;
            
        // Header
        for (int col = 0; col < header.size(); col++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
            cell.setCellValue(header.get(col));
            cell.setCellStyle(headerCellStyle);
        }   
        
        String strTemp="";
        // Ciclo che scorre le righe delle fatture 
        for (Map<String,Object> itemTabella : tabella) 
        {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            //System.out.println("riga = " + rowIdx);
            
            for (int col = 0; col < header.size(); col++) {
                strTemp = header.get(col);

                if (itemTabella.get(strTemp) == null)
                    strTemp="";
                else
                    strTemp= itemTabella.get(strTemp) .toString();
                    
                row.createCell(col).setCellValue(strTemp);
                
            }
            // Di ogni fattura si prendono solo i campi di interesse 
        }
          
        // CellStyle for Age
        CellStyle ageCellStyle = workbook.createCellStyle();
        ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

        workbook.write(out);

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream GenerateExcelReportCompleto(Boolean soloDaSaldare) throws IOException {
        
        
        // Inizializzo Foglio Excel
        Workbook workbook = new HSSFWorkbook(); 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("ReportCompleto");
        
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle;
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        org.apache.poi.ss.usermodel.Font paymentFont = workbook.createFont();
        paymentFont.setColor(IndexedColors.RED.getIndex());

        CellStyle paymentCellStyle;
        paymentCellStyle = workbook.createCellStyle();
        paymentCellStyle.setFont(paymentFont);

        // Row for Header
        int indiceRiga = 0;

        FatturaElettronicaType fatturaElettronica = new FatturaElettronicaType();
         
        // Ciclo su Lista di tutti i Fornitori
        List<AnagraficaSocieta> listaFornitori = providerRepository.findAll();
        // Elenco delle fatture per ogni fornitore
        for (AnagraficaSocieta fornitore : listaFornitori) 
        {
            org.apache.poi.ss.usermodel.Row headerFornitore = sheet.createRow(indiceRiga++);
            
            //org.apache.poi.ss.usermodel.Cell cell = headerFornitore.createCell(0);
            headerFornitore.createCell(0).setCellValue(fornitore.getDenominazione() + " - " +fornitore.getPiva());
            headerFornitore.getCell(0).setCellStyle(headerCellStyle);
//            headerFornitore.createCell(1).setCellValue(fornitore.getPiva());
//            headerFornitore.getCell(1).setCellStyle(headerCellStyle);
            
            Set<XmlFatturaBase> listaFatture = fornitore.getListaXmlFatturaBase();
            int indiceCella=1;
            if (listaFatture.size()>0)
            {
                org.apache.poi.ss.usermodel.Row header2 = sheet.createRow(indiceRiga++);
                header2.setRowStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("Numero");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("Data");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("Importo");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("Stato/Note Pagamento");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("WBS");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("Controllo");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
                header2.createCell(indiceCella).setCellValue("Note Controllo");
                header2.getCell(indiceCella++).setCellStyle(headerCellStyle);
            }
            
            for (XmlFatturaBase fattura : listaFatture) 
            {
                indiceCella=1;
                fatturaElettronica = fattura.getFatturaElettronica();
                if  (soloDaSaldare && (fattura.isSaldata() || fattura.isSaldataAuto()))
                {
                    
                }else {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(indiceRiga++);
                    row.setRowStyle(headerCellStyle);
                    row.createCell(indiceCella++).setCellValue(fatturaElettronica.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero());
                    row.createCell(indiceCella++).setCellValue(formattaData.format(fatturaElettronica.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime()));

                    if (fatturaElettronica.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento() !=null)
                    {
                        row.createCell(indiceCella++).setCellValue( df.format(fatturaElettronica.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento()));
                    }

                    if (fattura.isSaldata()){
                        row.createCell(indiceCella++).setCellValue("SALDATA");
                         for (Pagamento itemPagamento :  fattura.getPagamenti())
                         {
                            int indiceCella2=2;
                            org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(indiceRiga++);
                            row2.createCell(indiceCella2).setCellValue( formattaData.format(itemPagamento.getDataVersamento()));
                            row2.getCell(indiceCella2++).setCellStyle(paymentCellStyle);
                            row2.createCell(indiceCella2).setCellValue( df.format(itemPagamento.getImportoVersamento()));
                            row2.getCell(indiceCella2++).setCellStyle(paymentCellStyle);
                            row2.createCell(indiceCella2).setCellValue( itemPagamento.getNote());
                            row2.getCell(indiceCella2++).setCellStyle(paymentCellStyle);
                         }
                    }else if (fattura.isSaldataParz()){
                        row.createCell(indiceCella++).setCellValue("PARZIALMENTE SALDATA");
                        for (Pagamento itemPagamento :  fattura.getPagamenti())
                        {
                            int indiceCella2=2;
                            org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(indiceRiga++);
                            row2.createCell(indiceCella2).setCellValue( formattaData.format(itemPagamento.getDataVersamento()));
                            row2.getCell(indiceCella2++).setCellStyle(paymentCellStyle);
                            row2.createCell(indiceCella2).setCellValue(df.format( itemPagamento.getImportoVersamento()));
                            row2.getCell(indiceCella2++).setCellStyle(paymentCellStyle);
                            row2.createCell(indiceCella2).setCellValue( itemPagamento.getNote());
                            row2.getCell(indiceCella2++).setCellStyle(paymentCellStyle);
                        }
                    } 
                    else if (fattura.isSaldataAuto()){
                        row.createCell(indiceCella++).setCellValue("AUTOMATICO");
                    }   
                    else {
                        row.createCell(indiceCella++).setCellValue("NESSUN PAGAMENTO");
                    }   

                    if (fattura.isControllataOK()) 
                    {
                        row.createCell(indiceCella++).setCellValue(fattura.getUltimoControllo().getCentroDiCosto().getText());
                        row.createCell(indiceCella++).setCellValue(fattura.getUltimoControllo().getCreatore());
                        row.createCell(indiceCella++).setCellValue(fattura.getUltimoControllo().getNote());
                    }   
                }
            }
        }



         
            
            
            
    
        
        
        
        
        
        
        // Chiusura foglio excel 
        // CellStyle for Age
        CellStyle ageCellStyle = workbook.createCellStyle();
        ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

        workbook.write(out);

        return new ByteArrayInputStream(out.toByteArray());
    }

    
    @RequestMapping(value = "/ReportTest", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelReport() throws IOException {
        List<User> listaUtenti  = userRepository.findAll();

     String[] COLUMNs = { "Id", "Name", "Desc" };

        try (
            Workbook workbook = new HSSFWorkbook(); 
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ) 
        {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("StateInfo");

            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (User state : listaUtenti) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(state.getId());
                row.createCell(1).setCellValue(state.getUsername());
                row.createCell(2).setCellValue(state.getShortname());
            }

             org.apache.poi.ss.usermodel.Row rowTest = sheet.createRow(rowIdx++);

                rowTest.createCell(0).setCellValue("test_1");
                rowTest.createCell(1).setCellValue("test_2");
                rowTest.createCell(2).setCellValue("test_3");
            
            
            workbook.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
        }

    // return IOUtils.toByteArray(in);

    
}
      
    @RequestMapping(value = "/ReportInvoicesIn", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelReportInvoices() throws IOException {
       
        List<String> COLUMNs = xmlFatturaBaseService.getHeaders();
        List<Map<String,Object>> listaFatture  = xmlFatturaBaseService.getRows();

        HttpHeaders headers = new HttpHeaders();
        // set filename in header
        
        headers.add("Content-Disposition", "attachment; filename=download.xls");
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(GenerateExcelReport(COLUMNs,listaFatture,"listaFatture" )));    
    }
 
    @RequestMapping(value = "/ReportCompleto", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelReportCompleto(@RequestParam("soloDaSaldare") Boolean soloDaSaldare) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        // set filename in header
        if (soloDaSaldare == null) soloDaSaldare=false;
        
        headers.add("Content-Disposition", "attachment; filename=download.xls");
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(GenerateExcelReportCompleto(soloDaSaldare)));  
    }
}
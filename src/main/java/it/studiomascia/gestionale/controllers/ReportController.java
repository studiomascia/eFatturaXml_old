/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import com.sun.rowset.internal.Row;
import it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.UserRepository;
import it.studiomascia.gestionale.service.UtenteDetails;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.Thread.State;
import java.util.List;
import javafx.scene.control.Cell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 /**
 *
 * @author luigi
 */
@Controller
public class ReportController
{
    @Autowired
    UserRepository userRepository;
    
    @RequestMapping(value = "/Report", method = RequestMethod.GET)
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
}
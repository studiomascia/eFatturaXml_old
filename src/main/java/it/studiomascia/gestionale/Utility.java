/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale;

import it.studiomascia.gestionale.xml.DettaglioPagamentoType;
import it.studiomascia.gestionale.xml.ModalitaPagamentoType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author luigi
 */
public class Utility {
    
    public static void main(String[] args) 
    {
    }
    
    public static boolean CheckInvoicePayed (List<DettaglioPagamentoType> lista)
    {
        boolean ret= false;
         for (DettaglioPagamentoType x:lista)
         { 
            ModalitaPagamentoType y = x.getModalitaPagamento();
            if ((y!=null) && (  ( y == ModalitaPagamentoType.MP_01) || 
                                ( y == ModalitaPagamentoType.MP_03) || 
                                ( y == ModalitaPagamentoType.MP_04) || 
                                ( y == ModalitaPagamentoType.MP_08) || 
                                ( y == ModalitaPagamentoType.MP_09) || 
                                ( y == ModalitaPagamentoType.MP_10) || 
                                ( y == ModalitaPagamentoType.MP_11) || 
                                ( y == ModalitaPagamentoType.MP_12) || 
                                ( y == ModalitaPagamentoType.MP_16) || 
                                ( y == ModalitaPagamentoType.MP_17 ) 
            ))  ret = true;
         }
        return ret;
    }
    
    public static String ConvertBigDecimaltoString(BigDecimal numero){
        
        String ret="";
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALY);
        numberFormat.setMinimumFractionDigits(2);
        try{
            ret = numberFormat.format(numero);
        }catch (Exception e){
        }
        return ret;
    }
}

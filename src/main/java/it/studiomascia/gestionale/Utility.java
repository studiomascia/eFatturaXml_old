/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale;

import it.studiomascia.gestionale.xml.DettaglioPagamentoType;
import it.studiomascia.gestionale.xml.ModalitaPagamentoType;
import java.util.EnumSet;

/**
 *
 * @author luigi
 */
public class Utility {
    
    public static void main(String[] args) 
    {
    }
    
    public static boolean CheckInoicePayed (DettaglioPagamentoType x)
    {
        boolean ret= false;
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
        return ret;
    }
}

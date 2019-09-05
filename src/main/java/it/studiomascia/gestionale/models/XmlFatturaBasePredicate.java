/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

/**
 *
 * @author luigi
 */

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
 
public class XmlFatturaBasePredicate
{
    public static Predicate<XmlFatturaBase> isAttiva() {
        return p -> p.isAttiva();
    }
     
    public static Predicate<XmlFatturaBase> isPassiva() {
        return p -> !p.isAttiva();
    }
      
    
    public static List<XmlFatturaBase> filterXmlFatturaBase (List<XmlFatturaBase> fatture,
                                                Predicate<XmlFatturaBase> predicate)
    {
        return fatture.stream()
                    .filter( predicate )
                    .collect(Collectors.<XmlFatturaBase>toList());
    }
}  
//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.09.27 alle 01:21:58 AM CEST 
//


package it.studiomascia.gestionale.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per TipoCessionePrestazioneType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoCessionePrestazioneType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;length value="2"/&gt;
 *     &lt;enumeration value="SC"/&gt;
 *     &lt;enumeration value="PR"/&gt;
 *     &lt;enumeration value="AB"/&gt;
 *     &lt;enumeration value="AC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TipoCessionePrestazioneType")
@XmlEnum
public enum TipoCessionePrestazioneType {


    /**
     * Sconto
     * 
     */
    SC,

    /**
     * Premio
     * 
     */
    PR,

    /**
     * Abbuono
     * 
     */
    AB,

    /**
     * Spesa accessoria
     * 
     */
    AC;

    public String value() {
        return name();
    }

    public static TipoCessionePrestazioneType fromValue(String v) {
        return valueOf(v);
    }

}

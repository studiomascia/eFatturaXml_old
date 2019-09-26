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
 * <p>Classe Java per EsigibilitaIVAType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="EsigibilitaIVAType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;minLength value="1"/&gt;
 *     &lt;maxLength value="1"/&gt;
 *     &lt;enumeration value="D"/&gt;
 *     &lt;enumeration value="I"/&gt;
 *     &lt;enumeration value="S"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EsigibilitaIVAType")
@XmlEnum
public enum EsigibilitaIVAType {


    /**
     * esigibilità differita
     * 
     */
    D,

    /**
     * esigibilità immediata
     * 
     */
    I,

    /**
     * scissione dei pagamenti
     * 
     */
    S;

    public String value() {
        return name();
    }

    public static EsigibilitaIVAType fromValue(String v) {
        return valueOf(v);
    }

}

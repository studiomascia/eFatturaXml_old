//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.09.27 alle 01:21:58 AM CEST 
//


package it.studiomascia.gestionale.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per TipoRitenutaType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoRitenutaType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;length value="4"/&gt;
 *     &lt;enumeration value="RT01"/&gt;
 *     &lt;enumeration value="RT02"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TipoRitenutaType")
@XmlEnum
public enum TipoRitenutaType {


    /**
     * Ritenuta di acconto persone fisiche
     * 
     */
    @XmlEnumValue("RT01")
    RT_01("RT01"),

    /**
     * Ritenuta di acconto persone giuridiche
     * 
     */
    @XmlEnumValue("RT02")
    RT_02("RT02");
    private final String value;

    TipoRitenutaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoRitenutaType fromValue(String v) {
        for (TipoRitenutaType c: TipoRitenutaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

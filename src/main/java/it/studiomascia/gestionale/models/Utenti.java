/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luigi
 */
@Entity
@Table(name = "utenti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Utenti.findAll", query = "SELECT u FROM Utenti u"),
    @NamedQuery(name = "Utenti.findById", query = "SELECT u FROM Utenti u WHERE u.id = :id"),
    @NamedQuery(name = "Utenti.findByEmail", query = "SELECT u FROM Utenti u WHERE u.email = :email"),
    @NamedQuery(name = "Utenti.findByPassword", query = "SELECT u FROM Utenti u WHERE u.password = :password"),
    @NamedQuery(name = "Utenti.findByStato", query = "SELECT u FROM Utenti u WHERE u.stato = :stato"),
    @NamedQuery(name = "Utenti.findByEmailAndPassword",query ="SELECT u FROM Utenti u WHERE u.email = :email and u.password = :password" ),
    @NamedQuery(name = "Utenti.findByRuolo", query = "SELECT u FROM Utenti u WHERE u.ruolo = :ruolo")})
public class Utenti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Email
    @Column(name = "email")
    private String email;
    @Size(min=6,max = 255)
    @Column(name = "password")
    private String password;
    @Column(name = "stato")
    private Integer stato;
    @Column(name = "ruolo")
    private Integer ruolo;
    @Temporal(TemporalType.TIMESTAMP)    
    @Column(name = "ultimo_accesso")
    private Date ultimoAccesso;

    public Utenti() {
    }

    public Utenti(Integer id) {
        this.id = id;
    }

    public Utenti(Integer id, Date ultimoAccesso) {
        this.id = id;
        this.ultimoAccesso = ultimoAccesso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStato() {
        return stato;
    }

    public void setStato(Integer stato) {
        this.stato = stato;
    }

    public Integer getRuolo() {
        return ruolo;
    }

    public void setRuolo(Integer ruolo) {
        this.ruolo = ruolo;
    }

    public Date getUltimoAccesso() {
        return ultimoAccesso;
    }

    public void setUltimoAccesso(Date ultimoAccesso) {
        this.ultimoAccesso = ultimoAccesso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utenti)) {
            return false;
        }
        Utenti other = (Utenti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.studiomascia.gestionale.models.Utenti[ id=" + id + " ]";
    }
    
    public String getDescrizioneStato(){
        String desc="";
        switch (this.stato)
        {
            case 0:
                desc="Attivo";
                break;
            case 1:
                desc="Non Attivo";
                break;
        }
        return desc;
    }
    
      public String getDescrizioneRuolo(){
        String desc="";
        switch (this.ruolo )
        {
            case 0:
                desc="Utente";
                break;
            case 1:
                desc="Amministratore";
                break;
        }
        return desc;
    }
    
}

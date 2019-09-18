/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
 
/**
 *
 * @author Admin
 */
@Entity
@Table(name = "cdc")
public class Cdc {
    
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String descrizione;

    @OneToMany
    @OrderColumn
    @JoinColumn(name = "parent_id")
    private List<Cdc> children = new LinkedList<Cdc>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "parent_id",insertable=false,updatable=false)
    private Cdc parent;
    
//    public Cdc getTree(){ 
//        entityManager.createNamedQuery("findAllNodesWithTheirChildren").getResultList(); 
//        Cdc root = entityManager.find(Cdc.class, 1); 
//   return root;
//  } 
       
        /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @return the children
     */
    public List<Cdc> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<Cdc> children) {
        this.children = children;
    }

    /**
     * @return the parent
     */
    public Cdc getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Cdc parent) {
        this.parent = parent;
    }

	

}
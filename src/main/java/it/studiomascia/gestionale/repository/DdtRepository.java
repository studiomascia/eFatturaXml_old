////*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.Ddt;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author luigi
 */
public interface DdtRepository extends JpaRepository<Ddt, Integer>{
//    @EntityGraph(value = "Ddt.AnagraficaSocieta", type = EntityGraphType.FETCH)
//    public List<Ddt> findByIdAnagraficaSocieta(int id);
//    
//    @Query("select u from ddt u where u.idAnagraficaSocieta= ?1")
//  List<Ddt> findByAnagraficaSocieta(int x);
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.Utenti;
import java.util.Optional;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author luigi
 */
public interface UtentiRepository extends JpaRepository<Utenti, Integer> {
      Optional<Utenti>  findByEmailAndPassword(String email, String password);
      Optional<Utenti>  loadUserByUsername(String email);

}

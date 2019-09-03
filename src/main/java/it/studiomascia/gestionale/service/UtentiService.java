/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.Utenti;
import it.studiomascia.gestionale.repository.UtentiRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author luigi
 */
@Service
public class UtentiService implements UserDetailsService {

  @Autowired
  private UtentiRepository utentiRepository;

   @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Utenti user = utentiRepository.loadUserByUsername(username).get();

    if (user == null) {
      throw new UsernameNotFoundException("Username not found");
    }

    return new org.springframework.security.core.userdetails.User(
      username
      , user.getPassword()
      , Collections.singleton(new SimpleGrantedAuthority("user")));
  }
  
  
//  public UserDetails loadUserByUsername(String username,String password) throws UsernameNotFoundException {
//    Utenti user = utentiRepository.findByEmailAndPassword(username, password).get();
//
//    if (user == null) {
//      throw new UsernameNotFoundException("Username not found");
//    }
//
//    return new org.springframework.security.core.userdetails.User(
//      username
//      , user.getPassword()
//      , Collections.singleton(new SimpleGrantedAuthority("user")));
//  }
}
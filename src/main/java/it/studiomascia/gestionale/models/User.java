/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private int enabled;


    @ManyToMany (fetch=FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinTable(name="users_authority", 
            joinColumns = { @JoinColumn(name="users_id")}, 
            inverseJoinColumns ={@JoinColumn(name="authority_id")} )
    
    private Set<Authority> authorities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int x) {
        this.enabled = x;
    }

    /**
     * @return the authorities
     */
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.entidades;

import com.globant.SitioNoticias.Enums.ROLES;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Guido
 */

@Entity
public class Usuario {
    
    @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String nombre;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private ROLES rol;

    @OneToOne
    private Imagen imgPerfil;
    
    public Usuario () {
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public ROLES getRol() {
        return rol;
    }

    public void setRol(ROLES rol) {
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Imagen getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(Imagen imgPerfil) {
        this.imgPerfil = imgPerfil;
    }
    
    

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", email=" + email + ", password=" + password + ", rol=" + rol + '}';
    }
    
    
}

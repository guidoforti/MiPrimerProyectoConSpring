/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 *
 * @author Guido
 */
@Entity
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mime;
    private String nombre;

    // la anotacion lob, le dice a java que esto es un dato que puede ocupar muicho espacio, que puede tener muchos butes
    // basic es una anotacion que nos permite configurar como va a cargar, tenemos 2 , lazy que significa lento y que por lo tanto 
    // se va a tambien cargar unicamente cuando lo pidamos, en cambio los otros atributos ide mime nombre, estan por defecto como 
    //eager -->  opsea que se cargaran rapido y se van a cargar apenas llamamos a una imagen.
    // el contenido entonces SOLO se va a traer cuando se haga un GET sobre este atributo
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

    public Imagen() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    
    
    
}

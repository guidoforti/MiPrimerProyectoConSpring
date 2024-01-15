/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.servicios;

import com.globant.SitioNoticias.excepciones.MiExcepcion;
import com.globant.SitioNoticias.entidades.Noticia;
import com.globant.SitioNoticias.repositorios.SitioNoticiasRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticiaServicio {
    
    @Autowired
    private SitioNoticiasRepositorio SitioNoticiasRepositorio;
 
    @Transactional //ESTA ANOTACION SE PONE EN TODOS LOS METODOS QUIE GENERAN 
    // MODIIFCAIONES PERMANENTES EN LA BASE DE DATOS,  PORQUE LO QUE HACE ES DECIR
    // QUE SI EL METODO SE EJECUTA SIN EXCEPCIONES, SE REALIZA UN COMMIT A LA BD Y SE APLICAN LOS CAMBIOS, SI SALE ERROR, NO SE APLICA NADA EN LA BD POR ESA ANOTAICON
    public void crearNoticia (String titulo, String cuerpo) throws MiExcepcion {
   
        Validar(titulo, cuerpo);
        
        Noticia noticiaNueva = new Noticia ();
        noticiaNueva.setTitulo(titulo);
        noticiaNueva.setCuerpo(cuerpo);
        
        SitioNoticiasRepositorio.save(noticiaNueva);
        
    }
    
    public List<Noticia>  listarNoticia () {
    List<Noticia> listaDeNoticias = new ArrayList();
    
    listaDeNoticias = SitioNoticiasRepositorio.findAll();
    return listaDeNoticias;
        
    }
    
    @Transactional
    public void modificarNoticia (Long id , String titulo, String cuerpo) throws MiExcepcion {
        
        Validar(titulo, cuerpo);
        
        
        //ESTA CLASE OPTIONAL,nos retorna una respuesta de tipo libro, y se hace 
        // POR SI EL ID, NO SE ENCUENTRA EN LA BD  , optional es un objeto contenedor
        // que pùede o no tner un objeto no null, si el objeto no es null, returna un true
        // entonces hacemos un if que si no es nulo, osea esta presente, ahi modificamos el libro
        Optional<Noticia> respuesta = SitioNoticiasRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
        Noticia noticia = respuesta.get();
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        SitioNoticiasRepositorio.save(noticia);
        }
    }
    
    //METODO PARA HACER LAS EXCEPCIONES DE TODOS LOS METODOS qeu tengan parametros repetidos 
    private void Validar (String titulo, String cuerpo) throws MiExcepcion {
    
    if (titulo == null || titulo.trim().isEmpty()) {
        throw new MiExcepcion("El título no puede estar vacío");
    }

    if (cuerpo == null || cuerpo.trim().isEmpty()) {
        throw new MiExcepcion("El cuerpo del artículo no puede estar vacío");
    }
    }
    
    
    public Noticia getOne (Long id) {
    
        return SitioNoticiasRepositorio.getOne(id);
       }
    
  @Transactional
    public void darDeBajaNoticia (Long id) {
    
        
        SitioNoticiasRepositorio.deleteById(id);
    }
}

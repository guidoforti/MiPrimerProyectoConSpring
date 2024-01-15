/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.servicios;

import com.globant.SitioNoticias.entidades.Imagen;
import com.globant.SitioNoticias.excepciones.MiExcepcion;
import com.globant.SitioNoticias.repositorios.ImagenRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Guido
 */
@Service
public class ImagenServicio {
    
    @Autowired
   private  ImagenRepositorio repo;
    
    @Transactional
    //MULTIPARTFILE es el tipo de archivo en el cual se va a almacenar la imagen
    public Imagen guardarImagen (MultipartFile archivo)  {
        
        if (archivo != null) {
        
            try {
                Imagen img = new Imagen();
                
                // para setear los atributos, utilizo el archivo que me llega .
                // primero e ltipo de conteniudo
                img.setMime(archivo.getContentType());
                // desp nombre
                img.setNombre(archivo.getName());
                // despues la cantidad de bytes.
                img.setContenido(archivo.getBytes());
                
              return  repo.save(img);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        }
    return null;
}
    

    
    public Imagen actualizarImagen (MultipartFile archivo, Long id) throws MiExcepcion{
    
        if (archivo != null) {
        
            try {
                
                if (id != null) {
                  Imagen img = new Imagen();
                
                Optional<Imagen> respuesta = repo.findById(id);
                
                if(respuesta.isPresent()) {
                
                    img = respuesta.get();
                    img.setMime(archivo.getContentType());
                // desp nombre
                img.setNombre(archivo.getName());
                // despues la cantidad de bytes.
                img.setContenido(archivo.getBytes());
                repo.save(img);
                
                }
                
                        }
              
                
            } catch (Exception e) {
                 System.out.println(e.getMessage());
            }
        
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.Controladores;

import com.globant.SitioNoticias.entidades.Usuario;
import com.globant.SitioNoticias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Guido
 */
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/perfil/{id}")
    //esto seria como MANDARLE EL ID A RTAVES DE LA URL , USEA COMO PATHVARIABLE
    public ResponseEntity<byte[]> imagenUsuario (@PathVariable Long id) {
        Usuario usuario = usuarioServicio.buscarPorId(id);
        
        //ARREGLO DE BYTES -- osea el espacion, que es la img que cargo la persona
        byte[] imagen = usuario.getImgPerfil().getContenido();
        
        //LOS HEADES que van a indicar que tipo de contenido va  a tener est archivo , en este caso img jpge
        HttpHeaders headers = new HttpHeaders ();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        // estado http --> es el estado en el que termina el proceso estadfo 404 o 200
        
        
        //ENTONCES EN LUGAR DE RETORNAR UNA VISTA O UN HTML, OLO QUE HACEMOS ES RETORNAR LA IMAGEN DEL USUARIO
        // CON TODO LO QUE CONLLEVA, UNA RESPONSEENTITY IMAGEN HEADERS Y EL ESTADO HTTP
      return  new ResponseEntity<>(imagen, headers , HttpStatus.OK);
    }
}

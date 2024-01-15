/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.Controladores;

import com.globant.SitioNoticias.entidades.Noticia;
import com.globant.SitioNoticias.entidades.Usuario;
import com.globant.SitioNoticias.excepciones.MiExcepcionUsuario;
import com.globant.SitioNoticias.servicios.NoticiaServicio;
import com.globant.SitioNoticias.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Guido
 */

@Controller
@RequestMapping("/admin")
public class adminControlador {
    
   @Autowired
   NoticiaServicio noticiaServicio;
    @Autowired
   UsuarioServicio usuarioServicio;
    
    @GetMapping("/dashboard") 
    public String vistaAdmin (ModelMap modelo) {
        
         List<Noticia> noticias = noticiaServicio.listarNoticia();
        modelo.addAttribute("noticias", noticias);
    return "vistaInicio.html";
    }
    
    @GetMapping("/listaDeUsuarios") 
    public String listaDeUsuarios (ModelMap modelo) {
   List<Usuario> usuarios = usuarioServicio.listarUsuarios();
    modelo.addAttribute("listaUsuarios", usuarios); 
    return "listaUsuarios.html";
    }
    
    @GetMapping("/cambiarRol/{id}")
    public String modificarRol (@PathVariable Long id, ModelMap modelo) throws MiExcepcionUsuario {
    
      usuarioServicio.cambiarRol(id);
        System.out.println("CAMBIOOOOOOOOOOOOOOOOOOOO DE ROL LUEGO DEL METODO");
        System.out.println(usuarioServicio.buscarPorId(id));
       
        return "redirect:/admin/listaDeUsuarios";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.Controladores;

import com.globant.SitioNoticias.entidades.Noticia;
import com.globant.SitioNoticias.entidades.Usuario;
import com.globant.SitioNoticias.excepciones.MiExcepcion;
import com.globant.SitioNoticias.excepciones.MiExcepcionUsuario;
import com.globant.SitioNoticias.servicios.NoticiaServicio;
import com.globant.SitioNoticias.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
// este request mapping hacer que cada vez que se presione / en el url, 
 //del localhost, se llame a este controladro
@RequestMapping("/")
public class PortalControlador {
    
     @Autowired
    private NoticiaServicio noticiaServicio;
    
    @Autowired
    private UsuarioServicio uSv;
    
    //esta anotacion mapea la url cuando se agrega la barra
    @GetMapping("/") 
public String index() { 
return "vistaInicio.html";
}
// OSEA, ESTE CONTROLADOR LO QUE ESTA HACIENDO ES QUE LUEGO DE /. SE RETORNE EL INDEX

@GetMapping("/registrar") 
public String registrar() { 
return "registro.html";
}

@PostMapping("/registro") 
public String registro (@RequestParam String nombre ,@RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam MultipartFile archivo , ModelMap modelo ){
    
        try {
            uSv.crearUsuario(nombre, email, password, password, archivo);
            modelo.put("exito","el usuario fue creado exitosamente");
             return "registro.html";
        } catch (MiExcepcionUsuario ex) {
            
            modelo.put("error", ex.getMessage());
           return "registro.html";
        }
    
    
}

@GetMapping("/login") 
public String login(@RequestParam (required = false) String error , ModelMap modelo) { 
    if ( error != null) {
     modelo.put("error", "usuario o contraseña invalido");
    }
return "login.html";
}

 

//ESTA ANOTACION LO QUE HACES ES DECIR QUE SOLO SE PUEDE ACCER SI SE TIENE ALGUNO DE ESOS ROLES
@PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
 @GetMapping("/inicio")
    public String inicio (ModelMap modelo , HttpSession sesion) {
        
        List<Noticia> noticias = noticiaServicio.listarNoticia();
        modelo.addAttribute("noticias", noticias);
           
        Usuario logueado = (Usuario) sesion.getAttribute("usuarioSesion");
        
        if (logueado.getRol().toString().equals("ADMIN")) {
         
          
        modelo.addAttribute("noticias", noticias);
        return  "redirect:/admin/dashboard";}
        else { 
            modelo.addAttribute("sesion", sesion);
            return "vistaInicio.html";
        }
   
    
            }
    
    
    //ESTE GETMAPPING nos va a llevar al perfil para modificar segun los datos del usuari ocargador
 @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
 @GetMapping("/modificarUsuario")
 public  String perfil (ModelMap modelo , HttpSession sesion) {
 Usuario usuario = (Usuario) sesion.getAttribute("usuarioSesion");
 modelo.put("usuario", usuario);
 
 //HACER EL HTML
 return "modificarUsuario.html";
 
 }
 
 
 @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
 @PostMapping("/modificarUsuario/{id}")
 public String actualizarUsuario (MultipartFile archivo , @PathVariable Long id , @RequestParam String nombre ,@RequestParam String email,
         @RequestParam String password, @RequestParam String password2 , ModelMap modelo) throws MiExcepcionUsuario {
 
     try {
         uSv.modificarUsuario(id, nombre, email, password, password2, archivo);
         
         modelo.put("exito", "usuario actualizado correctamente");
          List<Noticia> noticias = noticiaServicio.listarNoticia();
        modelo.addAttribute("noticias", noticias);
        return "vistaInicio.html";
     } catch (MiExcepcion e) {
         
         modelo.put("error", e.getMessage());
          return "modificarUsuario.html";
     }
 
 
 }
}

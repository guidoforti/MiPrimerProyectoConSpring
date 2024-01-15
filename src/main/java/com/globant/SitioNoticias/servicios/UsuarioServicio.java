/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.servicios;

import com.globant.SitioNoticias.excepciones.MiExcepcionUsuario;
import com.globant.SitioNoticias.Enums.ROLES;
import com.globant.SitioNoticias.entidades.Imagen;
import com.globant.SitioNoticias.entidades.Usuario;
import com.globant.SitioNoticias.excepciones.MiExcepcion;
import com.globant.SitioNoticias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Guido
 */
//ESTE SERVICIO IMPLEMENTA ESTA INTERFAZ USERDETAILSERVICE QUE NOS PERMITE
// VALIDAR A CADA USUARIO QUE SE LOGUEA
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio r;
    
     @Autowired
    private ImagenServicio imgSv;

    @Transactional
    public void crearUsuario(String nombre, String email, String password, String password2, MultipartFile archivo) throws MiExcepcionUsuario {

        validar(nombre, email, password, password2);

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        //ESTO SE CONECTA CON EL METODO WEBSECURITY userdetailservice
        u.setPassword(new BCryptPasswordEncoder().encode(password));
        u.setRol(ROLES.USUARIO);

        Imagen img = imgSv.guardarImagen(archivo);

        u.setImgPerfil(img);

        r.save(u);

    }

    @Transactional
    public void modificarUsuario(Long id, String nombre, String email, String password, String password2, MultipartFile archivo) throws MiExcepcionUsuario, MiExcepcion {
        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = r.findById(id);

        if (respuesta.isPresent()) {
            Usuario u = respuesta.get();

            u.setNombre(nombre);
            u.setEmail(email);
            //ESTO SE CONECTA CON EL METODO WEBSECURITY userdetailservice
            u.setPassword(new BCryptPasswordEncoder().encode(password));
            u.setRol(ROLES.USUARIO);

            //HASTA ACA SE MODIFICA AL USUARIO DE MANERA NORMAL
            //PERO AGREGAMOS QUE , creamos una img vacia,  validamos que si la imagen del usuario actual que obtenemos de la respuesta no esta vacia, obtenemos SU ID y apartir de eso usamos el servicio de img, 
            // PARA ACTUALIZAR LA IMG, CON EL NUEVO ARCHIVO E ID , la guardamos en el usuario y PERSISTIMOS EL NUIEVO USUARIO.
            Long idImg = null;

            if (u.getImgPerfil() != null) {
                idImg = u.getImgPerfil().getId();
            }

            Imagen imgNueva = imgSv.actualizarImagen(archivo, id);
            u.setImgPerfil(imgNueva);

            r.save(u);

        }
    }
    
    public Usuario buscarPorId (Long id) {
        Usuario u = r.getOne(id);
        
        return u;
    }
    
    public List<Usuario> listarUsuarios () {
        
        List<Usuario> usuarios = new ArrayList();
        usuarios = r.findAll();
        
        return usuarios;
    }

   
    public void cambiarRol (Long id) throws MiExcepcionUsuario {
    
       Optional<Usuario> respuesta = r.findById(id);
       
       if (respuesta.isPresent()) {
       
       Usuario usuarioEncontrado = respuesta.get();
           System.out.println(usuarioEncontrado);
       if (usuarioEncontrado.getRol().equals(ROLES.USUARIO)) {
           
       usuarioEncontrado.setRol(ROLES.ADMIN);
       
       } else {
           
       usuarioEncontrado.setRol(ROLES.USUARIO);
       
       }
          r.save(usuarioEncontrado);
           
       }
        
    }
    
    
    private void validar(String nombre, String email, String password, String password2) throws MiExcepcionUsuario {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MiExcepcionUsuario("el nombre no puede estar vacio");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new MiExcepcionUsuario("el email no puede estar vacio");
        }

        if (!email.contains("@")) {
            throw new MiExcepcionUsuario("el email no es valido");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new MiExcepcionUsuario("la contraseña no puede estar vacia");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcionUsuario("la contraseñas deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //busco el usuario en nuestro domilio segun el mail
        Usuario u = r.buscarPorEmail(email);

        // si el usuario es diferente a nulo , comienzo a pasarlo de mi domino al dominio de spring security
        if (u != null) {
            // COMO EL USUARIO NO ES NULO, CREO UNA LISTA DE PERMISOS, estos son los permisos listados
            //son objetos de la clase grantedauthority
            List<GrantedAuthority> permisos = new ArrayList();

            //ahora creo algunos permisos  para que se los voy a dar SOLO A UN OBJETO QUE TENGA UN ROL DETERMINADO, EN ESTE CASO ROL DE ususari
            // y lo hago que sea como uns tring
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + u.getRol().toString());
            // A ESTE PERMISO LO AGREGO A LA LISTA DE PERMISOS
            permisos.add(p);

            //esto ATRAPA AL USUARIO YA AUTENTICADO Y LO GUARDA E NLA SESSION WEB
            // primero  llamo al request  como attr de atribute  y le pido que nos traiga losa tributos del contexto actuial , osea del usuario actual.
            // por lo tanto recupera los atributos de la solicitud http
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            // una vez que trae esta solicitud lo guardo en un objeto httpsession
            // y guardo al attr    a su sesion
            HttpSession sesion = attr.getRequest().getSession(true);

            // en los datos de esta sesion ,vamos a setear los atributos.
            // osea , en esta sesion, en esta variable, seteo un atributo llamado usuarioSesion como llave
            // y lo que va aobjtner es a este usuario que nosotros habiamos autenticado previamente 
            // y por lo tanto a sus datos
            sesion.setAttribute("usuarioSesion", u);

            return new User(u.getEmail(), u.getPassword(), permisos);

        } else {
            return null;
        }

        //ENTONCES TODO CONFIGURE PARA HACER QUE CUANDO UN USUARIO INICIE SESION CON SUS 
        // SPRIUNG SECURITY VA A DIRIGIRSE A ESTE METODO PARA OTORGAR LOS PERMISOS NECESARIOS
        // PARA ESTE USUARIO.
    }
}

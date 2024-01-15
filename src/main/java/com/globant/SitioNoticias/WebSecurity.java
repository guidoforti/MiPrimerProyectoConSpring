/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globant.SitioNoticias;

import com.globant.SitioNoticias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecurity extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private  UsuarioServicio uSv;
    
    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception{
        // cuando se registra un usuario, vamos a autenticarlo atravez de lmetodo user detai service
        // y una vez autenticado le vamos a pasar el CODIFICADOR de la contraseña 
        //Y EN EL METODO DEL SERVICIO DONDE SE GUARDA LA CONTRASEÑA VAMOS A TENER
        // QUE SETEARLA CON EL CODIFICADOR
        auth.userDetailsService(uSv).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/css/*","/js/*","/img/*","/**")
                .permitAll()
                //configuracion para loguearnos, esto es para el formulari ode login
                .and().formLogin()
                // indicamos cual va a ser la url de la login page
                .loginPage("/login")
                // cual va a ser la url que va a autenticar , con la cual spring security autetifica un ususario
                .loginProcessingUrl("/logincheck")
                // configramos las credenciales, parametro de nombre usu y la pass
                .usernameParameter("email")
                .passwordParameter("password")
                // configuro a que url se va dirigir si el login es correcto
                .defaultSuccessUrl("/inicio")
                .permitAll()
                //configuro el logout
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and().csrf()
                .disable();
               
    }
} 

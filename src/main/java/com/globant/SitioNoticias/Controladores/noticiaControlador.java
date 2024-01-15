/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.Controladores;

import com.globant.SitioNoticias.entidades.Noticia;
import com.globant.SitioNoticias.excepciones.MiExcepcion;
import com.globant.SitioNoticias.servicios.NoticiaServicio;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


//ESTO DICE QUE ES UNCONTROLADOR Y EL DE  ABAJO ME REDIRIJE AL URL NOTICIA
@Controller
@RequestMapping("/noticia") //localhost :8081/noticia -- ingrese automaticame al componente noticiacontrolador
public class noticiaControlador {
    
    //ESTO AUTO INICIALIZA EL SERVICIO DE NOTICIA
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    
    //ESTA PETICION GETHTTP VA A RESPONDER AL LLAMADO DE localhost :8081/noticia/registrar si o si tiene noticia antes para poder ingresar al controlador
    //este metodo RETORNA UN STRING, Y NOS RETORNA LA PAGINA QUE QUEREMOS MOSTRAR, NUESTRO FORM DE NOTICIA
     @GetMapping("/panelAdmin")
    public String panelAdmin (ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticia();
        modelo.addAttribute("noticias", noticias);
        return "panelAdmin.html";
    
            } 
    
     @GetMapping("/inicio")
    public String inicio (ModelMap modelo) {
        
        List<Noticia> noticias = noticiaServicio.listarNoticia();
        modelo.addAttribute("noticias", noticias);
           
    return "vistaInicio.html";
    
            }
    
    
     @GetMapping("/laNoticia/{id}")
    public String laNoticia (@PathVariable Long id, ModelMap modelo) {
        
        modelo.put("noticia", noticiaServicio.getOne(id));
    return "vistaNoticia.html";
    
            } 
    
    @GetMapping("/modificar/{id}")
    public String modificar (@PathVariable Long id, ModelMap modelo) {
        
        modelo.put("noticia",noticiaServicio.getOne(id));
       
        return "modificar.html";
    }
    
     @GetMapping("/eliminar/{id}")
    public String eliminar (@PathVariable Long id, ModelMap modelo) {
        
      
            noticiaServicio.darDeBajaNoticia(id);
        
       
          return "redirect:/noticia/panelAdmin";
    }
  
   @PostMapping("/modificarLaNoticia") 
   public String modificarLaNoticia (@RequestParam  Long id ,@RequestParam String tituloNuevo , @RequestParam String cuerpoNuevo, ModelMap modelo) throws MiExcepcion  {
  
       try {
           
            noticiaServicio.modificarNoticia(id, tituloNuevo, cuerpoNuevo);
       } catch (MiExcepcion e) {
           e.getMessage();
          return "redirect:/noticia/panelAdmin";
       } 
  
   return "redirect:/noticia/panelAdmin";
   }
    
    //aca este post, esta pidiendo el POST DEL form que tiene un action /noticia/subirLaNoticia y nosotros pasamos eso por este postmapping
    //EL METODO RECIBE COMO PARAMETRO, UN STRING QUE TIENE QUE TENER REQUESTPARAM Y QUE VA A SER EN ESTE CASO EL NOMBRE DEL AUTOR DE LA NOTICIA
    // YO TENGO QUE PONER COMO NOMBRE DE PARAMETRO EL MISMO NOMBRE QUE LE PUSE EN EL ATRIBUTO NAME AL INPUT
    @PostMapping("/subirLaNoticia")
    // EL request param indica que es un parametro qeu viaja en la URL y que va a llegar cuando se ejecute e lformulario
    public String subirLaNoticia (@RequestParam String tituloDeNoticia , @RequestParam String cuerpo, ModelMap modelo) throws MiExcepcion{
         
// este controlador tiene que RECIBIR EL DATO QUE ESTA LLEGANDO Y ENVIARLOS AL METODO DEL SERVICIO NOTICIA QUE CREA LA NOTICIA por eso se crea noticiaservicio
// entonces recibe los datos Y LLAMA A LSERVICIO DE LA NOTICIA Y LOS PASA AL METODO DE CREAR NOTICIA
// asi entonces es como lo rodeo con un trycatch, si sale un errro vuelve al formulario y sino va al index
      

//POR OTRO LADO, HACEMOS USO DEL MODEL MAP, EL MODEL MAP NOS VA A PERMITIR MOSTRAR COSAS ALUSUARIO, LOI PASAMOS OCMO PARAMETRO
// Y SI TODO FUNCIONA, HACEMOS EL MODELO.PUT , CON LA LLAVE EXITO, AVISAMOS QUE SE CARGO CON EXITO
// SI SALE UNA EXCEPCION, CON LA LLAVE ERROR, LLAMAMOS AL MSJ DE LE EXCEPCION ,PERO COMO SABE EL HTML QUE TIENE QUE MOSTRAR 
// ALGO SEGUN UNA U OTRA LLAVE? --> PARA ESO TENEMOS QUE CREAR COSAS EN EL HTML , MENSAJES, Y PARA ESOS MENSAJES, USAR
// TIMELEAF !!!!!!!!!!!!!!!! PARA PONERLE UNA CONDICION
        try {
           
            noticiaServicio.crearNoticia(tituloDeNoticia, cuerpo);
            modelo.put("exito", "la noticia fue cargada con exito");
             System.out.println("se creo");
            
        } catch (MiExcepcion ex) {
             modelo.put("error", ex.getMessage());
     
            System.out.println("no se creo");
               return "redirect:/noticia/panelAdmin";
        }
       
        return "redirect:/noticia/panelAdmin";
    }
    
   // @PostMapping("/modificar/{id}"){
//}
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.Controladores;

import java.text.BreakIterator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class ErrorControlador implements ErrorController{
    
    //en este caso, estamos haciedo el request mapping a nivel del metodo y no a ni vel de la clase como lo soliamos hacer, pero solo como para trabajkarlo distinto
    // todo recurso que vednga CON /ERROR VA A INGRESAR A ESTE METODO QUE ESTAMOS HACIEDNO
    //lo que hacemos es recuperar todo el codigo de error que viene del servidor y en base a eso establecemos un msj particular segund el codigo de error y abrimos una vista especifica.
    @RequestMapping( value = "/error", method = {RequestMethod.GET, RequestMethod.POST} )
    public ModelAndView renderErrorPage (HttpServletRequest httpRequest) {
        //model and view trabaja parecid a model map, la dif es que este retorna directamente el model oy la vista
    ModelAndView errorPage = new ModelAndView("error");
    String msjError = "";
    // se obtiene el codigo desde un metodo getErrrocode que esta abajo
    int httpErrorCode = getErrorCode(httpRequest);
   
    switch (httpErrorCode) {
    
        case 400 : {
            msjError = "el recurso solicitado no existe";
            break;
        }
        
        case 401 : {
            msjError = "no se encuentra autorizado";
            break;
        }
        
        case 403 : {
            msjError = "No tiene permisos para acceder al recurso";
            break;
        }
        
        case 404 : {
            msjError = "el recurso solicitado no fue encontrado";
            break;
        }
    
        case 500 : {
            msjError = "ocurrio un error interno";
            break;
        }
    
    
    }
    
    errorPage.addObject("codigo", httpErrorCode);
    errorPage.addObject("mensaje", msjError);
    return errorPage;
    }

    
    private int getErrorCode(HttpServletRequest httpRequest) {
      return  (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
        
    }
    

}

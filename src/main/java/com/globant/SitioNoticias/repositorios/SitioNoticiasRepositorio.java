/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globant.SitioNoticias.repositorios;

import com.globant.SitioNoticias.entidades.Noticia;
import java.util.List;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SitioNoticiasRepositorio extends JpaRepository<Noticia, Long> {
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public Noticia buscarPorTitulo(@Param("titulo") String titulo) ;
    
      @Query("SELECT n FROM Noticia n WHERE n.id = :id")
    public Noticia buscarPorId(@Param("id") Long id) ;
    
    // ACA SI QUIERO DEVOLVER TODAS LAS NOTICIAS CON IGUAL TITULO , DEVUELVO UNA LISTA
       @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public List <Noticia> buscarNoticiasPorTituloIgual(@Param("titulo") String titulo) ;
    
}



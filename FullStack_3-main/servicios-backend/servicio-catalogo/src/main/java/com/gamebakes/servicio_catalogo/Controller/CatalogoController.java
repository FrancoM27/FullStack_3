package com.gamebakes.servicio_catalogo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamebakes.servicio_catalogo.Model.Catalogo;
import com.gamebakes.servicio_catalogo.Service.CatalogoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {
    
    @Autowired
    private CatalogoService catalogoService;

    @GetMapping
    public List<Catalogo> Listar(){
        return catalogoService.verCatalogoCompleto();
    }
    
    @GetMapping("/categoria/{categoria}")
    public List<Catalogo> ListarPorCategoria(@PathVariable String categoria){
        return catalogoService.verCatalogoPorCategoria(categoria);
    }




}

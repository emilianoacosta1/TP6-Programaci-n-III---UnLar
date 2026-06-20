package com.unlar.edu.ar.controller;

import com.unlar.edu.ar.model.Estudiante;
import com.unlar.edu.ar.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesOrdenados(
            @RequestParam(defaultValue = "promedio") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        
        // 1. Obtenemos la lista original
        List<Estudiante> listaBase = estudianteService.obtenerTodos();
        
        // 2. Usamos el método con la firma estricta que pide el TP
        List<Estudiante> listaOrdenada = estudianteService.ordenar(listaBase, sortBy, order);
        
        return ResponseEntity.ok(listaOrdenada);
    }
}
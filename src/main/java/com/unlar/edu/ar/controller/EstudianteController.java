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

    // Inyección de dependencias
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesOrdenados(
            @RequestParam(defaultValue = "promedio") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        
        List<Estudiante> listaOrdenada = estudianteService.ordenar(sortBy, order);
        return ResponseEntity.ok(listaOrdenada);
    }
}
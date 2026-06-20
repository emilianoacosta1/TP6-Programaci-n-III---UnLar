package com.unlar.edu.ar.controller;

import com.unlar.edu.ar.model.Estudiante;
import com.unlar.edu.ar.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        List<Estudiante> listaBase = estudianteService.obtenerTodos();
        List<Estudiante> listaOrdenada = estudianteService.ordenar(listaBase, sortBy, order);
        
        return ResponseEntity.ok(listaOrdenada);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleCriterioInvalido(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Criterio de ordenamiento no válido");
        response.put("criterioRecibido", ex.getMessage().replace("Criterio inválido: ", ""));
        response.put("criteriosAceptados", List.of("promedio", "edad", "nombre", "materiasAprobadas", "legajo"));
        
        return ResponseEntity.badRequest().body(response);
    }
}
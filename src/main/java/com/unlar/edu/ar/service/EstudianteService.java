package com.unlar.edu.ar.service;

import com.unlar.edu.ar.model.Estudiante;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstudianteService {

    private List<Estudiante> estudiantesEnMemoria;
    private Map<String, Comparator<Estudiante>> estrategiasDeOrdenamiento;

    @PostConstruct
    public void init() {
        estudiantesEnMemoria = new ArrayList<>(Arrays.asList(
                new Estudiante("LU-2024-001", "Martín Quiroga", 8.5, 22, 18),
                new Estudiante("LU-2024-002", "Valeria Díaz", 8.5, 20, 15),
                new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22),
                new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24),
                new Estudiante("LU-2024-005", "Lucas González", 9.1, 23, 24),
                new Estudiante("LU-2024-006", "Agustina López", 6.8, 19, 10),
                new Estudiante("LU-2024-007", "Nahuel Herrera", 7.5, 22, 14),
                new Estudiante("LU-2024-008", "Florencia Ríos", 8.9, 25, 20),
                new Estudiante("LU-2024-009", "Tomás Sosa", 6.5, 20, 12),
                new Estudiante("LU-2024-010", "Lucía Fernández", 7.8, 21, 16)
        ));

        // Punto 1: El Map que reemplaza al switch
        estrategiasDeOrdenamiento = new HashMap<>();
        estrategiasDeOrdenamiento.put("promedio", Comparator.comparing(Estudiante::getPromedio));
        estrategiasDeOrdenamiento.put("edad", Comparator.comparing(Estudiante::getEdad));
        estrategiasDeOrdenamiento.put("nombre", Comparator.comparing(Estudiante::getNombre));
        estrategiasDeOrdenamiento.put("materiasAprobadas", Comparator.comparing(Estudiante::getCantidadMateriasAprobadas));
        estrategiasDeOrdenamiento.put("legajo", Comparator.comparing(Estudiante::getLegajo));
    }

    public List<Estudiante> obtenerTodos() {
        return new ArrayList<>(estudiantesEnMemoria);
    }

    // Punto 2: Firma exacta pedida en el TP
    public List<Estudiante> ordenar(List<Estudiante> lista, String sortBy, String order) {
        
        // Si no existe el criterio, lanzamos excepción (luego Spring tira error 400 o 500)
        if (!estrategiasDeOrdenamiento.containsKey(sortBy)) {
            throw new IllegalArgumentException("Criterio inválido: " + sortBy);
        }

        // Buscamos el comparador y le sumamos el tie-breaker por legajo (del Ejercicio 7)
        Comparator<Estudiante> comparator = estrategiasDeOrdenamiento.get(sortBy)
                                                .thenComparing(Estudiante::getLegajo);

        // Aplicamos reversed si lo piden
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        // Ordenamos y devolvemos
        List<Estudiante> listaOrdenada = new ArrayList<>(lista);
        listaOrdenada.sort(comparator);

        return listaOrdenada;
    }
}
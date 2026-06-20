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
        // Inicializamos los 10 estudiantes con los empates intencionales
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

        // Armamos el Map con las estrategias de comparación (Esto nos adelanta el Ejercicio 8)
        estrategiasDeOrdenamiento = new HashMap<>();
        estrategiasDeOrdenamiento.put("promedio", Comparator.comparing(Estudiante::getPromedio));
        estrategiasDeOrdenamiento.put("edad", Comparator.comparing(Estudiante::getEdad));
        estrategiasDeOrdenamiento.put("nombre", Comparator.comparing(Estudiante::getNombre));
        estrategiasDeOrdenamiento.put("materiasAprobadas", Comparator.comparing(Estudiante::getCantidadMateriasAprobadas));
        estrategiasDeOrdenamiento.put("legajo", Comparator.comparing(Estudiante::getLegajo));
    }

    public List<Estudiante> ordenar(String sortBy, String order) {
        // Validamos que el criterio exista
        if (!estrategiasDeOrdenamiento.containsKey(sortBy)) {
            throw new IllegalArgumentException("Criterio inválido: " + sortBy);
        }

        // 1. Obtenemos el comparador principal desde el mapa
        Comparator<Estudiante> comparator = estrategiasDeOrdenamiento.get(sortBy);

        // 2. Aplicamos el tie-breaker (desempate) por defecto que exige el Ejercicio 7
        comparator = comparator.thenComparing(Estudiante::getLegajo);

        // 3. Verificamos si el cliente pidió orden descendente
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        // 4. Creamos una copia de la lista para no alterar el orden original en memoria
        List<Estudiante> listaOrdenada = new ArrayList<>(estudiantesEnMemoria);
        listaOrdenada.sort(comparator);

        return listaOrdenada;
    }
}
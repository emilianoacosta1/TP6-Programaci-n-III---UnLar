package com.unlar.edu.ar.ui;

import com.unlar.edu.ar.model.Estudiante;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPractica {

    public static void main(String[] args) {

        List<Estudiante> lista = new ArrayList<>();
        
        lista.add(new Estudiante("LU-2024-001", "Martín Quiroga", 8.5, 22, 18));
        lista.add(new Estudiante("LU-2024-002", "Valeria Díaz", 8.5, 20, 15));
        lista.add(new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22));
        lista.add(new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24));
        lista.add(new Estudiante("LU-2024-006", "Agustina López", 6.8, 19, 10));

        System.out.println("--- LISTA ANTES DE ORDENAR ---");
        lista.forEach(System.out::println);

        // Ahora compila y ordena usando el compareTo que definimos
        Collections.sort(lista);

        System.out.println("\n--- LISTA DESPUÉS DE ORDENAR (PROMEDIO DESCENDENTE) ---");
        lista.forEach(System.out::println);
    }
}
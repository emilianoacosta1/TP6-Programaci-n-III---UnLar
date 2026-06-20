package com.unlar.edu.ar.ui;

import com.unlar.edu.ar.model.Estudiante;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator; // <-- ¡Nuevo import clave para la Parte 3!
import java.util.List;

public class MainPractica {

    public static void main(String[] args) {

        List<Estudiante> lista = new ArrayList<>();
        
        // Los 10 estudiantes con empates intencionales para probar a fondo
        lista.add(new Estudiante("LU-2024-001", "Martín Quiroga", 8.5, 22, 18));
        lista.add(new Estudiante("LU-2024-002", "Valeria Díaz", 8.5, 20, 15));
        lista.add(new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22));
        lista.add(new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24));
        lista.add(new Estudiante("LU-2024-005", "Lucas González", 9.1, 23, 24));
        lista.add(new Estudiante("LU-2024-006", "Agustina López", 6.8, 19, 10));
        lista.add(new Estudiante("LU-2024-007", "Nahuel Herrera", 7.5, 22, 14));
        lista.add(new Estudiante("LU-2024-008", "Florencia Ríos", 8.9, 25, 20));
        lista.add(new Estudiante("LU-2024-009", "Tomás Sosa", 6.5, 20, 12));
        lista.add(new Estudiante("LU-2024-010", "Lucía Fernández", 7.8, 21, 16));

        System.out.println("--- LISTA ANTES DE ORDENAR ---");
        lista.forEach(System.out::println);

        // --- PARTE 2: Comparable ---
        Collections.sort(lista);
        System.out.println("\n--- LISTA ORDENADA (PROMEDIO DESCENDENTE - Comparable) ---");
        lista.forEach(System.out::println);

        // =========================================================
        // PARTE 3 - EJERCICIO 4: Lambdas y Method References
        // =========================================================

        // 1. Con lambda explícita usando Integer.compare() para evitar el overflow
        Comparator<Estudiante> porMateriasAsc = (e1, e2) -> 
            Integer.compare(e1.getCantidadMateriasAprobadas(), e2.getCantidadMateriasAprobadas());

        // 2. Con method references (mucho más limpio y legible)
        Comparator<Estudiante> porNombreAlpha = Comparator.comparing(Estudiante::getNombre);
        Comparator<Estudiante> porEdadAsc = Comparator.comparing(Estudiante::getEdad);

        // 3. Verificamos los tres criterios usando list.sort()
        System.out.println("\n--- Orden por Materias Aprobadas (Ascendente / Lambda) ---");
        lista.sort(porMateriasAsc);
        lista.forEach(System.out::println);

        System.out.println("\n--- Orden por Nombre (Alfabético / Method Reference) ---");
        lista.sort(porNombreAlpha);
        lista.forEach(System.out::println);

        System.out.println("\n--- Orden por Edad (Ascendente / Method Reference) ---");
        lista.sort(porEdadAsc);
        lista.forEach(System.out::println);


        // =========================================================
        // PARTE 3 - EJERCICIO 5: Criterios compuestos y orden inverso
        // =========================================================

        // 1. Desempate con thenComparing(): promedio descendente, luego nombre ascendente
        Comparator<Estudiante> porPromedioDescYNombre = Comparator
                .comparing(Estudiante::getPromedio)
                .reversed()
                .thenComparing(Estudiante::getNombre);

        // 2. Orden inverso con reversed(): a partir del descendente, generamos el ascendente
        Comparator<Estudiante> porPromedioDesc = Comparator.comparing(Estudiante::getPromedio).reversed();
        Comparator<Estudiante> porPromedioAsc = porPromedioDesc.reversed();

        // 3. Combiná todo: cantidadMateriasAprobadas descendente, luego nombre ascendente
        Comparator<Estudiante> porMateriasDescYNombre = Comparator
                .comparing(Estudiante::getCantidadMateriasAprobadas)
                .reversed()
                .thenComparing(Estudiante::getNombre);

        // --- Verificación (Criterio de aceptación) ---
        
        System.out.println("\n--- 1. Orden Compuesto: Promedio DESC, desempata Nombre ASC ---");
        lista.sort(porPromedioDescYNombre);
        lista.forEach(System.out::println);
        // Desempate visual: Camila Torres y Lucas González tienen 9.1. Camila debe ir primero.

        System.out.println("\n--- 2. Orden Inverso: Promedio ASC (generado con .reversed()) ---");
        lista.sort(porPromedioAsc);
        lista.forEach(System.out::println);

        System.out.println("\n--- 3. Combinado: Materias DESC, desempata Nombre ASC ---");
        lista.sort(porMateriasDescYNombre);
        lista.forEach(System.out::println);
        // Desempate visual: Camila Torres y Lucas González tienen 24 materias. Camila debe ir primero.

        // =========================================================
        // PARTE 3 - EJERCICIO 6: El anti-patrón de la resta
        // =========================================================
        System.out.println("\n=== DEMOSTRACIÓN DEL ANTI-PATRÓN DE LA RESTA ===");

        // 1. Creamos los dos estudiantes con las edades extremas para forzar el error
        Estudiante eMax = new Estudiante("LU-MAX", "Estudiante Viejo", 5.0, Integer.MAX_VALUE, 10);
        Estudiante eMin = new Estudiante("LU-MIN", "Estudiante Joven", 5.0, -1, 10);

        // 2. Definimos el comparator con el "truco" prohibido y el comparator correcto
        Comparator<Estudiante> restaTramposa = (e1, e2) -> e1.getEdad() - e2.getEdad();
        Comparator<Estudiante> comparacionCorrecta = Comparator.comparing(Estudiante::getEdad);

        System.out.println("Edad de " + eMax.getNombre() + ": " + eMax.getEdad());
        System.out.println("Edad de " + eMin.getNombre() + ": " + eMin.getEdad());

        // 3. Verificamos el error en tiempo de ejecución
        int resultadoTramposo = restaTramposa.compare(eMax, eMin);
        System.out.println("\nResultado usando (eMax - eMin): " + resultadoTramposo);
        if (resultadoTramposo < 0) {
            System.out.println("Incorrecto: Da negativo debido al overflow. Java cree falsamente que eMax es MENOR que eMin.");
        }

        // 4. Comprobamos la solución con Integer.compare()
        int resultadoCorrecto = comparacionCorrecta.compare(eMax, eMin);
        System.out.println("\nResultado usando Integer.compare(eMax, eMin): " + resultadoCorrecto);
        if (resultadoCorrecto > 0) {
            System.out.println("Correcto: Da positivo (1). Java sabe que eMax es estrictamente MAYOR que eMin.");
        }
    }
}
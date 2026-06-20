# Respuestas del TP - Ordenamiento de Objetos: Comparable, Comparator y Polimorfismo

## Parte 1 — El problema

**Pregunta 1: ¿Por qué `Collections.sort()` no compila cuando le pasamos una `List<Estudiante>`? ¿Qué contrato exige Java que nuestra clase no está cumpliendo?**

**Respuesta 1:** `Collections.sort()` no compila porque Java no puede inferir un ordenamiento para objetos de dominio propio. A diferencia de clases nativas como `String` o `Integer` que ya tienen un orden predefinido, nuestra clase `Estudiante` no le indica al compilador qué atributo evaluar (edad, legajo, promedio, etc.). 

El contrato estricto que Java exige es que los elementos de la lista implementen la interfaz `Comparable<T>`. Al no implementarla, nuestra clase carece del método `compareTo(T o)`, el cual es el mecanismo que utiliza el algoritmo de ordenamiento subyacente para determinar si un objeto debe ir antes, después o en la misma posición que otro.

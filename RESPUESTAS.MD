# Respuestas del TP - Ordenamiento de Objetos: Comparable, Comparator y Polimorfismo

## Parte 1 — El problema

**Pregunta 1: ¿Por qué `Collections.sort()` no compila cuando le pasamos una `List<Estudiante>`? ¿Qué contrato exige Java que nuestra clase no está cumpliendo?**

**Respuesta 1:** `Collections.sort()` no compila porque Java no puede inferir un ordenamiento para objetos de dominio propio. A diferencia de clases nativas como `String` o `Integer` que ya tienen un orden predefinido, nuestra clase `Estudiante` no le indica al compilador qué atributo evaluar (edad, legajo, promedio, etc.). 

El contrato estricto que Java exige es que los elementos de la lista implementen la interfaz `Comparable<T>`. Al no implementarla, nuestra clase carece del método `compareTo(T o)`, el cual es el mecanismo que utiliza el algoritmo de ordenamiento subyacente para determinar si un objeto debe ir antes, después o en la misma posición que otro.

**Pregunta 2: ¿Por qué elegiste el atributo `promedio` como orden natural? ¿Qué pasaría si mañana un nuevo requisito pide ordenar por `cantidadMateriasAprobadas`? ¿Modificarías `compareTo`? ¿Qué consecuencias tendría?**

**Respuesta 2:** Elegimos el atributo `promedio` porque, al modelar un dominio universitario, el mérito académico representa la jerarquía más intrínseca o "natural" a la hora de evaluar un listado general de estudiantes. Es el criterio por defecto que uno esperaría encontrar.

Si el día de mañana surge un nuevo requisito en el sistema pidiendo ordenar por `cantidadMateriasAprobadas`, **bajo ningún punto de vista modificaríamos el método `compareTo`**. 

Las consecuencias de hacerlo serían críticas a nivel de diseño y mantenimiento. Al alterar el orden natural de la clase, estaríamos rompiendo silenciosamente el comportamiento de cualquier otro módulo que ya confíe en el ordenamiento implícito original (como podría ser, por ejemplo, un sistema de padrones o listas universitarias que ya esté en producción). Estaríamos violando directamente el Principio Abierto/Cerrado (OCP), ya que estaríamos "abriendo" una clase que ya funciona para modificarla ante un nuevo requisito. La forma correcta y segura de resolverlo es dejar `compareTo` intacto y crear una estrategia de ordenamiento externa utilizando la interfaz `Comparator`.

**Pregunta 3: `Comparable` nos ata a un único criterio de ordenamiento. ¿Qué problemas de diseño introduce esto si nuestro sistema necesitara ordenar la misma lista de estudiantes de 4 formas distintas según el contexto? Relacioná tu respuesta con los principios de responsabilidad única (SRP) y abierto/cerrado (OCP).**

**Respuesta 3:** Tratar de forzar múltiples criterios de ordenamiento dentro de la clase `Estudiante` utilizando únicamente `Comparable` genera un alto acoplamiento y rompe directamente con dos principios fundamentales del diseño orientado a objetos:

* **Violación del Principio de Responsabilidad Única (SRP):** La clase `Estudiante` debería tener una única razón para cambiar: modificaciones en su propio estado o reglas de negocio internas. Si le inyectamos la responsabilidad de saber cómo ordenarse para 4 contextos distintos (por ejemplo, alfabéticamente para un padrón, por edad para una estadística, o por materias para un reporte), la clase asume responsabilidades externas. Esto ensucia el dominio y le da múltiples motivos para ser modificada.
* **Violación del Principio Abierto/Cerrado (OCP):** Este principio dicta que las entidades de software deben estar abiertas a la extensión, pero cerradas a la modificación. Si estamos atados a `Comparable` (que permite un solo orden natural) y se requiere un quinto criterio, estamos obligados a "abrir" el código fuente de `Estudiante.java` para alterarlo. 

La solución de diseño para mantener la cohesión alta y el acoplamiento bajo es externalizar este comportamiento mediante la interfaz `Comparator`. Al separar las lógicas de comparación, podemos crear infinitos criterios sin necesidad de tocar ni recompilar la clase original.
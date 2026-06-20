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

**Pregunta 4: Explicá con tus palabras qué es un overflow de enteros, por qué el "truco de la resta" lo provoca, qué parte del contrato de `Comparator` rompe, y por qué `Integer.compare()` no sufre este problema.**

**Respuesta 4:** Un *overflow* (o desbordamiento) de enteros ocurre cuando el resultado de una operación matemática supera el límite máximo o mínimo de memoria que el tipo de dato `int` puede almacenar. 

El "truco de la resta" (`e1.getEdad() - e2.getEdad()`) provoca este problema porque es una operación aritmética ciega a los límites. Como pudimos comprobar, si a un número positivo muy grande le restamos un número negativo (por ejemplo, `Integer.MAX_VALUE - (-1)`), el resultado sobrepasa el límite positivo y "da la vuelta" hacia el extremo negativo, dando un resultado de `-2147483648`.

Al devolver este valor negativo, se rompe directamente la **transitividad, simetría y consistencia** del contrato que exige la interfaz `Comparator`. Básicamente, le estamos dando información falsa a Java, indicándole que el número positivo máximo es "menor" que `-1`.

La forma correcta, que es utilizar `Integer.compare()`, no sufre de este desbordamiento porque **no realiza restas matemáticas**. En su lugar, evalúa directamente los valores mediante operadores relacionales lógicos (`<`, `==`, `>`) y retorna `-1`, `0` o `1` de manera segura ante cualquier caso extremo.

**Pregunta 5: ¿Qué patrón de diseño estás aplicando al usar un `Map<String, Comparator<T>>` en lugar de un `switch`? Explicá cómo se relaciona este patrón con el polimorfismo y por qué es preferible a la alternativa procedural.**

**Respuesta 5:** Al utilizar un `Map` para registrar y recuperar dinámicamente las distintas lógicas de ordenamiento, estamos aplicando el **Patrón de Diseño Strategy (Estrategia)**. 

Este patrón se relaciona íntimamente con el **polimorfismo** porque nos permite tratar a múltiples comportamientos distintos de una manera unificada. En nuestro caso, todos los valores almacenados en el mapa implementan la misma interfaz genérica (`Comparator<T>`). Nuestro método `ordenar` no necesita saber si está comparando números enteros, decimales o cadenas de texto; simplemente extrae la "estrategia" correspondiente según la clave ingresada y le delega el trabajo ejecutando el método de comparación. Gracias al polimorfismo, Java resuelve en tiempo de ejecución cuál es la implementación exacta que debe usar.

Esta solución orientada a objetos es ampliamente preferible a la alternativa procedural (como un `switch` o múltiples `if-else`) porque respeta el **Principio Abierto/Cerrado (Open/Closed Principle)** de SOLID:
* **Mantenibilidad (Abierto a la extensión, cerrado a la modificación):** Si el día de mañana requerimos agregar un nuevo criterio (por ejemplo, ordenar por "fecha de inscripción"), con un `switch` estaríamos obligados a modificar el código central agregando un nuevo `case`. Con el patrón Strategy, el método `ordenar` queda intacto y cerrado a modificaciones; simplemente "extendemos" la funcionalidad agregando un nuevo par clave-valor al `Map` inicial.
* **Legibilidad y Cohesión:** Elimina la complejidad ciclomática. El método central se vuelve mucho más limpio y fácil de leer, ya que delega la responsabilidad de selección a la estructura de datos y se enfoca únicamente en aplicar la estrategia obtenida.
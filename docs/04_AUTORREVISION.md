# Autorrevisión técnica | Ae6

**Estudiante:** Justin Arreaga Ramírez
**Pull Request:** [#3 · test: completar suite, cobertura y evidencia de Ae6](https://github.com/justinjw93/UEES_UCOM0310_Semana7_Proyecto_BASE_Ae6/pull/3)

Antes de la entrega se revisaron los archivos cambiados del Pull Request, la calidad de las pruebas, el historial Git y la documentación.

## Checklist

| Punto de revisión | Resultado | Cómo se comprobó |
|---|---|---|
| Revisé Files changed línea por línea | ✅ | Se revisaron los 18 archivos del PR: 2 de pruebas, 4 de documentación y 12 de evidencia. |
| No hay `target/` ni archivos generados | ✅ | `git ls-files` no muestra `target/`, `.class`, `jacoco.exec` ni archivos del IDE. El `.gitignore` los excluye. |
| No hay credenciales ni datos sensibles | ✅ | Se buscaron palabras como *password*, *token* o *secret* en todo el repositorio, sin resultados. Las evidencias solo muestran rutas de carpetas locales de Maven. |
| Los nombres de las pruebas son expresivos | ✅ | Todas describen el comportamiento (`dosHorasEsElLimitePermitido`) y tienen un `@DisplayName` con el número de caso. No hay nombres como `test1()`. |
| Las aserciones comprueban comportamiento | ✅ | Todas las pruebas tienen al menos un `assert` o `verify` sobre un resultado del negocio: estado, monto, mensaje de error o interacción. |
| Los casos límite están presentes | ✅ | CP-02, CP-03, CP-04 y CP-08, más los inválidos CP-09, CP-13, CP-14, CP-15 y CP-18. |
| Los mocks verifican interacciones relevantes | ✅ | Solo se verifica guardar, notificar y el orden entre ambos. La consulta de disponibilidad solo se verifica cuando importa que *no* ocurra (CP-12). |
| La cobertura está interpretada | ✅ | El análisis explica cuatro mediciones, los huecos, lo que la cobertura no muestra y la decisión de no probar `cancelar()` ni `getId()`. |
| Los commits explican la evolución | ✅ | Cada commit tiene un solo propósito y sigue el formato `tipo: descripción` (`docs`, `test`, `refactor`, `chore`). |
| El PR indica cómo verificar | ✅ | Incluye el comando `mvn clean test` y el resultado esperado (21 pruebas, `BUILD SUCCESS`). |

## Hallazgos y correcciones

| Hallazgo | Acción |
|---|---|
| El archivo `evidencias/ae6/jacoco_linea_base.csv` era idéntico a `evidencias/ae6/jacoco_antes/jacoco.csv`. | Se eliminó el duplicado en un commit aparte (`chore: eliminar csv duplicado de la linea base de cobertura`). |
| La prueba `entornoJUnitFunciona()` no comprueba ningún comportamiento (`assertTrue(true)`). | Se mantiene porque viene del proyecto base y la actividad pide no eliminar las pruebas previas. Solo confirma que JUnit funciona. |
| Maven agrupa el conteo de pruebas de forma irregular: suma la prueba de entorno al grupo de confirmación. | No es un error de la suite. El total (21) es correcto y coincide con la matriz (20 casos) más la prueba de entorno. |
| Al ejecutar aparece un aviso de Java sobre la carga de un agente, generado por Mockito. | No afecta los resultados. Se decidió no modificar el `pom.xml` del proyecto base para ocultarlo. |

## Comprobación de que la suite detecta errores

Se hicieron dos experimentos en los que se introdujo un error a propósito en el código y luego se restauró:

| Experimento | Prueba que lo detectó | Evidencia |
|---|---|---|
| Cambiar el límite de cancelación de "2 horas o más" a "más de 2 horas" | Solo CP-02 | [`evidencias/lab1/02_microexperimento_frontera.txt`](evidencias/lab1/02_microexperimento_frontera.txt) |
| Notificar antes de guardar la reserva | Solo CP-19 | [`evidencias/ae6/02_experimento_orden_confirmacion.txt`](evidencias/ae6/02_experimento_orden_confirmacion.txt) |

En ambos casos el código productivo quedó sin cambios, como muestra `git status` al final de cada evidencia.

## Historial Git

Evidencia: [`evidencias/ae6/04_git_log.txt`](evidencias/ae6/04_git_log.txt)

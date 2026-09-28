# Laboratorio 1 | Diseño de casos y JUnit 5

**Estudiante:** Justin Arreaga Ramírez
**Rama:** `test/lab1-junit-casos`

## 1. Punto de partida

Antes de hacer cambios se revisó que las herramientas estuvieran listas y que el proyecto funcionara tal como se entregó.

| Herramienta | Versión |
|---|---|
| Java | 21.0.12 |
| Maven | 3.9.16 |
| Git | 2.45.1 |

La prueba inicial del proyecto pasó sin problemas.
Evidencia: [`evidencias/lab1/01_linea_base.txt`](evidencias/lab1/01_linea_base.txt)

## 2. Diseño de casos

Primero se diseñaron 9 casos, sin escribir código todavía: 4 para la regla de cancelación y 5 para la de descuentos. Incluyen situaciones comunes, variantes de la regla, valores en el límite y un dato inválido.
Detalle: [`01_MATRIZ_CASOS_PLANTILLA.md`](01_MATRIZ_CASOS_PLANTILLA.md)

## 3. Pruebas implementadas

Cada prueba sigue tres pasos: preparar los datos, ejecutar la acción y comprobar el resultado (estructura AAA). Los nombres dicen qué comportamiento se espera, para que se entiendan sin leer el código.

| Caso | Prueba | Tipo |
|---|---|---|
| CP-01 | `cincoHorasPermitenCancelar` | Normal |
| CP-02 | `dosHorasEsElLimitePermitido` | Límite |
| CP-03 | `unaHoraNoPermiteCancelar` | Límite |
| CP-04 | `ceroHorasNoPermiteCancelar` | Extremo |
| CP-05 | `normalNoRecibeDescuento` | Normal |
| CP-06 | `vipRecibeQuincePorCiento` | Alternativo |
| CP-07 | `estudianteRecibeDiezPorCiento` | Alternativo |
| CP-08 | `totalCeroVipDevuelveCero` | Límite |
| CP-09 | `totalNegativoEsInvalido` | Inválido |

Decisiones tomadas:

- Se conservó la prueba original del proyecto.
- El servicio se creó sin sus servicios externos (disponibilidad, repositorio y notificador), porque los dos métodos probados no los usan. La confirmación de reservas sí los necesita, y se trabajará en el Laboratorio 2.
- En el caso inválido se revisa el tipo de error y también su mensaje, para asegurar que el rechazo es el esperado.

## 4. Resultado de la ejecución

Con `mvn clean test` pasaron las 10 pruebas (las 9 nuevas y la original), sin fallos.
Evidencia: [`evidencias/lab1/03_mvn_clean_test_final.txt`](evidencias/lab1/03_mvn_clean_test_final.txt)

## 5. Microexperimento: introducir un error a propósito

Para comprobar que las pruebas detectan errores reales, se cambió por un momento la regla de cancelación de "2 horas o más" a "más de 2 horas":

```java
return horasAnticipacion > 2;   // original: >= 2
```

Al volver a ejecutar, falló una sola prueba, `dosHorasEsElLimitePermitido`: esperaba poder cancelar con 2 horas y el sistema lo negó. Después se restauró el código original, se comprobó que todo volvía a pasar y el cambio no se guardó en el historial.
Evidencia: [`evidencias/lab1/02_microexperimento_frontera.txt`](evidencias/lab1/02_microexperimento_frontera.txt)

## 6. ¿Qué prueba detectó mejor un posible error de frontera y por qué?

Fue `dosHorasEsElLimitePermitido` (CP-02), la única que falló. Las pruebas con 5, 1 y 0 horas siguieron pasando porque en esos valores las dos versiones de la regla dan la misma respuesta.

Las dos versiones solo se diferencian en las 2 horas exactas. Por eso un caso ubicado en el límite vale más que muchos casos comunes: sin él, el error habría pasado con todas las pruebas en verde.

## 7. Checklist

- [x] Puedo explicar cada caso de la matriz.
- [x] Incluí casos límite (CP-02, CP-03, CP-08).
- [x] Incluí un caso inválido (CP-09).
- [x] Las pruebas tienen nombres descriptivos.
- [x] Las pruebas pasan con `mvn clean test`.
- [x] No cambié ningún resultado esperado solo para que la prueba pasara.

### Producto formativo

- [x] Matriz con al menos 8 casos (se diseñaron 9).
- [x] Al menos 7 pruebas implementadas (se implementaron 9).
- [x] Evidencia de `mvn clean test` exitoso.
- [x] Respuesta sobre la prueba que mejor detecta un error de frontera.

# Análisis de cobertura

El reporte se generó con JaCoCo al ejecutar `mvn clean test` y se revisó en `target/site/jacoco/index.html`. Se hicieron dos mediciones: una con las pruebas de los laboratorios 1 y 2, y otra después de agregar las pruebas que surgieron del análisis.

## Resultado observado

| Métrica (todo el proyecto) | Antes | Después |
|---|---|---|
| Instrucciones cubiertas | 87 % (121 de 138) | 91 % (126 de 138) |
| Ramas cubiertas | 83 % (15 de 18) | 94 % (17 de 18) |
| Líneas sin cubrir | 5 de 39 | 4 de 39 |
| Pruebas ejecutadas | 13 | 15 |

| Clase | Ramas antes | Ramas después | Comentario |
|---|---|---|---|
| `ReservaService` | 12 de 12 (100 %) | 12 de 12 (100 %) | Todas las decisiones del servicio se ejecutan. |
| `Reserva` | 3 de 6 (50 %) | 5 de 6 (83 %) | Aquí estaba el hueco principal. |

- **Cobertura de líneas:** 34 de 39 antes y 35 de 39 después.
- **Cobertura de ramas:** 83 % antes y 94 % después.
- **Clase o método analizado:** `ReservaService` (métodos `puedeCancelar`, `calcularTotal` y `confirmar`) y el constructor de `Reserva`.

Evidencia: capturas y datos en [`evidencias/lab2/jacoco_antes/`](evidencias/lab2/jacoco_antes/) y [`evidencias/lab2/jacoco_despues/`](evidencias/lab2/jacoco_despues/).

## Huecos relevantes

1. **La regla del id obligatorio nunca se probaba.** En la primera medición, la línea que rechaza una reserva sin id aparecía en rojo: ninguna prueba intentaba crear una reserva sin identificador. Es un hueco importante, porque una reserva sin id no se puede guardar ni consultar después.
2. **El tipo por defecto sigue sin probarse.** Cuando una reserva se crea sin tipo, el sistema le asigna "NORMAL". Esa rama sigue en amarillo, porque todas las pruebas crean reservas con un tipo definido.
3. **Métodos sin uso en las pruebas.** `cancelar()`, `getId()` y `getTipo()` de `Reserva` no se ejecutan. `cancelar()` todavía no la usa ninguna regla del servicio, así que su valor es bajo por ahora.

## Código cubierto, pero no del todo protegido

`ReservaService` tiene 100 % de cobertura, pero eso no significa que cada comportamiento esté bien comprobado. Por ejemplo:

- Los descuentos aceptan el tipo en minúsculas (`"vip"`), pero ninguna prueba lo verifica. La línea aparece en verde porque se ejecuta con `"VIP"`.
- No se comprueba qué pasa si un cliente VIP tiene un total negativo, es decir, que el error salte antes de aplicar el descuento.
- En la confirmación se verifica que la reserva se guarde y se notifique, pero no el orden. Si alguien invirtiera esos pasos, se podría avisar al cliente de una reserva que aún no está guardada, y la cobertura seguiría en 100 %.

La cobertura indica qué líneas se ejecutaron, no si las pruebas revisan lo correcto. Por eso se usa como guía para buscar huecos y no como meta.

## Decisiones

- **¿Qué prueba nueva se añadió?** Dos pruebas en `ReservaTest`: `reservaSinIdEsRechazada` (CP-13) y `reservaConIdEnBlancoEsRechazada` (CP-14). La validación tiene dos condiciones, que el id no sea nulo y que no esté en blanco, y cada prueba comprueba una.
- **¿Qué riesgo protege?** Que se creen reservas sin identificador. Si alguien quitara o debilitara esa validación, ahora una prueba fallaría.
- **¿Por qué no basta con el porcentaje?** Porque el servicio ya tenía 100 % y aun así tiene comportamientos sin verificar (minúsculas, orden de los pasos). Y el hueco real no estaba en el servicio sino en la clase `Reserva`, algo que solo se ve al revisar el reporte clase por clase.

## Próximas pruebas sugeridas

| Comportamiento | Prueba propuesta |
|---|---|
| Tipo por defecto | Crear una reserva sin tipo y comprobar que queda como "NORMAL". |
| Descuento con minúsculas | `calcularTotal("vip", 100)` debe devolver 85. |
| Validación antes del descuento | `calcularTotal("VIP", -1)` debe lanzar el error. |
| Orden al confirmar | Verificar que primero se guarda y después se notifica. |

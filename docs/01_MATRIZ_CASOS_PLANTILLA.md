# Matriz de casos

Los casos se diseñaron antes de escribir las pruebas. Cada uno parte de una regla del sistema de reservas y señala qué error ayudaría a detectar.

La matriz creció en tres etapas: CP-01 a CP-09 en el Laboratorio 1, CP-10 a CP-14 en el Laboratorio 2 y CP-15 en adelante en Ae6.

## Reglas analizadas

| Método | Regla | Qué podría salir mal |
|---|---|---|
| `puedeCancelar(int)` | Una reserva se puede cancelar con 2 horas o más de anticipación. | Que el límite de 2 horas quede mal definido. |
| `calcularTotal(String, double)` | VIP tiene 15 % de descuento, ESTUDIANTE 10 % y NORMAL ninguno. No se aceptan totales negativos. | Que se aplique un descuento equivocado o se acepte un monto negativo. |
| `confirmar(Reserva)` | Una reserva solo se confirma si hay disponibilidad. Al confirmarse se guarda y se notifica al cliente. | Guardar o avisar de una reserva que no se pudo confirmar. |

## Regla de cancelación

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-01 | Cancelar con 2 h o más | Anticipación habitual | 5 | `true` | Normal | Rechazar una cancelación válida. |
| CP-02 | Cancelar con 2 h o más | Justo en el límite | 2 | `true` | Límite | Que el límite excluya las 2 horas. |
| CP-03 | Cancelar con 2 h o más | Debajo del límite | 1 | `false` | Límite | Permitir una cancelación tardía. |
| CP-04 | Cancelar con 2 h o más | Sin anticipación | 0 | `false` | Extremo | Permitir una cancelación de último momento. |
| CP-15 | Cancelar con 2 h o más | Horas negativas (la reserva ya pasó) | -1 | `false` | Inválido | Permitir cancelar una reserva que ya ocurrió. |

## Regla de descuentos

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-05 | NORMAL sin descuento | Cliente normal | `NORMAL`, 100 | 100.0 | Normal | Descontar a quien no le corresponde. |
| CP-06 | VIP 15 % | Cliente VIP | `VIP`, 100 | 85.0 | Alternativo | Aplicar mal el porcentaje VIP. |
| CP-07 | ESTUDIANTE 10 % | Cliente estudiante | `ESTUDIANTE`, 100 | 90.0 | Alternativo | Aplicar mal el porcentaje de estudiante. |
| CP-08 | Total no negativo | Total cero | `VIP`, 0 | 0.0 | Límite | Rechazar el menor total permitido. |
| CP-09 | Total no negativo | Total negativo | `NORMAL`, -1 | Error: "Total base inválido" | Inválido | Aceptar un cobro negativo. |
| CP-16 | VIP 15 % | Tipo escrito en minúsculas | `vip`, 100 | 85.0 | Alternativo | Negar el descuento por cómo se escribió el tipo. |
| CP-17 | NORMAL sin descuento | Tipo no reconocido | `CORPORATIVO`, 100 | 100.0 | Alternativo | Dar un descuento a un tipo que no lo tiene. |
| CP-18 | Total no negativo | VIP con total negativo | `VIP`, -1 | Error: "Total base inválido" | Excepción | Aplicar el descuento antes de validar el monto. |

## Regla de confirmación

Esta regla depende de tres servicios externos: el de disponibilidad, el repositorio donde se guardan las reservas y el de notificaciones. En las pruebas se reemplazan por dobles de prueba, para no depender de una base de datos, de un servicio real ni de un servidor de correo.

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-10 | Confirmar solo con disponibilidad | Horario disponible | Reserva R-001, disponibilidad: sí | Reserva CONFIRMADA, guardada y notificada | Normal | Confirmar sin guardar o sin avisar al cliente. |
| CP-11 | Confirmar solo con disponibilidad | Horario no disponible | Reserva R-002, disponibilidad: no | Error "Horario no disponible"; no se guarda ni se notifica | Alternativo | Guardar o avisar de una reserva rechazada. |
| CP-12 | La reserva es obligatoria | Reserva nula | `null` | Error "Reserva obligatoria"; no se consulta ningún servicio | Excepción | Consultar servicios externos con datos incompletos. |
| CP-19 | Guardar antes de avisar | Orden de los pasos al confirmar | Reserva R-003, disponibilidad: sí | Primero se guarda y después se notifica | Normal | Avisar al cliente de una reserva que todavía no está guardada. |

## Regla de la reserva (agregada tras el análisis de JaCoCo)

El reporte de cobertura mostró que la validación del identificador de la reserva nunca se ejecutaba en las pruebas. Se agregaron estos casos para protegerla.

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-13 | El id es obligatorio | Reserva sin id | id `null` | Error "Id obligatorio" | Inválido | Crear reservas imposibles de identificar. |
| CP-14 | El id es obligatorio | Id en blanco | id `"   "` | Error "Id obligatorio" | Inválido | Aceptar un id que solo tiene espacios. |

## Por qué se eligió cada caso

- **CP-01:** confirma que la regla funciona en una situación común.
- **CP-02:** es el caso más importante de la regla, porque 2 horas es justo el punto donde cambia la respuesta. Si el límite estuviera mal escrito, solo este caso lo notaría.
- **CP-03:** comprueba el primer valor que ya no debe permitir cancelar.
- **CP-04:** comprueba el caso extremo de no tener anticipación.
- **CP-05:** confirma que un cliente normal paga el precio completo.
- **CP-06 y CP-07:** comprueban cada descuento. Se usa 100 como monto porque así el porcentaje se lee directamente en el resultado.
- **CP-08:** 0 es el menor total válido, y aun con descuento VIP el resultado debe seguir siendo 0.
- **CP-09:** confirma que un monto negativo se rechaza con un mensaje claro y no se calcula en silencio.
- **CP-10:** comprueba el camino exitoso completo: la reserva cambia de estado, se guarda y el cliente recibe el aviso.
- **CP-11:** comprueba que una reserva rechazada no deja rastros: ni se guarda ni se envía una confirmación falsa.
- **CP-12:** comprueba que el proceso se detiene de inmediato si falta la reserva, antes de consultar cualquier servicio.
- **CP-13 y CP-14:** la validación del id tiene dos condiciones (que no sea nulo y que no esté en blanco), y cada caso comprueba una de ellas.
- **CP-15:** un valor negativo no tiene sentido como anticipación. El caso confirma que no abre una puerta para cancelar.
- **CP-16:** la regla acepta el tipo sin importar mayúsculas o minúsculas. La cobertura marcaba esa línea en verde, pero ninguna prueba lo comprobaba.
- **CP-17:** confirma que cualquier tipo distinto de VIP o ESTUDIANTE paga el precio completo.
- **CP-18:** confirma que el monto se valida antes de aplicar cualquier descuento, incluso para clientes VIP.
- **CP-19:** guardar y avisar ya se verificaban, pero no su orden. Si se invirtieran, el cliente podría recibir la confirmación de una reserva que no quedó registrada.

## Resumen

| Tipo | Casos | Cantidad |
|---|---|---|
| Normal | CP-01, CP-05, CP-10, CP-19 | 4 |
| Alternativo | CP-06, CP-07, CP-11, CP-16, CP-17 | 5 |
| Límite y extremo | CP-02, CP-03, CP-04, CP-08 | 4 |
| Inválido | CP-09, CP-13, CP-14, CP-15 | 4 |
| Excepción | CP-12, CP-18 | 2 |
| **Total** | | **19** |

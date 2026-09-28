# Matriz de casos

Los casos se diseñaron antes de escribir las pruebas. Cada uno parte de una regla del sistema de reservas y señala qué error ayudaría a detectar.

## Reglas analizadas

| Método | Regla | Qué podría salir mal |
|---|---|---|
| `puedeCancelar(int)` | Una reserva se puede cancelar con 2 horas o más de anticipación. | Que el límite de 2 horas quede mal definido. |
| `calcularTotal(String, double)` | VIP tiene 15 % de descuento, ESTUDIANTE 10 % y NORMAL ninguno. No se aceptan totales negativos. | Que se aplique un descuento equivocado o se acepte un monto negativo. |

## Regla de cancelación

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-01 | Cancelar con 2 h o más | Anticipación habitual | 5 | `true` | Normal | Rechazar una cancelación válida. |
| CP-02 | Cancelar con 2 h o más | Justo en el límite | 2 | `true` | Límite | Que el límite excluya las 2 horas. |
| CP-03 | Cancelar con 2 h o más | Debajo del límite | 1 | `false` | Límite | Permitir una cancelación tardía. |
| CP-04 | Cancelar con 2 h o más | Sin anticipación | 0 | `false` | Extremo | Permitir una cancelación de último momento. |

## Regla de descuentos

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-05 | NORMAL sin descuento | Cliente normal | `NORMAL`, 100 | 100.0 | Normal | Descontar a quien no le corresponde. |
| CP-06 | VIP 15 % | Cliente VIP | `VIP`, 100 | 85.0 | Alternativo | Aplicar mal el porcentaje VIP. |
| CP-07 | ESTUDIANTE 10 % | Cliente estudiante | `ESTUDIANTE`, 100 | 90.0 | Alternativo | Aplicar mal el porcentaje de estudiante. |
| CP-08 | Total no negativo | Total cero | `VIP`, 0 | 0.0 | Límite | Rechazar el menor total permitido. |
| CP-09 | Total no negativo | Total negativo | `NORMAL`, -1 | Error: "Total base inválido" | Inválido | Aceptar un cobro negativo. |

## Por qué se eligió cada caso

- **CP-01:** confirma que la regla funciona en una situación común.
- **CP-02:** es el caso más importante de la regla, porque 2 horas es justo el punto donde cambia la respuesta. Si el límite estuviera mal escrito, solo este caso lo notaría.
- **CP-03:** comprueba el primer valor que ya no debe permitir cancelar.
- **CP-04:** comprueba el caso extremo de no tener anticipación.
- **CP-05:** confirma que un cliente normal paga el precio completo.
- **CP-06 y CP-07:** comprueban cada descuento. Se usa 100 como monto porque así el porcentaje se lee directamente en el resultado.
- **CP-08:** 0 es el menor total válido, y aun con descuento VIP el resultado debe seguir siendo 0.
- **CP-09:** confirma que un monto negativo se rechaza con un mensaje claro y no se calcula en silencio.

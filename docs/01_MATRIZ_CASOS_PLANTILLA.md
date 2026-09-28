# Matriz de casos

Casos diseñados antes de implementar las pruebas. Cada caso se relaciona con una regla de `ReservaService` y con el riesgo que protege.

## Reglas analizadas

| Método | Regla | Riesgo principal |
|---|---|---|
| `puedeCancelar(int)` | Se permite cancelar con 2 o más horas de anticipación. | Error en la frontera `>= 2`. |
| `calcularTotal(String, double)` | VIP 15 % de descuento, ESTUDIANTE 10 %, NORMAL sin descuento; total negativo inválido. | Reglas alternativas mal aplicadas y aceptación de totales negativos. |

## Regla de cancelación

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-01 | Cancelar con >= 2 h | Anticipación habitual | 5 | `true` | Normal | Negar una cancelación legítima. |
| CP-02 | Cancelar con >= 2 h | Límite permitido | 2 | `true` | Límite | Uso incorrecto de `>` en lugar de `>=`. |
| CP-03 | Cancelar con >= 2 h | Debajo del límite | 1 | `false` | Límite | Permitir una cancelación tardía. |
| CP-04 | Cancelar con >= 2 h | Sin anticipación | 0 | `false` | Extremo | Permitir una cancelación inmediata. |

## Regla de descuentos

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-05 | NORMAL sin descuento | Cliente normal | `NORMAL`, 100 | 100.0 | Normal | Aplicar un descuento indebido. |
| CP-06 | VIP 15 % | Cliente VIP | `VIP`, 100 | 85.0 | Alternativo | Porcentaje VIP incorrecto. |
| CP-07 | ESTUDIANTE 10 % | Cliente estudiante | `ESTUDIANTE`, 100 | 90.0 | Alternativo | Porcentaje de estudiante incorrecto. |
| CP-08 | Total base no negativo | Total cero | `VIP`, 0 | 0.0 | Límite | Rechazar el menor total válido. |
| CP-09 | Total base no negativo | Total negativo | `NORMAL`, -1 | `IllegalArgumentException` ("Total base inválido") | Inválido | Calcular cobros con montos negativos. |

## Justificación de los casos

- **CP-01** comprueba la regla general con un valor claramente dentro del rango permitido.
- **CP-02** es el caso más valioso de la regla: detecta si la condición se escribe con `>` en lugar de `>=`.
- **CP-03** protege la frontera inferior: el primer valor entero que ya no debe permitir cancelar.
- **CP-04** representa el extremo de la regla: una cancelación sin anticipación no debe aceptarse.
- **CP-05** confirma que el tipo sin beneficio conserva el total original.
- **CP-06** y **CP-07** cubren cada regla alternativa de descuento con un monto que hace evidente el porcentaje (100).
- **CP-08** es el límite de la validación: 0 es el menor total válido y, además, VIP sobre 0 no debe producir valores extraños.
- **CP-09** verifica la entrada inválida y el mensaje de error, para que el rechazo sea explícito y no un resultado silencioso.

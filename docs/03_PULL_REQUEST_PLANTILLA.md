# test: completar suite, cobertura y evidencia de Ae6

**Base:** `main` · **Rama:** `ae6/suite-pruebas`

## Objetivo
Proteger las reglas principales del módulo de reservas con una suite de pruebas clara y fácil de repetir, y usar el reporte de cobertura para encontrar lo que todavía faltaba comprobar.

## Cambios realizados
- Suite reorganizada por regla de negocio (cancelación, descuentos y confirmación). Cada prueba muestra el número de caso de la matriz. No se eliminó ninguna prueba de los laboratorios.
- 6 casos nuevos (CP-15 a CP-20): horas negativas, tipo en minúsculas, tipo no reconocido, VIP con total negativo, orden entre guardar y notificar, y tipo NORMAL por defecto.
- Matriz completa con 20 casos, análisis de cobertura con cuatro mediciones y README actualizado.
- Evidencias de ejecución, capturas de JaCoCo y un experimento que demuestra el valor de CP-19.

## Casos de prueba
| Caso | Prueba | Tipo |
|---|---|---|
| CP-15 | `horasNegativasNoPermitenCancelar` | Inválido |
| CP-16 | `vipEnMinusculasRecibeQuincePorCiento` | Alternativo |
| CP-17 | `tipoNoReconocidoNoRecibeDescuento` | Alternativo |
| CP-18 | `vipConTotalNegativoEsInvalido` | Excepción |
| CP-19 | `reservaSeGuardaAntesDeNotificar` | Normal |
| CP-20 | `reservaSinTipoQuedaComoNormalYPendiente` | Alternativo |

Los casos CP-01 a CP-14 vienen de los laboratorios 1 y 2. La matriz completa está en `docs/01_MATRIZ_CASOS_PLANTILLA.md`.

### Uso de Stub y Mock
- **Disponibilidad (Stub):** se decide de antemano si hay horario, para probar los dos caminos de la confirmación sin un servicio real.
- **Repositorio y notificador (Mocks):** se verifica que la reserva se guarde y se notifique solo cuando corresponde, y en ese orden. Estos servicios no devuelven ningún valor, así que la única forma de comprobarlos es revisar si fueron llamados.
- **Reserva:** se usa el objeto real, porque es simple y no depende de nada externo.

## Cómo verificar
```bash
mvn clean test
```
Deben pasar 21 pruebas sin fallos (`BUILD SUCCESS`). El reporte de cobertura queda en `target/site/jacoco/index.html`.

## Cobertura
| Métrica | Inicio de Ae6 | Final |
|---|---|---|
| Instrucciones | 91 % | 94 % |
| Ramas | 94 % | 100 % |

- El reporte mostró una sola rama sin comprobar: el tipo "NORMAL" por defecto. Se agregó CP-20 para cubrirla.
- CP-15 a CP-19 no cambiaron el porcentaje, pero protegen comportamientos que la cobertura no distinguía. Por ejemplo, al invertir por un momento el orden entre guardar y notificar, solo CP-19 detectó el error.
- Análisis completo en `docs/02_ANALISIS_COBERTURA_PLANTILLA.md`.

## Limitaciones
- `cancelar()` y `getId()` de `Reserva` no tienen pruebas: ninguna regla del servicio los usa todavía, y probarlos solo subiría el porcentaje.
- Las pruebas usan dobles para los servicios externos, así que no comprueban la conexión con una base de datos o un servicio de correo reales.
- Al ejecutar aparece un aviso de Java sobre la carga de un agente, generado por Mockito. No afecta los resultados.

## Autorrevisión
- [ ] Compila
- [ ] Pruebas en verde
- [ ] Sin archivos accidentales
- [ ] Commits descriptivos
- [ ] Documentación actualizada

## Uso de IA
Se utilizó Claude (Anthropic) como asistente para proponer casos de prueba, redactar la documentación y revisar el código de las pruebas. Todas las pruebas se ejecutaron localmente, y los resultados, el análisis de cobertura y las decisiones fueron revisados y validados por el estudiante.

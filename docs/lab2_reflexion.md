# Laboratorio 2 | Stub, Mock, JaCoCo y Git

**Estudiante:** Justin Arreaga Ramírez
**Rama:** `test/lab2-dobles-cobertura`

## 1. Punto de partida

El laboratorio continúa sobre el resultado del Laboratorio 1, ya integrado en `main`. Antes de empezar se comprobó que las 10 pruebas existentes pasaban y se creó una rama propia para este trabajo.
Evidencia: [`evidencias/lab2/01_punto_partida.txt`](evidencias/lab2/01_punto_partida.txt)

## 2. El reto: probar la confirmación de reservas

Para confirmar una reserva, el sistema depende de tres servicios externos:

| Servicio | Qué hace en la realidad |
|---|---|
| Disponibilidad | Consulta si el horario está libre. |
| Repositorio | Guarda la reserva en la base de datos. |
| Notificador | Envía la confirmación al cliente. |

Usar los servicios reales haría las pruebas lentas, dependientes de la red y difíciles de repetir. Por eso se reemplazaron por dobles de prueba creados con Mockito. Con esto también se dejó de crear el servicio con valores vacíos (`null`), como se había hecho en el Laboratorio 1.

## 3. Uso de Stub y Mock

| Servicio | Rol en la prueba | Cómo se usa | Por qué |
|---|---|---|---|
| Disponibilidad | **Stub** | `when(...).thenReturn(true/false)` | Permite decidir de antemano si hay horario, para probar los dos caminos sin depender de un servicio real. |
| Repositorio | **Mock** | `verify(...)` y `verify(..., never())` | Guardar no devuelve ningún resultado, así que la única forma de comprobarlo es revisar si se llamó. |
| Notificador | **Mock** | `verify(...)` y `verify(..., never())` | Es lo que ve el cliente: hay que asegurar que solo reciba aviso cuando la reserva se confirmó. |

La reserva en sí no se reemplazó por un doble: es un objeto simple y se prueba con una instancia real.

## 4. Pruebas implementadas

| Caso | Prueba | Qué comprueba |
|---|---|---|
| CP-10 | `reservaDisponibleSeConfirmaGuardaYNotifica` | Con horario disponible, la reserva queda confirmada, se guarda y se avisa al cliente. |
| CP-11 | `reservaNoDisponibleNoSeGuardaNiNotifica` | Sin horario, se muestra el error, la reserva sigue pendiente y no se guarda ni se notifica. |
| CP-12 | `reservaNulaNoConsultaDependencias` | Si falta la reserva, el proceso se detiene antes de consultar cualquier servicio. |
| CP-13 | `reservaSinIdEsRechazada` | No se puede crear una reserva sin id (surgió del análisis de JaCoCo). |
| CP-14 | `reservaConIdEnBlancoEsRechazada` | No se acepta un id que solo tiene espacios (surgió del análisis de JaCoCo). |

`any()` se usa en el Stub para responder igual a cualquier reserva, y con `never()` para confirmar que un servicio no se llamó con ningún valor.

## 5. Resultado de la ejecución

Con `mvn clean test` pasaron las 15 pruebas (13 del servicio y 2 de la reserva), sin fallos.
Evidencia: [`evidencias/lab2/03_mvn_clean_test_final.txt`](evidencias/lab2/03_mvn_clean_test_final.txt)

La salida muestra un aviso de Java sobre la carga de un agente (`A Java agent has been loaded dynamically`). Lo genera Mockito al preparar los dobles de prueba en Java 21. No afecta los resultados y no es un error.

## 6. Cobertura con JaCoCo

| Métrica | Antes | Después |
|---|---|---|
| Instrucciones | 87 % | 91 % |
| Ramas | 83 % | 94 % |

El servicio de reservas ya tenía 100 % de cobertura. El hueco estaba en la clase `Reserva`: la regla que exige un id nunca se probaba. A partir de eso se agregaron CP-13 y CP-14.

Aun así, quedan comportamientos sin proteger, como el tipo "NORMAL" por defecto o el orden entre guardar y notificar. Esto muestra que un porcentaje alto no garantiza que todo esté bien probado.
Análisis completo: [`02_ANALISIS_COBERTURA_PLANTILLA.md`](02_ANALISIS_COBERTURA_PLANTILLA.md)

## 7. Historial Git

```
f732154 docs: analizar cobertura jacoco
30d4ddd test: agregar caso detectado por jacoco
d00d71c test: cubrir flujo sin disponibilidad y reserva nula
3e2bfff test: agregar escenarios con stub y mock
566fa20 Merge pull request #1 from justinjw93/test/lab1-junit-casos
```

Cada commit tiene un solo propósito, así que el historial muestra el orden del trabajo: primero las pruebas con dobles, luego el análisis de cobertura y al final la mejora que surgió de ese análisis.
Evidencia: [`evidencias/lab2/04_git_log.txt`](evidencias/lab2/04_git_log.txt)

## 8. ¿Qué diferencia práctica se observó entre Stub y Mock?

El **Stub** se usa para *preparar* la prueba: define qué responde un servicio externo, por ejemplo "sí hay disponibilidad", para llevar al sistema por el camino que se quiere probar. No se revisa después.

El **Mock** se usa para *comprobar* el resultado: después de ejecutar la acción, se revisa si el sistema llamó (o no llamó) a un servicio. En este caso, si guardó la reserva y si avisó al cliente.

En pocas palabras, el Stub responde a lo que el sistema pregunta y el Mock revisa lo que el sistema hizo. En `confirmar()` se necesitan los dos: la disponibilidad decide el camino (Stub), y guardar y notificar son los efectos que hay que verificar (Mock), porque no devuelven ningún valor que se pueda comparar.

## 9. Checklist

- [x] No uso mocks sin una razón clara.
- [x] Sé explicar qué respuesta controla mi Stub.
- [x] Sé explicar qué interacción verifica cada Mock.
- [x] Las pruebas pasan.
- [x] JaCoCo genera el reporte.
- [x] Puedo señalar al menos un comportamiento aún no protegido (tipo por defecto, orden al confirmar).
- [x] Mi rama y mis commits permiten reconstruir el trabajo.

### Producto formativo

- [x] Al menos 3 pruebas de `confirmar()` con Mockito.
- [x] Uso de `when(...).thenReturn(...)`.
- [x] Uso de `verify(...)`, `never()` y `any()`.
- [x] Reporte de JaCoCo generado (capturas en `evidencias/lab2/`).
- [x] Análisis escrito de al menos un hueco de cobertura.
- [x] Rama Git específica y al menos 3 commits descriptivos.
- [x] Respuesta sobre la diferencia práctica entre Stub y Mock.

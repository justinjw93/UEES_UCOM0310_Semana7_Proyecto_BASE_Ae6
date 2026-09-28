# Análisis de cobertura

El reporte se generó con JaCoCo al ejecutar `mvn clean test` y se revisó en `target/site/jacoco/index.html`. Se analizaron las clases `ReservaService` y `Reserva` en cuatro momentos del trabajo, para ver no solo el porcentaje sino qué comportamientos quedaban sin comprobar.

## Evolución de la cobertura

| Momento | Pruebas | Instrucciones | Ramas | Líneas sin cubrir |
|---|---|---|---|---|
| Laboratorio 2, antes del análisis | 13 | 87 % (121/138) | 83 % (15/18) | 5 de 39 |
| Laboratorio 2, después del análisis (CP-13, CP-14) | 15 | 91 % (126/138) | 94 % (17/18) | 4 de 39 |
| Ae6, con CP-15 a CP-19 | 20 | 91 % (126/138) | 94 % (17/18) | 4 de 39 |
| Ae6, después del análisis (CP-20) | 21 | 94 % (131/138) | 100 % (18/18) | 3 de 39 |

Evidencia: [`evidencias/lab2/`](evidencias/lab2/) y [`evidencias/ae6/`](evidencias/ae6/) (capturas del reporte y archivos `jacoco.csv` de cada medición).

## Resultado observado

- **Cobertura de líneas:** 36 de 39 (92 %).
- **Cobertura de ramas:** 18 de 18 (100 %).
- **Clase o método analizado:**

| Clase | Instrucciones | Ramas | Métodos sin ejecutar |
|---|---|---|---|
| `ReservaService` | 100 % | 100 % (12/12) | Ninguno |
| `Reserva` | 83 % | 100 % (6/6) | `cancelar()` y `getId()` |

## Huecos relevantes

1. **La validación del id nunca se probaba (Laboratorio 2).** La línea que rechaza una reserva sin identificador aparecía en rojo. Se resolvió con CP-13 y CP-14.
2. **El tipo por defecto no se comprobaba (Ae6).** Al iniciar Ae6, la única rama incompleta era la que asigna "NORMAL" cuando una reserva llega sin tipo: todas las pruebas creaban reservas con un tipo definido. Se resolvió con CP-20.
3. **`cancelar()` y `getId()` siguen sin ejecutarse.** Se decidió **no** agregar pruebas para ellos por ahora. Ninguna regla del servicio cancela reservas ni usa el id, así que una prueba solo serviría para subir el porcentaje, sin proteger ningún comportamiento del negocio. Cuando exista una regla de cancelación en el servicio, sus pruebas cubrirán ese método de forma natural.

## Lo que la cobertura no mostraba

Al terminar el Laboratorio 2, `ReservaService` ya tenía 100 % de cobertura. Aun así, se identificaron comportamientos importantes sin comprobar y se agregaron en Ae6:

| Caso | Comportamiento | Por qué la cobertura no lo detectaba |
|---|---|---|
| CP-15 | Horas negativas no permiten cancelar | La condición ya se ejecutaba con otros valores. |
| CP-16 | El descuento VIP funciona con el tipo en minúsculas | La línea se ejecutaba con `"VIP"`, en mayúsculas. |
| CP-17 | Un tipo no reconocido paga el precio completo | El camino sin descuento ya se recorría con `"NORMAL"`. |
| CP-18 | Un VIP con total negativo es rechazado | La validación ya se ejecutaba con un cliente NORMAL. |
| CP-19 | La reserva se guarda antes de avisar al cliente | Las dos líneas se ejecutaban; nadie revisaba su orden. |

Estas cinco pruebas **no cambiaron el porcentaje**: la cobertura se mantuvo exactamente igual (91 % de instrucciones y 94 % de ramas). Sin embargo, sí aumentaron la protección de la suite.

Para comprobarlo, se invirtió por un momento el orden entre guardar y notificar en `confirmar()`. Con ese error, la cobertura seguía siendo la misma, CP-10 (que verifica que ambas cosas ocurran) seguía pasando y **solo CP-19 falló**. Después se restauró el código.
Evidencia: [`evidencias/ae6/02_experimento_orden_confirmacion.txt`](evidencias/ae6/02_experimento_orden_confirmacion.txt)

## Decisiones

- **¿Qué prueba nueva se añadió?** A partir del reporte: CP-13 y CP-14 en el Laboratorio 2 (id obligatorio) y CP-20 en Ae6 (tipo NORMAL por defecto). A partir de revisar las reglas más allá del reporte: CP-15 a CP-19.
- **¿Qué riesgo protege?** Que se creen reservas sin id o sin tarifa definida, que se apliquen descuentos de forma incorrecta y que el cliente reciba la confirmación de una reserva que no quedó guardada.
- **¿Por qué no basta con el porcentaje?** Porque el porcentaje mide qué líneas se ejecutaron, no si las pruebas revisan el resultado correcto. En este proyecto, cinco pruebas valiosas no movieron el porcentaje, y una de ellas (CP-19) fue la única capaz de detectar un error real. Por eso la cobertura se usó como guía para encontrar huecos, no como meta.

## Limitaciones

- `cancelar()` y `getId()` no tienen pruebas, por la razón explicada en los huecos relevantes.
- JaCoCo no mide la calidad de las comprobaciones: una prueba sin verificaciones útiles también suma cobertura.
- Las pruebas usan dobles de prueba para los servicios externos, así que no comprueban la integración con una base de datos o un servicio de correo reales.

# Laboratorio 1 | Diseño de casos y JUnit 5

**Estudiante:** Justin Arreaga Ramírez
**Rama:** `test/lab1-junit-casos`

## 1. Línea base

Antes de modificar la suite se verificó el entorno y se ejecutó la prueba mínima `entornoJUnitFunciona()`.

| Herramienta | Versión |
|---|---|
| Java | OpenJDK 21.0.12 (Microsoft) |
| Maven | 3.9.16 |
| Git | 2.45.1 |

Resultado: `Tests run: 1, Failures: 0` y `BUILD SUCCESS`.
Evidencia: [`evidencias/lab1/01_linea_base.txt`](evidencias/lab1/01_linea_base.txt)

## 2. Matriz de casos

Se diseñaron 9 casos (CP-01 a CP-09) antes de escribir cualquier `@Test`: 4 para la regla de cancelación y 5 para la regla de descuentos. La matriz incluye casos normales, alternativos, límite, extremo e inválido.
Detalle y justificación: [`01_MATRIZ_CASOS_PLANTILLA.md`](01_MATRIZ_CASOS_PLANTILLA.md)

## 3. Suite implementada

Todas las pruebas siguen la estructura AAA (Arrange, Act, Assert) y tienen nombres que describen el comportamiento esperado.

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

Se conservó `entornoJUnitFunciona()`. El servicio se construye con `new ReservaService(null, null, null)` porque `puedeCancelar()` y `calcularTotal()` no consultan colaboradores. Esta decisión solo es válida para estos dos métodos; `confirmar()` requiere dobles de prueba y se abordará en el Laboratorio 2.

Los valores decimales se comparan con una tolerancia de `0.001` para evitar falsos fallos por redondeo de `double`. En la excepción se verifica también el mensaje `"Total base inválido"`, no solo el tipo.

## 4. Ejecución

```
mvn clean test
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Evidencia: [`evidencias/lab1/03_mvn_clean_test_final.txt`](evidencias/lab1/03_mvn_clean_test_final.txt)

## 5. Microexperimento: detectar un bug de frontera

Se cambió temporalmente la regla de `ReservaService.puedeCancelar()`:

```java
return horasAnticipacion > 2;   // original: >= 2
```

Resultado de `mvn test`:

```
Tests run: 10, Failures: 1, Errors: 0, Skipped: 0
ReservaServiceTest.dosHorasEsElLimitePermitido:48 expected: <true> but was: <false>
BUILD FAILURE
```

Luego se restauró el código con `git restore` y se volvió a ejecutar la suite completa en verde. El cambio no se registró en ningún commit.
Evidencia: [`evidencias/lab1/02_microexperimento_frontera.txt`](evidencias/lab1/02_microexperimento_frontera.txt)

## 6. Respuesta: ¿qué prueba detectó mejor un posible error de frontera y por qué?

La prueba `dosHorasEsElLimitePermitido()` (CP-02). Fue la **única** de las diez que falló al cambiar `>=` por `>`. Las pruebas con 5, 1 y 0 horas siguieron pasando porque esos valores producen el mismo resultado con ambos operadores: 5 es mayor que 2 en los dos casos, y 1 y 0 no lo son en ninguno.

El valor 2 es el único punto donde `>=` y `>` responden distinto, por eso un caso ubicado exactamente en la frontera es más valioso que repetir muchos valores normales. Si la suite solo tuviera casos alejados del límite, el defecto habría pasado desapercibido con todas las pruebas en verde.

## 7. Checklist

- [x] Puedo explicar cada caso de mi matriz.
- [x] Incluí al menos un caso límite (CP-02, CP-03, CP-08).
- [x] Incluí al menos un caso inválido (CP-09).
- [x] Mis pruebas tienen nombres descriptivos.
- [x] Las pruebas pasan con `mvn clean test`.
- [x] No alteré el resultado esperado solo para obtener verde.

### Producto formativo

- [x] Matriz con al menos 8 casos diseñados (9).
- [x] Suite JUnit con al menos 7 pruebas implementadas (9 nuevas).
- [x] Evidencia de `mvn clean test` exitoso.
- [x] Respuesta breve sobre la prueba que detecta mejor un error de frontera.

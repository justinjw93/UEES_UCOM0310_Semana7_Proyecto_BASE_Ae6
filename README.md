# UEES UCOM0310 — Semana 7 — Proyecto base Ae6

Proyecto de la Semana 7 de Diseño de Software. Contiene un servicio de reservas y la suite de pruebas desarrollada en los dos laboratorios formativos y en la actividad evaluada Ae6.

**Estudiante:** Justin Arreaga Ramírez

## Requisitos
- Java 21 (la variable `JAVA_HOME` debe apuntar a un JDK 21)
- Maven 3.9+
- Git

## Cómo ejecutar las pruebas
```bash
mvn clean test
```

Resultado esperado: `Tests run: 21, Failures: 0, Errors: 0, Skipped: 0` y `BUILD SUCCESS`.

## Cobertura
El mismo comando genera el reporte de JaCoCo. Luego abrir:

`target/site/jacoco/index.html`

Resultado actual: 94 % de instrucciones y 100 % de ramas. La interpretación está en [`docs/02_ANALISIS_COBERTURA_PLANTILLA.md`](docs/02_ANALISIS_COBERTURA_PLANTILLA.md).

## Reglas protegidas

| Regla | Pruebas |
|---|---|
| Cancelar con 2 horas o más de anticipación | CP-01 a CP-04, CP-15 |
| Descuentos VIP 15 %, ESTUDIANTE 10 %, NORMAL sin descuento; total no negativo | CP-05 a CP-09, CP-16 a CP-18 |
| Confirmar solo con disponibilidad; guardar y luego notificar | CP-10 a CP-12, CP-19 |
| Reserva con id obligatorio y tipo NORMAL por defecto | CP-13, CP-14, CP-20 |

Detalle de cada caso: [`docs/01_MATRIZ_CASOS_PLANTILLA.md`](docs/01_MATRIZ_CASOS_PLANTILLA.md)

## Estructura de las pruebas
- `src/test/java/edu/uees/testing/service/ReservaServiceTest.java`: pruebas del servicio, agrupadas por regla (cancelación, descuentos y confirmación). La disponibilidad se reemplaza por un Stub; el repositorio y el notificador, por Mocks.
- `src/test/java/edu/uees/testing/domain/ReservaTest.java`: pruebas de la clase `Reserva`, con objetos reales.

## Recorrido del trabajo

| Etapa | Rama | Documentación |
|---|---|---|
| Laboratorio 1: casos y JUnit 5 | `test/lab1-junit-casos` (PR #1) | [`docs/lab1_reflexion.md`](docs/lab1_reflexion.md) |
| Laboratorio 2: Stub, Mock, JaCoCo y Git | `test/lab2-dobles-cobertura` (PR #2) | [`docs/lab2_reflexion.md`](docs/lab2_reflexion.md) |
| Ae6: suite, cobertura y Pull Request | `ae6/suite-pruebas` (PR #3) | [`docs/03_PULL_REQUEST_PLANTILLA.md`](docs/03_PULL_REQUEST_PLANTILLA.md) |

Las salidas de ejecución y las capturas de JaCoCo de cada etapa están en [`docs/evidencias/`](docs/evidencias/).

## Regla de trabajo
No modifiques el código productivo solo para hacer pasar una prueba sin justificar el cambio.
Primero diseña el caso, luego implementa la prueba y finalmente interpreta el resultado.

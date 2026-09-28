package edu.uees.testing.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Punto de partida.
 * El estudiante debe ampliar esta suite durante las actividades.
 */
class ReservaServiceTest {

    // puedeCancelar() y calcularTotal() no consultan colaboradores,
    // por eso en este laboratorio se construye el servicio con null.
    private final ReservaService servicio = new ReservaService(null, null, null);

    @Test
    void entornoJUnitFunciona() {
        assertTrue(true);
    }

    // CP-01
    @Test
    void cincoHorasPermitenCancelar() {
        // Arrange
        int horas = 5;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertTrue(resultado);
    }

    // CP-02
    @Test
    void dosHorasEsElLimitePermitido() {
        // Arrange
        int horas = 2;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertTrue(resultado);
    }

    // CP-03
    @Test
    void unaHoraNoPermiteCancelar() {
        // Arrange
        int horas = 1;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertFalse(resultado);
    }

    // CP-04
    @Test
    void ceroHorasNoPermiteCancelar() {
        // Arrange
        int horas = 0;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertFalse(resultado);
    }
}

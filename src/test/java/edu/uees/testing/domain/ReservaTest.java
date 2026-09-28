package edu.uees.testing.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Reserva es un objeto simple del dominio: se prueba con instancias reales,
 * sin dobles de prueba.
 */
@DisplayName("Reserva")
class ReservaTest {

    @Test
    @DisplayName("CP-13 · reserva sin id es rechazada")
    void reservaSinIdEsRechazada() {
        // Arrange
        String id = null;

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Reserva(id, "NORMAL")
        );

        // Assert
        assertEquals("Id obligatorio", ex.getMessage());
    }

    @Test
    @DisplayName("CP-14 · reserva con id en blanco es rechazada")
    void reservaConIdEnBlancoEsRechazada() {
        // Arrange
        String id = "   ";

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Reserva(id, "NORMAL")
        );

        // Assert
        assertEquals("Id obligatorio", ex.getMessage());
    }
}

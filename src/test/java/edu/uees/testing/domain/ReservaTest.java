package edu.uees.testing.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Reserva es un objeto simple del dominio: se prueba con instancias reales,
 * sin dobles de prueba.
 */
class ReservaTest {

    // CP-13
    @Test
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

    // CP-14
    @Test
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

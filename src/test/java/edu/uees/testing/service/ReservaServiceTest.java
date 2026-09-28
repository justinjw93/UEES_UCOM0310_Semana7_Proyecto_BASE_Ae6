package edu.uees.testing.service;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Punto de partida.
 * El estudiante debe ampliar esta suite durante las actividades.
 */
class ReservaServiceTest {

    // JUnit crea una instancia nueva por prueba, así que cada prueba
    // recibe dobles limpios.
    // Stub: controla la respuesta de disponibilidad.
    private final DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
    // Mocks: permiten verificar que se guarda y se notifica.
    private final ReservaRepository repository = mock(ReservaRepository.class);
    private final Notificador notificador = mock(Notificador.class);

    private final ReservaService servicio =
            new ReservaService(disponibilidad, repository, notificador);

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

    // CP-05
    @Test
    void normalNoRecibeDescuento() {
        // Arrange
        String tipo = "NORMAL";
        double totalBase = 100;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(100.0, total, 0.001);
    }

    // CP-06
    @Test
    void vipRecibeQuincePorCiento() {
        // Arrange
        String tipo = "VIP";
        double totalBase = 100;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(85.0, total, 0.001);
    }

    // CP-07
    @Test
    void estudianteRecibeDiezPorCiento() {
        // Arrange
        String tipo = "ESTUDIANTE";
        double totalBase = 100;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(90.0, total, 0.001);
    }

    // CP-08
    @Test
    void totalCeroVipDevuelveCero() {
        // Arrange
        String tipo = "VIP";
        double totalBase = 0;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(0.0, total, 0.001);
    }

    // CP-09
    @Test
    void totalNegativoEsInvalido() {
        // Arrange
        String tipo = "NORMAL";
        double totalBase = -1;

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal(tipo, totalBase)
        );

        // Assert
        assertEquals("Total base inválido", ex.getMessage());
    }

    // CP-10
    @Test
    void reservaDisponibleSeConfirmaGuardaYNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(any())).thenReturn(true);
        Reserva reserva = new Reserva("R-001", "NORMAL");

        // Act
        servicio.confirmar(reserva);

        // Assert
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        verify(repository).guardar(reserva);
        verify(notificador).enviarConfirmacion(reserva);
    }

    // CP-11
    @Test
    void reservaNoDisponibleNoSeGuardaNiNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(any())).thenReturn(false);
        Reserva reserva = new Reserva("R-002", "NORMAL");

        // Act
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> servicio.confirmar(reserva)
        );

        // Assert
        assertEquals("Horario no disponible", ex.getMessage());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }

    // CP-12
    @Test
    void reservaNulaNoConsultaDependencias() {
        // Arrange
        Reserva reserva = null;

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.confirmar(reserva)
        );

        // Assert
        assertEquals("Reserva obligatoria", ex.getMessage());
        verify(disponibilidad, never()).estaDisponible(any());
        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }
}

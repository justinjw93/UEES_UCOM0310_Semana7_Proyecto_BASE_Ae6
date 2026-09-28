package edu.uees.testing.service;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas de ReservaService organizadas por regla de negocio.
 * Cada prueba indica el caso de la matriz (docs/01_MATRIZ_CASOS_PLANTILLA.md).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService")
class ReservaServiceTest {

    // Stub: controla la respuesta de disponibilidad.
    @Mock
    private DisponibilidadClient disponibilidad;

    // Mocks: permiten verificar que se guarda y se notifica.
    @Mock
    private ReservaRepository repository;

    @Mock
    private Notificador notificador;

    @InjectMocks
    private ReservaService servicio;

    @Test
    void entornoJUnitFunciona() {
        assertTrue(true);
    }

    @Nested
    @DisplayName("Cancelación: se permite con 2 horas o más")
    class Cancelacion {

        @Test
        @DisplayName("CP-01 · 5 horas permiten cancelar")
        void cincoHorasPermitenCancelar() {
            // Arrange
            int horas = 5;

            // Act
            boolean resultado = servicio.puedeCancelar(horas);

            // Assert
            assertTrue(resultado);
        }

        @Test
        @DisplayName("CP-02 · 2 horas es el límite permitido")
        void dosHorasEsElLimitePermitido() {
            // Arrange
            int horas = 2;

            // Act
            boolean resultado = servicio.puedeCancelar(horas);

            // Assert
            assertTrue(resultado);
        }

        @Test
        @DisplayName("CP-03 · 1 hora no permite cancelar")
        void unaHoraNoPermiteCancelar() {
            // Arrange
            int horas = 1;

            // Act
            boolean resultado = servicio.puedeCancelar(horas);

            // Assert
            assertFalse(resultado);
        }

        @Test
        @DisplayName("CP-04 · 0 horas no permite cancelar")
        void ceroHorasNoPermiteCancelar() {
            // Arrange
            int horas = 0;

            // Act
            boolean resultado = servicio.puedeCancelar(horas);

            // Assert
            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Descuentos: VIP 15 %, ESTUDIANTE 10 %, NORMAL sin descuento")
    class Descuentos {

        @Test
        @DisplayName("CP-05 · NORMAL no recibe descuento")
        void normalNoRecibeDescuento() {
            // Arrange
            String tipo = "NORMAL";
            double totalBase = 100;

            // Act
            double total = servicio.calcularTotal(tipo, totalBase);

            // Assert
            assertEquals(100.0, total, 0.001);
        }

        @Test
        @DisplayName("CP-06 · VIP recibe 15 %")
        void vipRecibeQuincePorCiento() {
            // Arrange
            String tipo = "VIP";
            double totalBase = 100;

            // Act
            double total = servicio.calcularTotal(tipo, totalBase);

            // Assert
            assertEquals(85.0, total, 0.001);
        }

        @Test
        @DisplayName("CP-07 · ESTUDIANTE recibe 10 %")
        void estudianteRecibeDiezPorCiento() {
            // Arrange
            String tipo = "ESTUDIANTE";
            double totalBase = 100;

            // Act
            double total = servicio.calcularTotal(tipo, totalBase);

            // Assert
            assertEquals(90.0, total, 0.001);
        }

        @Test
        @DisplayName("CP-08 · total cero con VIP devuelve cero")
        void totalCeroVipDevuelveCero() {
            // Arrange
            String tipo = "VIP";
            double totalBase = 0;

            // Act
            double total = servicio.calcularTotal(tipo, totalBase);

            // Assert
            assertEquals(0.0, total, 0.001);
        }

        @Test
        @DisplayName("CP-09 · total negativo es inválido")
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
    }

    @Nested
    @DisplayName("Confirmación: solo con disponibilidad; guarda y notifica")
    class Confirmacion {

        @Test
        @DisplayName("CP-10 · reserva disponible se confirma, guarda y notifica")
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

        @Test
        @DisplayName("CP-11 · reserva no disponible no se guarda ni notifica")
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

        @Test
        @DisplayName("CP-12 · reserva nula no consulta dependencias")
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
}

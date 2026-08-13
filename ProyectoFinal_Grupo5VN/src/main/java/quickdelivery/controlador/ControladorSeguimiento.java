package quickdelivery.controlador;

import java.util.List;
import javax.swing.Timer;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.modelos.Paquete;
import quickdelivery.modelos.Ubicacion;
import quickdelivery.modelos.Vehiculo;
import quickdelivery.vista.VistaPaquetes;
import quickdelivery.vista.VistaSeguimiento;

/**
 * Actualiza la vista de seguimiento cada 3 segundos.
 */
public class ControladorSeguimiento {

    private final VistaSeguimiento vista;
    private final ClienteSocket cliente;
    private final Timer timer;

    public ControladorSeguimiento(VistaSeguimiento vista, ClienteSocket cliente) {
        this.vista = vista;
        this.cliente = cliente;

        actualizar();

        // Cada 3 segundos vuelve a pedir datos al servidor
        timer = new Timer(3000, e -> actualizar());
        timer.start();

        vista.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                timer.stop();
            }
        });
    }

    private void actualizar() {
        try {
            List<Paquete> paquetes = cliente.listarPaquetes();
            vista.limpiarPaquetes();
            for (Paquete p : paquetes) {
                vista.agregarPaquete(new Object[]{
                    p.getIdPaquete(),
                    p.getDescripcion(),
                    p.getDireccionOrigen(),
                    p.getDireccionDestino(),
                    VistaPaquetes.nombreEstado(p.getIdEstado())
                });
            }

            List<Vehiculo> vehiculos = cliente.listarVehiculos();
            vista.limpiarVehiculos();
            for (Vehiculo v : vehiculos) {
                vista.agregarVehiculo(new Object[]{
                    v.getIdVehiculo(),
                    v.getPlaca(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getDisponible()
                });
            }

            List<Ubicacion> ubicaciones = cliente.listarUbicaciones();
            vista.limpiarUbicaciones();
            for (Ubicacion u : ubicaciones) {
                vista.agregarUbicacion(new Object[]{
                    u.getIdUbicacion(),
                    u.getIdVehiculo(),
                    u.getLatitud(),
                    u.getLongitud(),
                    u.getFechaHora()
                });
            }

            vista.mostrarMensaje(
                    "Actualizado. Paquetes: " + paquetes.size()
                            + " | Vehículos: " + vehiculos.size()
                            + " | Ubicaciones: " + ubicaciones.size()
            );
        } catch (Exception ex) {
            vista.mostrarMensaje("Error al actualizar: " + ex.getMessage());
        }
    }

    public void detener() {
        if (timer != null) {
            timer.stop();
        }
    }
}

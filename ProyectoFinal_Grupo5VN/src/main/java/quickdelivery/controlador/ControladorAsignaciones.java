package quickdelivery.controlador;

import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.modelos.Asignacion;
import quickdelivery.vista.VistaAsignaciones;

public class ControladorAsignaciones {

    private final VistaAsignaciones vista;
    private final ClienteSocket cliente;

    public ControladorAsignaciones(VistaAsignaciones vista, ClienteSocket cliente) {
        this.vista = vista;
        this.cliente = cliente;
        registrarEventos();
        try {
            listarSinDialogo();
        } catch (Exception ex) {
            vista.mostrarMensaje(
                    "No se pudo listar. Reinicie el Servidor y pulse Listar. "
                            + ex.getMessage()
            );
        }
    }

    private void registrarEventos() {
        vista.getBtnAsignar().addActionListener(e -> asignar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnListar().addActionListener(e -> listar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());

        vista.getTabla().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                cargarFila();
            }
        });
    }

    private void asignar() {
        try {
            long idPaquete = Long.parseLong(vista.getTxtIdPaquete().getText().trim());
            long idVehiculo = Long.parseLong(vista.getTxtIdVehiculo().getText().trim());

            Asignacion a = new Asignacion();
            a.setIdPaquete(idPaquete);
            a.setIdVehiculo(idVehiculo);

            String fecha = vista.getTxtFecha().getText().trim();
            if (fecha.isEmpty()) {
                fecha = LocalDate.now().toString();
            }
            a.setFechaAsignacion(fecha);

            cliente.insertarAsignacion(a);
            listar();
            vista.limpiarCampos();
            vista.mostrarMensaje("Paquete asignado al vehículo.");
        } catch (Exception ex) {
            error("Error al asignar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            long id = Long.parseLong(vista.getTxtIdAsignacion().getText().trim());
            int ok = JOptionPane.showConfirmDialog(
                    vista, "¿Eliminar asignación " + id + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (ok != JOptionPane.YES_OPTION) {
                return;
            }
            cliente.eliminarAsignacion(id);
            listar();
            vista.limpiarCampos();
            vista.mostrarMensaje("Asignación eliminada.");
        } catch (Exception ex) {
            error("Error al eliminar: " + ex.getMessage());
        }
    }

    private void listar() {
        try {
            listarSinDialogo();
        } catch (Exception ex) {
            error("Error al listar: " + ex.getMessage());
        }
    }

    private void listarSinDialogo() throws Exception {
        List<Asignacion> lista = cliente.listarAsignaciones();
        vista.limpiarTabla();
        for (Asignacion a : lista) {
            vista.agregarFila(new Object[]{
                a.getIdAsignacion(), a.getIdPaquete(),
                a.getIdVehiculo(), a.getFechaAsignacion()
            });
        }
        vista.mostrarMensaje("Se listaron " + lista.size() + " asignación(es).");
    }

    private void cargarFila() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            return;
        }
        vista.getTxtIdAsignacion().setText(vista.getTabla().getValueAt(fila, 0).toString());
        vista.getTxtIdPaquete().setText(vista.getTabla().getValueAt(fila, 1).toString());
        vista.getTxtIdVehiculo().setText(vista.getTabla().getValueAt(fila, 2).toString());
        vista.getTxtFecha().setText(vista.getTabla().getValueAt(fila, 3).toString());
    }

    private void error(String mensaje) {
        vista.mostrarMensaje(mensaje);
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

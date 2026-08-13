package quickdelivery.controlador;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.cliente.ClienteVehiculo;
import quickdelivery.modelos.Vehiculo;
import quickdelivery.modelos.VehiculoFactory;
import quickdelivery.vista.VistaVehiculos;

public class ControladorVehiculos {

    private final VistaVehiculos vista;
    private final ClienteSocket cliente;

    public ControladorVehiculos(VistaVehiculos vista, ClienteSocket cliente) {
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

    private void listarSinDialogo() throws Exception {
        List<Vehiculo> lista = cliente.listarVehiculos();
        vista.limpiarTabla();
        for (Vehiculo v : lista) {
            vista.agregarFila(new Object[]{
                v.getIdVehiculo(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.obtenerNombreTipo(),
                v.obtenerCapacidad(),
                v.getDisponible()
            });
        }
        vista.mostrarMensaje("Se listaron " + lista.size() + " vehículo(s).");
    }

    private void registrarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> registrar());
        vista.getBtnConsultar().addActionListener(e -> consultar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnListar().addActionListener(e -> listar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
        vista.getBtnAsignarConductor().addActionListener(e -> asignarConductor());
        vista.getBtnIniciarHilo().addActionListener(e -> iniciarHilo());

        vista.getTabla().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                cargarFila();
            }
        });
    }

    private void registrar() {
        try {
            Vehiculo v = leerFormulario(false);
            cliente.insertarVehiculo(v);
            listar();
            vista.limpiarCampos();
            vista.mostrarMensaje("Vehículo registrado.");
        } catch (Exception ex) {
            error("Error al registrar: " + ex.getMessage());
        }
    }

    private void consultar() {
        try {
            long id = Long.parseLong(vista.getTxtId().getText().trim());
            Vehiculo v = cliente.consultarVehiculo(id);
            vista.cargarEnFormulario(
                    v.getIdVehiculo(), v.getPlaca(), v.getMarca(),
                    v.getModelo(), v.getIdTipoVehiculo(), v.getDisponible()
            );
        } catch (Exception ex) {
            error("Error al consultar: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            Vehiculo v = leerFormulario(true);
            cliente.modificarVehiculo(v);
            listar();
            vista.mostrarMensaje("Vehículo actualizado.");
        } catch (Exception ex) {
            error("Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            long id = Long.parseLong(vista.getTxtId().getText().trim());
            int ok = JOptionPane.showConfirmDialog(
                    vista, "¿Eliminar vehículo " + id + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (ok != JOptionPane.YES_OPTION) {
                return;
            }
            cliente.eliminarVehiculo(id);
            listar();
            vista.limpiarCampos();
            vista.mostrarMensaje("Vehículo eliminado.");
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

    private void asignarConductor() {
        try {
            long idVehiculo = Long.parseLong(vista.getTxtId().getText().trim());
            long idUsuario = Long.parseLong(vista.getTxtIdUsuarioConductor().getText().trim());
            String licencia = vista.getTxtLicencia().getText().trim();

            if (licencia.isEmpty()) {
                throw new IllegalArgumentException("Digite la licencia.");
            }

            cliente.asignarConductor(idUsuario, licencia, idVehiculo);
            vista.mostrarMensaje("Conductor asignado al vehículo.");
        } catch (Exception ex) {
            error("Error al asignar conductor: " + ex.getMessage());
        }
    }

    private void iniciarHilo() {
        try {
            long idVehiculo = Long.parseLong(vista.getTxtId().getText().trim());
            Thread hilo = new Thread(new ClienteVehiculo((int) idVehiculo));
            hilo.start();
            vista.mostrarMensaje("Hilo del vehículo " + idVehiculo + " iniciado.");
        } catch (Exception ex) {
            error("Seleccione o escriba un ID de vehículo válido.");
        }
    }

    private void cargarFila() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            return;
        }

        String nombreTipo = vista.getTabla().getValueAt(fila, 4).toString();
        int idTipo = 1;
        if (nombreTipo.equalsIgnoreCase("Furgoneta")) {
            idTipo = 2;
        } else if (nombreTipo.equalsIgnoreCase("Camion")) {
            idTipo = 3;
        } else if (nombreTipo.equalsIgnoreCase("Carro")) {
            idTipo = 4;
        }

        vista.cargarEnFormulario(
                Long.parseLong(vista.getTabla().getValueAt(fila, 0).toString()),
                vista.getTabla().getValueAt(fila, 1).toString(),
                vista.getTabla().getValueAt(fila, 2).toString(),
                vista.getTabla().getValueAt(fila, 3).toString(),
                idTipo,
                vista.getTabla().getValueAt(fila, 6).toString()
        );
    }

    private Vehiculo leerFormulario(boolean requiereId) {
        int idTipo = vista.getIdTipoSeleccionado();
        Vehiculo v = VehiculoFactory.crear(idTipo);

        if (requiereId) {
            v.setIdVehiculo(Long.parseLong(vista.getTxtId().getText().trim()));
        }

        String placa = vista.getTxtPlaca().getText().trim();
        if (placa.isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }

        v.setPlaca(placa);
        v.setMarca(vista.getTxtMarca().getText().trim());
        v.setModelo(vista.getTxtModelo().getText().trim());
        v.setDisponible(vista.getDisponibleSeleccionado());
        return v;
    }

    private void error(String mensaje) {
        vista.mostrarMensaje(mensaje);
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

package quickdelivery.controlador;

import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.modelos.Paquete;
import quickdelivery.vista.VistaPaquetes;

/**
 * Controlador (MVC): conecta la Vista con el Modelo (Paquete + ClienteSocket).
 */
public class ControladorPaquetes {

    private final VistaPaquetes vista;
    private final ClienteSocket cliente;

    public ControladorPaquetes(VistaPaquetes vista, ClienteSocket cliente) {
        this.vista = vista;
        this.cliente = cliente;
        registrarEventos();
    }

    private void registrarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> registrar());
        vista.getBtnConsultar().addActionListener(e -> consultar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnListar().addActionListener(e -> listar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());

        vista.mostrarMensaje(VistaPaquetes.AYUDA);
        vista.actualizarBotonRegistrar();

        vista.getTxtId().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                vista.actualizarBotonRegistrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                vista.actualizarBotonRegistrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                vista.actualizarBotonRegistrar();
            }
        });

        vista.getTablaPaquetes().getSelectionModel().addListSelectionListener(
                (ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        cargarFilaSeleccionada();
                    }
                }
        );
    }

    private void registrar() {
        try {
            if (!vista.getTxtId().getText().trim().isEmpty()) {
                mostrarError(
                        "Hay un paquete cargado. Pulse Limpiar para registrar uno nuevo, "
                                + "o use Actualizar si desea modificar el actual."
                );
                return;
            }

            Paquete paquete = leerPaqueteDelFormulario(false);

            if (paquete.getFechaRegistro() == null || paquete.getFechaRegistro().isBlank()) {
                paquete.setFechaRegistro(LocalDate.now().toString());
            }

            cliente.insertarPaquete(paquete);
            listar();
            vista.limpiarCampos();
            vista.mostrarMensaje(
                    "Paquete registrado correctamente. El ID lo asignó la base de datos."
            );
        } catch (Exception ex) {
            mostrarError("Error al registrar: " + ex.getMessage());
        }
    }

    private void consultar() {
        try {
            long id = Long.parseLong(vista.getTxtId().getText().trim());
            Paquete paquete = cliente.consultarPaquete(id);

            vista.cargarPaqueteEnFormulario(
                    paquete.getIdPaquete(),
                    paquete.getDescripcion(),
                    paquete.getDireccionOrigen(),
                    paquete.getDireccionDestino(),
                    paquete.getPeso(),
                    paquete.getIdEstado(),
                    paquete.getFechaRegistro()
            );
            vista.mostrarMensaje("Paquete encontrado.");
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un ID válido para consultar.");
        } catch (Exception ex) {
            mostrarError("Error al consultar: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            Paquete paquete = leerPaqueteDelFormulario(true);
            cliente.modificarPaquete(paquete);
            vista.mostrarMensaje("Paquete actualizado correctamente.");
            listar();
        } catch (NumberFormatException ex) {
            mostrarError("Para actualizar, seleccione un paquete de la tabla o escriba un ID válido.");
        } catch (Exception ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            long id = Long.parseLong(vista.getTxtId().getText().trim());

            int confirmar = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Eliminar el paquete con ID " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            cliente.eliminarPaquete(id);
            vista.mostrarMensaje("Paquete eliminado correctamente.");
            listar();
            vista.limpiarCampos();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un ID válido para eliminar.");
        } catch (Exception ex) {
            mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

    private void listar() {
        try {
            List<Paquete> paquetes = cliente.listarPaquetes();
            vista.limpiarTabla();

            for (Paquete paquete : paquetes) {
                vista.agregarFilaTabla(new Object[]{
                    paquete.getIdPaquete(),
                    paquete.getDescripcion(),
                    paquete.getDireccionOrigen(),
                    paquete.getDireccionDestino(),
                    paquete.getPeso(),
                    VistaPaquetes.nombreEstado(paquete.getIdEstado()),
                    paquete.getFechaRegistro()
                });
            }

            vista.mostrarMensaje("Se listaron " + paquetes.size() + " paquete(s).");
        } catch (Exception ex) {
            mostrarError("Error al listar: " + ex.getMessage());
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = vista.getTablaPaquetes().getSelectedRow();
        if (fila < 0) {
            return;
        }

        String estadoTexto = vista.getTablaPaquetes().getValueAt(fila, 5).toString();
        int idEstado = 1;
        if (estadoTexto.contains(" - ")) {
            idEstado = Integer.parseInt(estadoTexto.substring(0, estadoTexto.indexOf(' ')));
        }

        vista.cargarPaqueteEnFormulario(
                Long.parseLong(vista.getTablaPaquetes().getValueAt(fila, 0).toString()),
                vista.getTablaPaquetes().getValueAt(fila, 1).toString(),
                vista.getTablaPaquetes().getValueAt(fila, 2).toString(),
                vista.getTablaPaquetes().getValueAt(fila, 3).toString(),
                vista.getTablaPaquetes().getValueAt(fila, 4).toString(),
                idEstado,
                vista.getTablaPaquetes().getValueAt(fila, 6).toString()
        );
    }

    private Paquete leerPaqueteDelFormulario(boolean requiereId) {
        Paquete paquete = new Paquete();

        if (requiereId) {
            String idTexto = vista.getTxtId().getText().trim();
            if (idTexto.isEmpty()) {
                throw new IllegalArgumentException(
                        "Seleccione un paquete de la tabla o escriba el ID a actualizar."
                );
            }
            paquete.setIdPaquete(Long.parseLong(idTexto));
        }

        String descripcion = vista.getTxtDescripcion().getText().trim();
        String origen = vista.getTxtOrigen().getText().trim();
        String destino = vista.getTxtDestino().getText().trim();
        String pesoTexto = vista.getTxtPeso().getText().trim();

        if (descripcion.isEmpty() || origen.isEmpty() || destino.isEmpty() || pesoTexto.isEmpty()) {
            throw new IllegalArgumentException(
                    "Complete descripción, origen, destino y peso."
            );
        }

        String pesoNormalizado = pesoTexto.toLowerCase().replace("kg", "").trim().replace(',', '.');
        try {
            double pesoNumero = Double.parseDouble(pesoNormalizado);
            if (pesoNumero <= 0) {
                throw new IllegalArgumentException("El peso debe ser mayor que 0.");
            }
            // Guarda el número limpio (ej. 10.5)
            if (pesoNumero == Math.rint(pesoNumero)) {
                paquete.setPeso(String.valueOf((long) pesoNumero));
            } else {
                paquete.setPeso(String.valueOf(pesoNumero));
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "El peso debe ser un número, por ejemplo 10 o 10.5"
            );
        }

        paquete.setDescripcion(descripcion);
        paquete.setDireccionOrigen(origen);
        paquete.setDireccionDestino(destino);
        paquete.setIdEstado(vista.getIdEstadoSeleccionado());
        paquete.setFechaRegistro(vista.getTxtFecha().getText().trim());

        return paquete;
    }

    private void mostrarError(String mensaje) {
        vista.mostrarMensaje(mensaje);
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

package quickdelivery.controlador;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.modelos.Usuario;
import quickdelivery.vista.VistaUsuarios;

public class ControladorUsuarios {

    private final VistaUsuarios vista;
    private final ClienteSocket cliente;

    public ControladorUsuarios(VistaUsuarios vista, ClienteSocket cliente) {
        this.vista = vista;
        this.cliente = cliente;
        registrarEventos();
        listar();
    }

    private void registrarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> registrar());
        vista.getBtnConsultar().addActionListener(e -> consultar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnListar().addActionListener(e -> listar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());

        vista.getTablaUsuarios().getSelectionModel().addListSelectionListener(
                (ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        cargarFilaSeleccionada();
                    }
                }
        );
    }

    private void registrar() {
        try {
            Usuario usuario = leerUsuarioDelFormulario(false);
            if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
                throw new IllegalArgumentException("La contraseña es obligatoria al registrar.");
            }
            cliente.insertarUsuario(usuario);
            listar();
            vista.limpiarCampos();
            vista.mostrarMensaje("Usuario registrado correctamente.");
        } catch (Exception ex) {
            mostrarError("Error al registrar: " + ex.getMessage());
        }
    }

    private void consultar() {
        try {
            long id = Long.parseLong(vista.getTxtId().getText().trim());
            Usuario usuario = cliente.consultarUsuario(id);
            vista.cargarUsuarioEnFormulario(
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getNombreCompleto(),
                    usuario.getEmail(),
                    usuario.getIdRol()
            );
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un ID válido para consultar.");
        } catch (Exception ex) {
            mostrarError("Error al consultar: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            Usuario usuario = leerUsuarioDelFormulario(true);
            cliente.modificarUsuario(usuario);
            vista.mostrarMensaje("Usuario actualizado correctamente.");
            listar();
        } catch (NumberFormatException ex) {
            mostrarError("Para actualizar, seleccione un usuario o escriba un ID válido.");
        } catch (Exception ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            long id = Long.parseLong(vista.getTxtId().getText().trim());

            int confirmar = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Eliminar el usuario con ID " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            cliente.eliminarUsuario(id);
            vista.mostrarMensaje("Usuario eliminado correctamente.");
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
            List<Usuario> usuarios = cliente.listarUsuarios();
            vista.limpiarTabla();

            for (Usuario usuario : usuarios) {
                vista.agregarFilaTabla(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getNombreCompleto(),
                    usuario.getEmail(),
                    VistaUsuarios.nombreRol(usuario.getIdRol())
                });
            }

            vista.mostrarMensaje("Se listaron " + usuarios.size() + " usuario(s).");
        } catch (Exception ex) {
            mostrarError("Error al listar: " + ex.getMessage());
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = vista.getTablaUsuarios().getSelectedRow();
        if (fila < 0) {
            return;
        }

        String rolTexto = vista.getTablaUsuarios().getValueAt(fila, 4).toString();
        int idRol = 1;
        if (rolTexto.contains(" - ")) {
            idRol = Integer.parseInt(rolTexto.substring(0, rolTexto.indexOf(' ')));
        }

        vista.cargarUsuarioEnFormulario(
                Long.parseLong(vista.getTablaUsuarios().getValueAt(fila, 0).toString()),
                vista.getTablaUsuarios().getValueAt(fila, 1).toString(),
                vista.getTablaUsuarios().getValueAt(fila, 2).toString(),
                vista.getTablaUsuarios().getValueAt(fila, 3).toString(),
                idRol
        );
    }

    private Usuario leerUsuarioDelFormulario(boolean requiereId) {
        Usuario usuario = new Usuario();

        if (requiereId) {
            String idTexto = vista.getTxtId().getText().trim();
            if (idTexto.isEmpty()) {
                throw new IllegalArgumentException(
                        "Seleccione un usuario de la tabla o escriba el ID."
                );
            }
            usuario.setIdUsuario(Long.parseLong(idTexto));
        }

        String nombreUsuario = vista.getTxtNombreUsuario().getText().trim();
        String nombreCompleto = vista.getTxtNombreCompleto().getText().trim();
        String email = vista.getTxtEmail().getText().trim();
        String contrasena = vista.getContrasena();

        if (nombreUsuario.isEmpty() || nombreCompleto.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException(
                    "Complete nombre de usuario, nombre completo y email."
            );
        }

        usuario.setNombreUsuario(nombreUsuario);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);
        usuario.setIdRol(vista.getIdRolSeleccionado());

        return usuario;
    }

    private void mostrarError(String mensaje) {
        vista.mostrarMensaje(mensaje);
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

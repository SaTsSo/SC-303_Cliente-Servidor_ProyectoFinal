package quickdelivery.controlador;

import javax.swing.JOptionPane;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.modelos.Sesion;
import quickdelivery.modelos.Usuario;
import quickdelivery.vista.VistaInicio;
import quickdelivery.vista.VistaLogin;

public class ControladorLogin {

    private final VistaLogin vista;

    public ControladorLogin(VistaLogin vista) {
        this.vista = vista;
        iniciarEventos();
    }

    private void iniciarEventos() {
        vista.getBtnIngresar().addActionListener(e -> iniciarSesion());
        vista.getBtnSalir().addActionListener(e -> System.exit(0));
        vista.getPassContra().addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String nombreUsuario = vista.getUsuario();
        String contrasena = vista.getContrasena();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarMensaje("Complete todos los campos.");
            return;
        }

        ClienteSocket cliente = new ClienteSocket();

        try {
            cliente.conectar();
            Usuario usuario = cliente.login(nombreUsuario, contrasena);

            if (usuario != null) {
                Sesion.iniciarSesion(usuario);

                VistaInicio inicio = new VistaInicio();
                inicio.configurarSegunRol();
                inicio.setVisible(true);

                vista.dispose();
            } else {
                vista.mostrarMensaje("Usuario o contraseña incorrectos.");
            }
        } catch (Exception ex) {
            vista.mostrarMensaje("No se pudo conectar al servidor.");
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo conectar al servidor.\n"
                            + "Asegúrese de que el Servidor esté en ejecución (puerto 5200).\n"
                            + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            try {
                cliente.desconectar();
            } catch (Exception ignored) {
                // Si falló la conexión, no hay nada que cerrar.
            }
        }
    }
}

package quickdelivery.controlador;

import quickdelivery.dao.UsuarioDAO;
import quickdelivery.modelos.Sesion;
import quickdelivery.modelos.Usuario;
import quickdelivery.vista.VistaInicio;
import quickdelivery.vista.VistaLogin;

public class ControladorLogin {

    private final VistaLogin vista;
    private final UsuarioDAO usuarioDAO;

    public ControladorLogin(VistaLogin vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
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

        Usuario usuario = usuarioDAO.loginUsuario(nombreUsuario, contrasena);

        if (usuario != null) {
            Sesion.iniciarSesion(usuario);

            VistaInicio inicio = new VistaInicio();
            inicio.configurarSegunRol();
            inicio.setVisible(true);

            vista.dispose();
        } else {
            vista.mostrarMensaje("Usuario o contraseña incorrectos.");
        }
    }
}

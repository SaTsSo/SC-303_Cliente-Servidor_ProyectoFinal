package quickdelivery.controlador;

import quickdelivery.GUI.Inicio;
import quickdelivery.GUI.Login;
import quickdelivery.dao.UsuarioDAO;
import quickdelivery.modelos.Sesion;
import quickdelivery.modelos.Usuario;
import quickdelivery.util.Roles;

public class ControladorLogin {
    private Login vista;
    private UsuarioDAO usuarioDAO;

    public ControladorLogin(Login vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();

        iniciarEventos();
    }

    private void iniciarEventos() {
        vista.getBtnIngresar().addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String nombreUsuario = vista.getTxtUsuario().getText().trim();
        String contrasena = new String(vista.getPassContra().getPassword());

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            vista.getLblMensaje().setText("Complete todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.loginUsuario(nombreUsuario, contrasena);

        if (usuario != null) {
            Sesion.iniciarSesion(usuario);

            Inicio inicio = new Inicio();
            inicio.configurarSegunRol();
            inicio.setVisible(true);

            vista.dispose();

        } else {

            vista.getLblMensaje().setText("Usuario o contraseña incorrectos.");

        }
    }
}

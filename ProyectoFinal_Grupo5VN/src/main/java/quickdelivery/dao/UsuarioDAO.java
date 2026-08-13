package quickdelivery.dao;

import quickdelivery.bd.ConexionBD;
import quickdelivery.modelos.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    ConexionBD conexion = new ConexionBD();
    ResultSet resultado = null;

    // Validar usuario para iniciar sesión
    public Usuario loginUsuario(String nombreUsuario, String contrasena) {

        Usuario usuario = null;

        conexion.setConexion();

        conexion.setConsulta("SELECT idUsuario, nombreUsuario, contrasena, "
                + "nombreCompleto, email, idRol "
                + "FROM Usuarios "
                + "WHERE nombreUsuario = ? AND contrasena = ?");

        try {
            conexion.getConsulta().setString(1, nombreUsuario);
            conexion.getConsulta().setString(2, contrasena);

            resultado = conexion.getResultado();

            if (resultado.next()) {

                usuario = new Usuario();

                usuario.setIdUsuario(resultado.getLong("idUsuario"));
                usuario.setNombreUsuario(resultado.getString("nombreUsuario"));
                usuario.setContrasena(resultado.getString("contrasena"));
                usuario.setNombreCompleto(resultado.getString("nombreCompleto"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setIdRol(resultado.getInt("idRol"));
            }

            conexion.cerrarConexion();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
}

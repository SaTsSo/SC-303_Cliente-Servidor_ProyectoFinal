package quickdelivery.dao;

import quickdelivery.bd.ConexionBD;
import quickdelivery.modelos.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    ConexionBD conexion = new ConexionBD();
    ResultSet resultado = null;

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
                usuario = mapearUsuario(resultado);
            }
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        conexion.setConexion();
        conexion.setConsulta("SELECT idUsuario, nombreUsuario, contrasena, "
                + "nombreCompleto, email, idRol FROM Usuarios");

        try {
            resultado = conexion.getResultado();
            while (resultado.next()) {
                usuarios.add(mapearUsuario(resultado));
            }
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public Usuario consultarUsuarioPorId(long id) {
        Usuario usuario = null;
        conexion.setConexion();
        conexion.setConsulta("SELECT idUsuario, nombreUsuario, contrasena, "
                + "nombreCompleto, email, idRol FROM Usuarios WHERE idUsuario = ?");

        try {
            conexion.getConsulta().setLong(1, id);
            resultado = conexion.getResultado();
            if (resultado.next()) {
                usuario = mapearUsuario(resultado);
            }
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public void insertarUsuario(Usuario usuario) {
        conexion.setConexion();
        conexion.setConsulta("INSERT INTO Usuarios "
                + "(nombreUsuario, contrasena, nombreCompleto, email, idRol) "
                + "VALUES (?, ?, ?, ?, ?)");

        try {
            conexion.getConsulta().setString(1, usuario.getNombreUsuario());
            conexion.getConsulta().setString(2, usuario.getContrasena());
            conexion.getConsulta().setString(3, usuario.getNombreCompleto());
            conexion.getConsulta().setString(4, usuario.getEmail());
            conexion.getConsulta().setInt(5, usuario.getIdRol());
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo insertar el usuario: " + e.getMessage(), e);
        }
    }

    public void modificarUsuario(Usuario usuario) {
        conexion.setConexion();

        boolean cambiaContrasena = usuario.getContrasena() != null
                && !usuario.getContrasena().isBlank();

        if (cambiaContrasena) {
            conexion.setConsulta("UPDATE Usuarios SET nombreUsuario = ?, contrasena = ?, "
                    + "nombreCompleto = ?, email = ?, idRol = ? WHERE idUsuario = ?");
        } else {
            conexion.setConsulta("UPDATE Usuarios SET nombreUsuario = ?, "
                    + "nombreCompleto = ?, email = ?, idRol = ? WHERE idUsuario = ?");
        }

        try {
            if (cambiaContrasena) {
                conexion.getConsulta().setString(1, usuario.getNombreUsuario());
                conexion.getConsulta().setString(2, usuario.getContrasena());
                conexion.getConsulta().setString(3, usuario.getNombreCompleto());
                conexion.getConsulta().setString(4, usuario.getEmail());
                conexion.getConsulta().setInt(5, usuario.getIdRol());
                conexion.getConsulta().setLong(6, usuario.getIdUsuario());
            } else {
                conexion.getConsulta().setString(1, usuario.getNombreUsuario());
                conexion.getConsulta().setString(2, usuario.getNombreCompleto());
                conexion.getConsulta().setString(3, usuario.getEmail());
                conexion.getConsulta().setInt(4, usuario.getIdRol());
                conexion.getConsulta().setLong(5, usuario.getIdUsuario());
            }

            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo modificar el usuario: " + e.getMessage(), e);
        }
    }

    public void eliminarUsuario(long id) {
        conexion.setConexion();
        conexion.setConsulta("DELETE FROM Usuarios WHERE idUsuario = ?");

        try {
            conexion.getConsulta().setLong(1, id);
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo eliminar el usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getLong("idUsuario"));
        usuario.setNombreUsuario(rs.getString("nombreUsuario"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setNombreCompleto(rs.getString("nombreCompleto"));
        usuario.setEmail(rs.getString("email"));
        usuario.setIdRol(rs.getInt("idRol"));
        return usuario;
    }
}

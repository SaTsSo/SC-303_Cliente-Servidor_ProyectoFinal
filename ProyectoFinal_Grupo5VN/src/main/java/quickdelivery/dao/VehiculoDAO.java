package quickdelivery.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import quickdelivery.bd.ConexionBD;
import quickdelivery.modelos.Vehiculo;

public class VehiculoDAO {

    ConexionBD conexion = new ConexionBD();
    ResultSet resultado = null;

    public List<Vehiculo> listarVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        conexion.setConexion();
        conexion.setConsulta(
                "SELECT idVehiculo, placa, marca, modelo, idTipoVehiculo, disponible FROM Vehiculos"
        );

        try {
            resultado = conexion.getResultado();
            while (resultado.next()) {
                lista.add(mapear(resultado));
            }
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Vehiculo consultarPorId(long id) {
        Vehiculo vehiculo = null;
        conexion.setConexion();
        conexion.setConsulta(
                "SELECT idVehiculo, placa, marca, modelo, idTipoVehiculo, disponible "
                        + "FROM Vehiculos WHERE idVehiculo = ?"
        );

        try {
            conexion.getConsulta().setLong(1, id);
            resultado = conexion.getResultado();
            if (resultado.next()) {
                vehiculo = mapear(resultado);
            }
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehiculo;
    }

    public void insertar(Vehiculo vehiculo) {
        conexion.setConexion();
        conexion.setConsulta(
                "INSERT INTO Vehiculos (placa, marca, modelo, idTipoVehiculo, disponible) "
                        + "VALUES (?, ?, ?, ?, ?)"
        );

        try {
            conexion.getConsulta().setString(1, vehiculo.getPlaca());
            conexion.getConsulta().setString(2, vehiculo.getMarca());
            conexion.getConsulta().setString(3, vehiculo.getModelo());
            conexion.getConsulta().setInt(4, vehiculo.getIdTipoVehiculo());
            conexion.getConsulta().setString(5, vehiculo.getDisponible());
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void modificar(Vehiculo vehiculo) {
        conexion.setConexion();
        conexion.setConsulta(
                "UPDATE Vehiculos SET placa = ?, marca = ?, modelo = ?, "
                        + "idTipoVehiculo = ?, disponible = ? WHERE idVehiculo = ?"
        );

        try {
            conexion.getConsulta().setString(1, vehiculo.getPlaca());
            conexion.getConsulta().setString(2, vehiculo.getMarca());
            conexion.getConsulta().setString(3, vehiculo.getModelo());
            conexion.getConsulta().setInt(4, vehiculo.getIdTipoVehiculo());
            conexion.getConsulta().setString(5, vehiculo.getDisponible());
            conexion.getConsulta().setLong(6, vehiculo.getIdVehiculo());
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void eliminar(long id) {
        conexion.setConexion();
        conexion.setConsulta("DELETE FROM Vehiculos WHERE idVehiculo = ?");

        try {
            conexion.getConsulta().setLong(1, id);
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void actualizarDisponible(long idVehiculo, String disponible) {
        conexion.setConexion();
        conexion.setConsulta("UPDATE Vehiculos SET disponible = ? WHERE idVehiculo = ?");

        try {
            conexion.getConsulta().setString(1, disponible);
            conexion.getConsulta().setLong(2, idVehiculo);
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardarUbicacion(long idVehiculo, String latitud, String longitud, String fechaHora) {
        conexion.setConexion();
        conexion.setConsulta(
                "INSERT INTO UbicacionesVehiculos (idVehiculo, latitud, longitud, fechaHora) "
                        + "VALUES (?, ?, ?, ?)"
        );

        try {
            conexion.getConsulta().setLong(1, idVehiculo);
            conexion.getConsulta().setString(2, latitud);
            conexion.getConsulta().setString(3, longitud);
            conexion.getConsulta().setString(4, fechaHora);
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void asignarConductor(long idUsuario, String licencia, long idVehiculo) {
        conexion.setConexion();
        conexion.setConsulta(
                "INSERT INTO Conductores (idUsuario, licencia, idVehiculo) VALUES (?, ?, ?)"
        );

        try {
            conexion.getConsulta().setLong(1, idUsuario);
            conexion.getConsulta().setString(2, licencia);
            conexion.getConsulta().setLong(3, idVehiculo);
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Vehiculo mapear(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setIdVehiculo(rs.getLong("idVehiculo"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setIdTipoVehiculo(rs.getInt("idTipoVehiculo"));
        v.setDisponible(rs.getString("disponible"));
        return v;
    }
}

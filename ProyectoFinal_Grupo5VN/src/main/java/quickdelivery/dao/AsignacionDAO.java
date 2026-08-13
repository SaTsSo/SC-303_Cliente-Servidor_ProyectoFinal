package quickdelivery.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import quickdelivery.bd.ConexionBD;
import quickdelivery.modelos.Asignacion;

public class AsignacionDAO {

    ConexionBD conexion = new ConexionBD();
    ResultSet resultado = null;

    public List<Asignacion> listarAsignaciones() {
        List<Asignacion> lista = new ArrayList<>();
        conexion.setConexion();
        conexion.setConsulta(
                "SELECT idAsignacion, idPaquete, idVehiculo, fechaAsignacion FROM AsignacionesPaquetes"
        );

        try {
            resultado = conexion.getResultado();
            while (resultado.next()) {
                Asignacion a = new Asignacion();
                a.setIdAsignacion(resultado.getLong("idAsignacion"));
                a.setIdPaquete(resultado.getLong("idPaquete"));
                a.setIdVehiculo(resultado.getLong("idVehiculo"));
                a.setFechaAsignacion(resultado.getString("fechaAsignacion"));
                lista.add(a);
            }
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insertar(Asignacion asignacion) {
        conexion.setConexion();
        conexion.setConsulta(
                "INSERT INTO AsignacionesPaquetes (idPaquete, idVehiculo, fechaAsignacion) "
                        + "VALUES (?, ?, ?)"
        );

        try {
            conexion.getConsulta().setLong(1, asignacion.getIdPaquete());
            conexion.getConsulta().setLong(2, asignacion.getIdVehiculo());
            conexion.getConsulta().setString(3, asignacion.getFechaAsignacion());
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void eliminar(long idAsignacion) {
        conexion.setConexion();
        conexion.setConsulta("DELETE FROM AsignacionesPaquetes WHERE idAsignacion = ?");

        try {
            conexion.getConsulta().setLong(1, idAsignacion);
            conexion.getConsulta().executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

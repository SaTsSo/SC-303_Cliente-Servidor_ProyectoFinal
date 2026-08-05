package quickdelivery.dao;

import quickdelivery.bd.ConexionBD;
import quickdelivery.modelos.Paquete;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaqueteDAO {

    ConexionBD conexion = new ConexionBD();
    ResultSet resultado = null;


    //Consultar todos los paquetes (SELECT)
    public List<Paquete> listarPaquetes() {

        List<Paquete> paquetes = new ArrayList<>();

        conexion.setConexion();

        conexion.setConsulta("SELECT idPaquete, descripcion, direccionOrigen, direccionDestino, "
                + "peso, idEstado, fechaRegistro FROM Paquetes");

        try {
            resultado = conexion.getResultado();

            while (resultado.next()) {
                Paquete paquete = new Paquete();
                paquete.setIdPaquete(resultado.getLong("idPaquete"));
                paquete.setDescripcion(resultado.getString("descripcion"));
                paquete.setDireccionOrigen(resultado.getString("direccionOrigen"));
                paquete.setDireccionDestino(resultado.getString("direccionDestino"));
                paquete.setPeso(resultado.getString("peso"));
                paquete.setIdEstado(resultado.getInt("idEstado"));
                paquete.setFechaRegistro(resultado.getString("fechaRegistro"));

                paquetes.add(paquete);
            }

            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paquetes;
    }

    //Consultar un paquete por ID (SELECT)
    public Paquete consultarPaquetePorId(long id) {

        Paquete paquete = null;

        conexion.setConexion();

        conexion.setConsulta("SELECT idPaquete, descripcion, direccionOrigen, direccionDestino, "
                + "peso, idEstado, fechaRegistro FROM Paquetes WHERE idPaquete = ?");

        try {
            conexion.getConsulta().setLong(1, id);

            resultado = conexion.getResultado();

            while (resultado.next()) {
                paquete = new Paquete();
                paquete.setIdPaquete(resultado.getLong("idPaquete"));
                paquete.setDescripcion(resultado.getString("descripcion"));
                paquete.setDireccionOrigen(resultado.getString("direccionOrigen"));
                paquete.setDireccionDestino(resultado.getString("direccionDestino"));
                paquete.setPeso(resultado.getString("peso"));
                paquete.setIdEstado(resultado.getInt("idEstado"));
                paquete.setFechaRegistro(resultado.getString("fechaRegistro"));
            }

            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paquete;
    }

    //Agregar paquete nuevo (INSERT)
    public void insertarPaquete(Paquete paquete) {

        conexion.setConexion();

        conexion.setConsulta("INSERT INTO Paquetes (descripcion, direccionOrigen, direccionDestino, "
                + "peso, idEstado, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?)");

        try {
            conexion.getConsulta().setString(1, paquete.getDescripcion());
            conexion.getConsulta().setString(2, paquete.getDireccionOrigen());
            conexion.getConsulta().setString(3, paquete.getDireccionDestino());
            conexion.getConsulta().setString(4, paquete.getPeso());
            conexion.getConsulta().setInt(5, paquete.getIdEstado());
            conexion.getConsulta().setString(6, paquete.getFechaRegistro());

            if (conexion.getConsulta().executeUpdate() > 0) {
                System.out.println("Paquete guardado correctamente!");
            } else {
                System.out.println("Error al guardar el paquete!");
            }

            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Modificar datos de paquete (UPDATE)
    public void modificarPaquete(Paquete paquete) {

        conexion.setConexion();

        conexion.setConsulta("UPDATE Paquetes SET descripcion = ?, direccionOrigen = ?, "
                + "direccionDestino = ?, peso = ? WHERE idPaquete = ?");

        try {
            conexion.getConsulta().setString(1, paquete.getDescripcion());
            conexion.getConsulta().setString(2, paquete.getDireccionOrigen());
            conexion.getConsulta().setString(3, paquete.getDireccionDestino());
            conexion.getConsulta().setString(4, paquete.getPeso());
            conexion.getConsulta().setLong(5, paquete.getIdPaquete());

            if (conexion.getConsulta().executeUpdate() > 0) {
                System.out.println("Paquete modificado correctamente!");
            } else {
                System.out.println("Error al modificar el paquete!");
            }

            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Actualizar el estado de paquete (En transito, Entregado, Incidencia)
    public void actualizarEstadoPaquete(long idPaquete, int idEstado) {

        conexion.setConexion();

        conexion.setConsulta("UPDATE Paquetes SET idEstado = ? WHERE idPaquete = ?");

        try {
            conexion.getConsulta().setInt(1, idEstado);
            conexion.getConsulta().setLong(2, idPaquete);

            if (conexion.getConsulta().executeUpdate() > 0) {
                System.out.println("Estado del paquete actualizado correctamente!");
            } else {
                System.out.println("Error al actualizar el estado del paquete!");
            }

            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Eliminar paquete (DELETE)
    public void eliminarPaquete(long id) {

        conexion.setConexion();

        conexion.setConsulta("DELETE FROM Paquetes WHERE idPaquete = ?");

        try {
            conexion.getConsulta().setLong(1, id);

            if (conexion.getConsulta().executeUpdate() > 0) {
                System.out.println("Paquete eliminado correctamente!");
            } else {
                System.out.println("Error al eliminar el paquete!");
            }

            conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
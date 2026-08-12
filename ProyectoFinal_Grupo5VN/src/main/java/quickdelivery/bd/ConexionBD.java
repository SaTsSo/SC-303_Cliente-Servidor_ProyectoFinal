package quickdelivery.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionBD {

    private Connection conexion = null;
    private PreparedStatement consulta = null;
    private ResultSet resultado = null;

    private String url = "jdbc:mysql://localhost:3306/QuickDelivery";
    private String username = "quickdelivery_user";
    private String password = "QuickDelivery123";


    //Métodos para la conexión a la BD
    public void setConsulta(String sql) {
        try {
            if (conexion == null) {
                throw new SQLException(
                        "No hay conexión a MySQL. Revise usuario/contraseña y que exista la BD QuickDelivery."
                );
            }
            this.consulta = conexion.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public PreparedStatement getConsulta() {
        return consulta;
    }

    public ResultSet getResultado() {
        try {
            return consulta.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "No se pudo conectar a MySQL (" + e.getMessage() + ")",
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void cerrarConexion() {
        if (resultado != null) {
            try {
                resultado.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (consulta != null) {
            try {
                consulta.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

package quickdelivery.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;

public class ConexionBD {

    Connection conexion = null;
    PreparedStatement consulta = null;
    ResultSet resultado = null;

    String url = "jdbc:mysql://localhost:3306/QuickDelivery";
    String username = "root";
    String password = "password";


    //Métodos para la conexión a la BD
    public void setConsulta(String sql) {
        try {
            this.consulta = conexion.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
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

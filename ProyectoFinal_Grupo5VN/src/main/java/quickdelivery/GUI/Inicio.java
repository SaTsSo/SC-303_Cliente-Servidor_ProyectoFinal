package quickdelivery.GUI;

import quickdelivery.controlador.ControladorLogin;
import quickdelivery.modelos.Sesion;
import quickdelivery.util.Permisos;

import javax.swing.*;

public class Inicio extends javax.swing.JFrame {
    private JLabel LblEmpresa;
    private JLabel LblUsuario;
    private JLabel LblInicio;
    private JLabel LblResumen;
    private JPanel TablaResumen;
    private JTable table1;
    private JButton salirButton;
    private JButton vehiculosButton;
    private JButton paquetesButton;
    private JButton asignacionesButton;
    private JButton seguimientoButton;
    private JButton usuariosButton;

    public Inicio() {
        initComponents();
    }
    private void initComponents(){
        setContentPane(TablaResumen);
        setEnabled(true);
        setTitle("QUICKDELIVERY");
        setSize(1000, 1000);

        salirButton.addActionListener(e -> {
            Sesion.cerrarSesion();

            Login login = new Login();
            new ControladorLogin(login);

            login.setVisible(true);

            dispose();
        });
    }

    public JLabel getLblEmpresa() {
        return LblEmpresa;
    }

    public JLabel getLblUsuario() {
        return LblUsuario;
    }

    public JLabel getLblInicio() {
        return LblInicio;
    }

    public JLabel getLblResumen() {
        return LblResumen;
    }

    public JPanel getTablaResumen() {
        return TablaResumen;
    }

    public JTable getTable1() {
        return table1;
    }

    public JButton getSalirButton() {
        return salirButton;
    }

    public JButton getVehiculosButton() {
        return vehiculosButton;
    }

    public JButton getPaquetesButton() {
        return paquetesButton;
    }

    public JButton getAsignacionesButton() {
        return asignacionesButton;
    }

    public JButton getSeguimientoButton() {
        return seguimientoButton;
    }

    public JButton getUsuariosButton() {
        return usuariosButton;
    }

    public void configurarSegunRol() {

        LblUsuario.setText(
                "Usuario: " + Sesion.getUsuarioActual().getNombreCompleto()
        );

        usuariosButton.setVisible(Permisos.puedeGestionarUsuarios());
        vehiculosButton.setVisible(Permisos.puedeGestionarVehiculos());
        paquetesButton.setVisible(Permisos.puedeGestionarPaquetes());
        asignacionesButton.setVisible(Permisos.puedeAsignarPaquetes());
        seguimientoButton.setVisible(
                Permisos.puedeActualizarEstadoPaquete()
        );
        LblResumen.setVisible(Permisos.esAdministrador()
                || Permisos.esDespachador());
    }
}

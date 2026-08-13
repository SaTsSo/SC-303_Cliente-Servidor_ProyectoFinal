package quickdelivery.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.controlador.ControladorAsignaciones;
import quickdelivery.controlador.ControladorLogin;
import quickdelivery.controlador.ControladorPaquetes;
import quickdelivery.controlador.ControladorSeguimiento;
import quickdelivery.controlador.ControladorUsuarios;
import quickdelivery.controlador.ControladorVehiculos;
import quickdelivery.modelos.Sesion;
import quickdelivery.util.Permisos;

public class VistaInicio extends JFrame {

    private JLabel lblEmpresa;
    private JLabel lblUsuario;
    private JLabel lblInicio;
    private JLabel lblResumen;

    private JButton btnVehiculos;
    private JButton btnPaquetes;
    private JButton btnAsignaciones;
    private JButton btnSeguimiento;
    private JButton btnUsuarios;
    private JButton btnCerrarSesion;

    private JPanel panelResumen;

    public VistaInicio() {
        setTitle("QuickDelivery");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelMenu(), BorderLayout.WEST);
        add(crearPanelCentral(), BorderLayout.CENTER);

        configurarEventos();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lblEmpresa = new JLabel("QuickDelivery S.A.");
        lblEmpresa.setFont(lblEmpresa.getFont().deriveFont(Font.BOLD, 20f));

        lblUsuario = new JLabel("Usuario: -", SwingConstants.RIGHT);

        panel.add(lblEmpresa, BorderLayout.WEST);
        panel.add(lblUsuario, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setPreferredSize(new Dimension(180, 0));

        lblInicio = new JLabel("Inicio", SwingConstants.CENTER);
        lblInicio.setFont(lblInicio.getFont().deriveFont(Font.BOLD, 16f));

        JPanel botones = new JPanel(new GridLayout(5, 1, 8, 8));
        btnVehiculos = new JButton("Vehiculos");
        btnPaquetes = new JButton("Paquetes");
        btnAsignaciones = new JButton("Asignaciones");
        btnSeguimiento = new JButton("Seguimiento");
        btnUsuarios = new JButton("Usuarios");

        botones.add(btnVehiculos);
        botones.add(btnPaquetes);
        botones.add(btnAsignaciones);
        botones.add(btnSeguimiento);
        botones.add(btnUsuarios);

        btnCerrarSesion = new JButton("Cerrar Sesion");

        panel.add(lblInicio, BorderLayout.NORTH);
        panel.add(botones, BorderLayout.CENTER);
        panel.add(btnCerrarSesion, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

        lblResumen = new JLabel("Resumen General", SwingConstants.CENTER);
        lblResumen.setFont(lblResumen.getFont().deriveFont(Font.BOLD, 16f));

        panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelResumen.setBorder(BorderFactory.createEtchedBorder());

        panel.add(lblResumen, BorderLayout.NORTH);
        panel.add(panelResumen, BorderLayout.CENTER);
        return panel;
    }

    private void configurarEventos() {
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        btnPaquetes.addActionListener(e -> abrirPaquetes());
        btnUsuarios.addActionListener(e -> abrirUsuarios());
        btnVehiculos.addActionListener(e -> abrirVehiculos());
        btnAsignaciones.addActionListener(e -> abrirAsignaciones());
        btnSeguimiento.addActionListener(e -> abrirSeguimiento());
    }

    private void abrirSeguimiento() {
        try {
            ClienteSocket cliente = new ClienteSocket();
            cliente.conectar();

            VistaSeguimiento vista = new VistaSeguimiento();
            ControladorSeguimiento controlador = new ControladorSeguimiento(vista, cliente);

            vista.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    controlador.detener();
                    try {
                        cliente.desconectar();
                    } catch (Exception ex) {
                        System.out.println("Error al desconectar: " + ex.getMessage());
                    }
                }
            });

            vista.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar al servidor.\n" + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirVehiculos() {
        try {
            ClienteSocket cliente = new ClienteSocket();
            cliente.conectar();

            VistaVehiculos vista = new VistaVehiculos();
            new ControladorVehiculos(vista, cliente);

            vista.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        cliente.desconectar();
                    } catch (Exception ex) {
                        System.out.println("Error al desconectar: " + ex.getMessage());
                    }
                }
            });

            vista.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar al servidor.\n" + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirAsignaciones() {
        try {
            ClienteSocket cliente = new ClienteSocket();
            cliente.conectar();

            VistaAsignaciones vista = new VistaAsignaciones();
            new ControladorAsignaciones(vista, cliente);

            vista.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        cliente.desconectar();
                    } catch (Exception ex) {
                        System.out.println("Error al desconectar: " + ex.getMessage());
                    }
                }
            });

            vista.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar al servidor.\n" + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirUsuarios() {
        try {
            ClienteSocket cliente = new ClienteSocket();
            cliente.conectar();

            VistaUsuarios vistaUsuarios = new VistaUsuarios();
            new ControladorUsuarios(vistaUsuarios, cliente);

            vistaUsuarios.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        cliente.desconectar();
                    } catch (Exception ex) {
                        System.out.println("Error al desconectar: " + ex.getMessage());
                    }
                }
            });

            vistaUsuarios.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar al servidor.\n"
                            + "Asegúrese de que el Servidor esté en ejecución (puerto 5200).\n"
                            + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirPaquetes() {
        try {
            ClienteSocket cliente = new ClienteSocket();
            cliente.conectar();

            VistaPaquetes vistaPaquetes = new VistaPaquetes();
            vistaPaquetes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            new ControladorPaquetes(vistaPaquetes, cliente);

            vistaPaquetes.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        cliente.desconectar();
                    } catch (Exception ex) {
                        System.out.println("Error al desconectar: " + ex.getMessage());
                    }
                }
            });

            vistaPaquetes.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar al servidor.\n"
                            + "Asegúrese de que el Servidor esté en ejecución (puerto 5200).\n"
                            + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cerrarSesion() {
        Sesion.cerrarSesion();

        VistaLogin login = new VistaLogin();
        new ControladorLogin(login);
        login.setVisible(true);

        dispose();
    }

    public void configurarSegunRol() {
        if (Sesion.getUsuarioActual() != null) {
            lblUsuario.setText(
                    "Usuario: " + Sesion.getUsuarioActual().getNombreCompleto()
            );
        }

        btnUsuarios.setVisible(Permisos.puedeGestionarUsuarios());
        btnVehiculos.setVisible(Permisos.puedeGestionarVehiculos());
        btnPaquetes.setVisible(Permisos.puedeGestionarPaquetes());
        btnAsignaciones.setVisible(Permisos.puedeAsignarPaquetes());
        btnSeguimiento.setVisible(Permisos.puedeActualizarEstadoPaquete());
        lblResumen.setVisible(Permisos.esAdministrador() || Permisos.esDespachador());
        panelResumen.setVisible(lblResumen.isVisible());
    }

    public JButton getBtnVehiculos() {
        return btnVehiculos;
    }

    public JButton getBtnPaquetes() {
        return btnPaquetes;
    }

    public JButton getBtnAsignaciones() {
        return btnAsignaciones;
    }

    public JButton getBtnSeguimiento() {
        return btnSeguimiento;
    }

    public JButton getBtnUsuarios() {
        return btnUsuarios;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }
}

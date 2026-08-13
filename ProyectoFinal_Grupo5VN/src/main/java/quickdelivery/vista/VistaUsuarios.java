package quickdelivery.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Vista (MVC): CRUD de usuarios.
 */
public class VistaUsuarios extends JFrame {

    public static final String[] ROLES = {
        "1 - Administrador",
        "2 - Despachador",
        "3 - Conductor"
    };

    public static final String AYUDA =
            "Registrar: complete los datos (sin ID). "
                    + "Consultar/Actualizar/Eliminar: escriba el ID o seleccione una fila. "
                    + "En actualizar, deje la contraseña vacía para no cambiarla.";

    private JTextField txtId;
    private JTextField txtNombreUsuario;
    private JPasswordField passContrasena;
    private JTextField txtNombreCompleto;
    private JTextField txtEmail;
    private JComboBox<String> cmbRol;

    private JButton btnRegistrar;
    private JButton btnConsultar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnListar;
    private JButton btnLimpiar;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    public VistaUsuarios() {
        setTitle("QuickDelivery - CRUD Usuarios");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel campos = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId = new JTextField();
        txtNombreUsuario = new JTextField();
        passContrasena = new JPasswordField();
        txtNombreCompleto = new JTextField();
        txtEmail = new JTextField();
        cmbRol = new JComboBox<>(new DefaultComboBoxModel<>(ROLES));

        campos.add(new JLabel("ID (solo consultar / actualizar / eliminar):"));
        campos.add(txtId);
        campos.add(new JLabel("Nombre de usuario:"));
        campos.add(txtNombreUsuario);
        campos.add(new JLabel("Contraseña:"));
        campos.add(passContrasena);
        campos.add(new JLabel("Nombre completo:"));
        campos.add(txtNombreCompleto);
        campos.add(new JLabel("Email:"));
        campos.add(txtEmail);
        campos.add(new JLabel("Rol:"));
        campos.add(cmbRol);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegistrar = new JButton("Registrar");
        btnConsultar = new JButton("Consultar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnListar = new JButton("Listar");
        btnLimpiar = new JButton("Limpiar");

        botones.add(btnRegistrar);
        botones.add(btnConsultar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnListar);
        botones.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Usuario", "Nombre completo", "Email", "Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        return new JScrollPane(tablaUsuarios);
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel lblAyuda = new JLabel(AYUDA);
        lblMensaje = new JLabel("Listo.");
        panel.add(lblAyuda, BorderLayout.NORTH);
        panel.add(lblMensaje, BorderLayout.SOUTH);
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombreUsuario.setText("");
        passContrasena.setText("");
        txtNombreCompleto.setText("");
        txtEmail.setText("");
        cmbRol.setSelectedIndex(0);
        mostrarMensaje("Formulario limpio.");
    }

    public void cargarUsuarioEnFormulario(
            long id,
            String nombreUsuario,
            String nombreCompleto,
            String email,
            int idRol
    ) {
        txtId.setText(String.valueOf(id));
        txtNombreUsuario.setText(nombreUsuario);
        passContrasena.setText("");
        txtNombreCompleto.setText(nombreCompleto);
        txtEmail.setText(email);
        seleccionarRol(idRol);
        mostrarMensaje("Usuario cargado. Puede Actualizar o Eliminar.");
    }

    public void seleccionarRol(int idRol) {
        int indice = idRol - 1;
        if (indice >= 0 && indice < ROLES.length) {
            cmbRol.setSelectedIndex(indice);
        } else {
            cmbRol.setSelectedIndex(0);
        }
    }

    public int getIdRolSeleccionado() {
        return cmbRol.getSelectedIndex() + 1;
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFilaTabla(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public static String nombreRol(int idRol) {
        int indice = idRol - 1;
        if (indice >= 0 && indice < ROLES.length) {
            return ROLES[indice];
        }
        return String.valueOf(idRol);
    }

    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtNombreUsuario() {
        return txtNombreUsuario;
    }

    public JPasswordField getPassContrasena() {
        return passContrasena;
    }

    public String getContrasena() {
        return new String(passContrasena.getPassword());
    }

    public JTextField getTxtNombreCompleto() {
        return txtNombreCompleto;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JComboBox<String> getCmbRol() {
        return cmbRol;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnConsultar() {
        return btnConsultar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JTable getTablaUsuarios() {
        return tablaUsuarios;
    }
}

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VistaVehiculos extends JFrame {

    public static final String[] TIPOS = {
        "1 - Moto",
        "2 - Furgoneta",
        "3 - Camion"
    };

    public static final String[] DISPONIBLE = {"SI", "NO"};

    private JTextField txtId;
    private JTextField txtPlaca;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbDisponible;
    private JTextField txtIdUsuarioConductor;
    private JTextField txtLicencia;

    private JButton btnRegistrar;
    private JButton btnConsultar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnListar;
    private JButton btnLimpiar;
    private JButton btnAsignarConductor;
    private JButton btnIniciarHilo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    public VistaVehiculos() {
        setTitle("QuickDelivery - Vehiculos");
        setSize(850, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel campos = new JPanel(new GridLayout(8, 2, 5, 5));

        txtId = new JTextField();
        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        cmbTipo = new JComboBox<>(new DefaultComboBoxModel<>(TIPOS));
        cmbDisponible = new JComboBox<>(new DefaultComboBoxModel<>(DISPONIBLE));
        txtIdUsuarioConductor = new JTextField();
        txtLicencia = new JTextField();

        campos.add(new JLabel("ID:"));
        campos.add(txtId);
        campos.add(new JLabel("Placa:"));
        campos.add(txtPlaca);
        campos.add(new JLabel("Marca:"));
        campos.add(txtMarca);
        campos.add(new JLabel("Modelo:"));
        campos.add(txtModelo);
        campos.add(new JLabel("Tipo:"));
        campos.add(cmbTipo);
        campos.add(new JLabel("Disponible:"));
        campos.add(cmbDisponible);
        campos.add(new JLabel("ID usuario conductor (opcional):"));
        campos.add(txtIdUsuarioConductor);
        campos.add(new JLabel("Licencia conductor (opcional):"));
        campos.add(txtLicencia);

        JPanel botones = new JPanel(new FlowLayout());
        btnRegistrar = new JButton("Registrar");
        btnConsultar = new JButton("Consultar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnListar = new JButton("Listar");
        btnLimpiar = new JButton("Limpiar");
        btnAsignarConductor = new JButton("Asignar conductor");
        btnIniciarHilo = new JButton("Iniciar hilo vehículo");

        botones.add(btnRegistrar);
        botones.add(btnConsultar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnListar);
        botones.add(btnLimpiar);
        botones.add(btnAsignarConductor);
        botones.add(btnIniciarHilo);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane crearTabla() {
        String[] cols = {"ID", "Placa", "Marca", "Modelo", "Tipo", "Disponible"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        return new JScrollPane(tabla);
    }

    private JPanel crearInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        lblMensaje = new JLabel("Listo.");
        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    public void limpiarCampos() {
        txtId.setText("");
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbDisponible.setSelectedIndex(0);
        txtIdUsuarioConductor.setText("");
        txtLicencia.setText("");
        mostrarMensaje("Formulario limpio.");
    }

    public void cargarEnFormulario(long id, String placa, String marca, String modelo,
                                   int idTipo, String disponible) {
        txtId.setText(String.valueOf(id));
        txtPlaca.setText(placa);
        txtMarca.setText(marca);
        txtModelo.setText(modelo);
        cmbTipo.setSelectedIndex(Math.max(0, idTipo - 1));
        cmbDisponible.setSelectedItem(disponible == null ? "SI" : disponible);
        mostrarMensaje("Vehículo cargado.");
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public int getIdTipoSeleccionado() {
        return cmbTipo.getSelectedIndex() + 1;
    }

    public String getDisponibleSeleccionado() {
        return cmbDisponible.getSelectedItem().toString();
    }

    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtPlaca() { return txtPlaca; }
    public JTextField getTxtMarca() { return txtMarca; }
    public JTextField getTxtModelo() { return txtModelo; }
    public JTextField getTxtIdUsuarioConductor() { return txtIdUsuarioConductor; }
    public JTextField getTxtLicencia() { return txtLicencia; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnConsultar() { return btnConsultar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnAsignarConductor() { return btnAsignarConductor; }
    public JButton getBtnIniciarHilo() { return btnIniciarHilo; }
    public JTable getTabla() { return tabla; }
}

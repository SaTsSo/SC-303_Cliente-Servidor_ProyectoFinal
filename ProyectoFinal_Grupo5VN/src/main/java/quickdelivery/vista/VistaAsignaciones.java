package quickdelivery.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VistaAsignaciones extends JFrame {

    private JTextField txtIdAsignacion;
    private JTextField txtIdPaquete;
    private JTextField txtIdVehiculo;
    private JTextField txtFecha;

    private JButton btnAsignar;
    private JButton btnEliminar;
    private JButton btnListar;
    private JButton btnLimpiar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    public VistaAsignaciones() {
        setTitle("QuickDelivery - Asignaciones");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel campos = new JPanel(new GridLayout(4, 2, 5, 5));

        txtIdAsignacion = new JTextField();
        txtIdPaquete = new JTextField();
        txtIdVehiculo = new JTextField();
        txtFecha = new JTextField();

        campos.add(new JLabel("ID asignación (solo eliminar):"));
        campos.add(txtIdAsignacion);
        campos.add(new JLabel("ID paquete:"));
        campos.add(txtIdPaquete);
        campos.add(new JLabel("ID vehículo:"));
        campos.add(txtIdVehiculo);
        campos.add(new JLabel("Fecha:"));
        campos.add(txtFecha);

        JPanel botones = new JPanel(new FlowLayout());
        btnAsignar = new JButton("Asignar");
        btnEliminar = new JButton("Eliminar");
        btnListar = new JButton("Listar");
        btnLimpiar = new JButton("Limpiar");
        botones.add(btnAsignar);
        botones.add(btnEliminar);
        botones.add(btnListar);
        botones.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane crearTabla() {
        String[] cols = {"ID", "ID Paquete", "ID Vehículo", "Fecha"};
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
        lblMensaje = new JLabel("Asigne un paquete a un vehículo.");
        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    public void limpiarCampos() {
        txtIdAsignacion.setText("");
        txtIdPaquete.setText("");
        txtIdVehiculo.setText("");
        txtFecha.setText("");
        mostrarMensaje("Formulario limpio.");
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public JTextField getTxtIdAsignacion() { return txtIdAsignacion; }
    public JTextField getTxtIdPaquete() { return txtIdPaquete; }
    public JTextField getTxtIdVehiculo() { return txtIdVehiculo; }
    public JTextField getTxtFecha() { return txtFecha; }
    public JButton getBtnAsignar() { return btnAsignar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTable getTabla() { return tabla; }
}

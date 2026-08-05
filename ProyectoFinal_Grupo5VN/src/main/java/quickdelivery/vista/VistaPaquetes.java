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

/**
 * Vista (MVC): interfaz gráfica Swing para el CRUD de paquetes.
 */
public class VistaPaquetes extends JFrame {

    private JTextField txtId;
    private JTextField txtDescripcion;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextField txtPeso;
    private JTextField txtEstado;
    private JTextField txtFecha;

    private JButton btnRegistrar;
    private JButton btnConsultar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnListar;
    private JButton btnLimpiar;

    private JTable tablaPaquetes;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    public VistaPaquetes() {
        setTitle("QuickDelivery - CRUD Paquetes");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelMensaje(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel campos = new JPanel(new GridLayout(7, 2, 5, 5));

        txtId = new JTextField();
        txtDescripcion = new JTextField();
        txtOrigen = new JTextField();
        txtDestino = new JTextField();
        txtPeso = new JTextField();
        txtEstado = new JTextField("1");
        txtFecha = new JTextField();

        campos.add(new JLabel("ID:"));
        campos.add(txtId);
        campos.add(new JLabel("Descripción:"));
        campos.add(txtDescripcion);
        campos.add(new JLabel("Dirección origen:"));
        campos.add(txtOrigen);
        campos.add(new JLabel("Dirección destino:"));
        campos.add(txtDestino);
        campos.add(new JLabel("Peso:"));
        campos.add(txtPeso);
        campos.add(new JLabel("ID Estado:"));
        campos.add(txtEstado);
        campos.add(new JLabel("Fecha registro:"));
        campos.add(txtFecha);

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
        String[] columnas = {
            "ID", "Descripción", "Origen", "Destino", "Peso", "Estado", "Fecha"
        };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPaquetes = new JTable(modeloTabla);
        return new JScrollPane(tablaPaquetes);
    }

    private JPanel crearPanelMensaje() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblMensaje = new JLabel(" ");
        panel.add(lblMensaje);
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    public void limpiarCampos() {
        txtId.setText("");
        txtDescripcion.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        txtPeso.setText("");
        txtEstado.setText("1");
        txtFecha.setText("");
    }

    public void cargarPaqueteEnFormulario(
            long id,
            String descripcion,
            String origen,
            String destino,
            String peso,
            int estado,
            String fecha
    ) {
        txtId.setText(String.valueOf(id));
        txtDescripcion.setText(descripcion);
        txtOrigen.setText(origen);
        txtDestino.setText(destino);
        txtPeso.setText(peso);
        txtEstado.setText(String.valueOf(estado));
        txtFecha.setText(fecha);
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFilaTabla(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtDescripcion() {
        return txtDescripcion;
    }

    public JTextField getTxtOrigen() {
        return txtOrigen;
    }

    public JTextField getTxtDestino() {
        return txtDestino;
    }

    public JTextField getTxtPeso() {
        return txtPeso;
    }

    public JTextField getTxtEstado() {
        return txtEstado;
    }

    public JTextField getTxtFecha() {
        return txtFecha;
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

    public JTable getTablaPaquetes() {
        return tablaPaquetes;
    }
}

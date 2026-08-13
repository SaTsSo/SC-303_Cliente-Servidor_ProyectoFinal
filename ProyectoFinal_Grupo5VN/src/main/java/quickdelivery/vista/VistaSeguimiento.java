package quickdelivery.vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Vista simple de seguimiento en tiempo real.
 */
public class VistaSeguimiento extends JFrame {

    private DefaultTableModel modeloPaquetes;
    private DefaultTableModel modeloVehiculos;
    private DefaultTableModel modeloUbicaciones;
    private JLabel lblEstado;

    public VistaSeguimiento() {
        setTitle("QuickDelivery - Seguimiento en tiempo real");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel titulo = new JLabel("Seguimiento (se actualiza cada 3 segundos)", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(3, 1, 8, 8));
        centro.add(crearPanelTabla("Paquetes",
                new String[]{"ID", "Descripción", "Origen", "Destino", "Estado"}, true));
        centro.add(crearPanelTabla("Vehículos",
                new String[]{"ID", "Placa", "Marca", "Modelo", "Disponible"}, false));
        centro.add(crearPanelTabla("Ubicaciones recientes",
                new String[]{"ID", "ID Vehículo", "Latitud", "Longitud", "Fecha"}, null));
        add(centro, BorderLayout.CENTER);

        lblEstado = new JLabel("Esperando datos...");
        add(lblEstado, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTabla(String titulo, String[] columnas, Boolean tipo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(titulo), BorderLayout.NORTH);

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (Boolean.TRUE.equals(tipo)) {
            modeloPaquetes = modelo;
        } else if (Boolean.FALSE.equals(tipo)) {
            modeloVehiculos = modelo;
        } else {
            modeloUbicaciones = modelo;
        }

        panel.add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        lblEstado.setText(mensaje);
    }

    public void limpiarPaquetes() {
        modeloPaquetes.setRowCount(0);
    }

    public void agregarPaquete(Object[] fila) {
        modeloPaquetes.addRow(fila);
    }

    public void limpiarVehiculos() {
        modeloVehiculos.setRowCount(0);
    }

    public void agregarVehiculo(Object[] fila) {
        modeloVehiculos.addRow(fila);
    }

    public void limpiarUbicaciones() {
        modeloUbicaciones.setRowCount(0);
    }

    public void agregarUbicacion(Object[] fila) {
        modeloUbicaciones.addRow(fila);
    }
}

package quickdelivery.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Vista (MVC): login de QuickDelivery en código (sin GUI Designer).
 */
public class VistaLogin extends JFrame {

    private JLabel lblEmpresa;
    private JLabel lblUsuario;
    private JLabel lblContra;
    private JLabel lblMensaje;

    private JTextField txtUsuario;
    private JPasswordField passContra;

    private JButton btnIngresar;
    private JButton btnSalir;

    public VistaLogin() {
        setTitle("QuickDelivery - Login");
        setSize(420, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        lblEmpresa = new JLabel("QuickDelivery S.A.", SwingConstants.CENTER);
        lblEmpresa.setFont(lblEmpresa.getFont().deriveFont(22f));
        panel.add(lblEmpresa, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField(18);

        lblContra = new JLabel("Contraseña:");
        passContra = new JPasswordField(18);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(lblContra, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(passContra, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnIngresar = new JButton("Ingresar");
        btnSalir = new JButton("Salir");
        botones.add(btnIngresar);
        botones.add(btnSalir);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(botones, gbc);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        lblMensaje = new JLabel("Ingrese usuario y contraseña.", SwingConstants.CENTER);
        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContrasena() {
        return new String(passContra.getPassword());
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getPassContra() {
        return passContra;
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }

    public JLabel getLblMensaje() {
        return lblMensaje;
    }
}

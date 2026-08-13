package quickdelivery.GUI;

import javax.swing.*;

public class Login extends javax.swing.JFrame {
    private JTextField TxtUsuario;
    private JPasswordField PassContra;
    private JButton BtnIngresar;
    private JButton BtnSalir;
    private JLabel LblEmpresa;
    private JLabel LblUsuario;
    private JLabel LblContra;
    private JLabel LblMensaje;
    private JPanel panel;

    public Login() {
        initComponents();
    }
    private void initComponents(){
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setEnabled(true);
        setTitle("QUICKDELIVERY");
        setSize(1000, 1000);
        setVisible(true);

        BtnSalir.addActionListener(e -> System.exit(0));
    }

    public JButton getBtnIngresar() {
        return BtnIngresar;
    }

    public JPasswordField getPassContra() {
        return PassContra;
    }


    public JTextField getTxtUsuario() {
        return TxtUsuario;
    }


    public JLabel getLblMensaje() {
        return LblMensaje;
    }
}

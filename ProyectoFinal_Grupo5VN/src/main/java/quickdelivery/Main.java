/*Integrantes:
Byron Benavides Hidalgo
Eber Jesus Velasquez Ramirez
Samuel Tsai Solís
Yabeth Villafuerte Sotelo
*/

package quickdelivery;

import javax.swing.SwingUtilities;
import quickdelivery.cliente.ClienteSocket;
import quickdelivery.controlador.ControladorPaquetes;
import quickdelivery.vista.VistaPaquetes;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                ClienteSocket cliente = new ClienteSocket();
                cliente.conectar();

                VistaPaquetes vista = new VistaPaquetes();
                new ControladorPaquetes(vista, cliente);

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
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "No se pudo conectar al servidor.\n"
                                + "Asegúrese de que el Servidor esté en ejecución (puerto 5200).\n"
                                + ex.getMessage(),
                        "Error de conexión",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}

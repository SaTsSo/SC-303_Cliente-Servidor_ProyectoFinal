/*Integrantes:
Byron Benavides Hidalgo
Eber Jesus Velasquez Ramirez
Samuel Tsai Solís
Yabeth Villafuerte Sotelo
*/

package quickdelivery;

import javax.swing.SwingUtilities;
import quickdelivery.controlador.ControladorLogin;
import quickdelivery.vista.VistaLogin;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin login = new VistaLogin();
            new ControladorLogin(login);
            login.setVisible(true);
        });
    }
}

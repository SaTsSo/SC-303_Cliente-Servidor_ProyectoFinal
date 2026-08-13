/*Integrantes:
Byron Benavides Hidalgo
Eber Jesus Velasquez Ramirez
Samuel Tsai Solís
Yabeth Villafuerte Sotelo
*/

package quickdelivery;

import quickdelivery.GUI.Login;
import quickdelivery.controlador.ControladorLogin;

public class Main {

    public static void main(String[] args) {
        Login login = new Login();
        new ControladorLogin(login);
        login.setVisible(true);


    }
}

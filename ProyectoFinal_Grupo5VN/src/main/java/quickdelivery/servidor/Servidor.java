package quickdelivery.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import quickdelivery.util.LogEventos;

public class Servidor {

    public static void main(String[] args) {

        ServerSocket servidor = null; // Socket del servidor.
        Socket cliente = null; // Socket del cliente.

        try {
            System.out.println("Servidor QuickDelivery iniciado.");
            LogEventos.registrar("Servidor QuickDelivery iniciado.");

            servidor = new ServerSocket(5200); // 127.0.0.1

            while (true) {

                cliente = servidor.accept();
                System.out.println("Cliente conectado.");
                LogEventos.registrar("Cliente conectado: " + cliente.getInetAddress());

                ProcesadorCliente procesador =
                        new ProcesadorCliente(cliente);
                Thread hilo = new Thread(procesador);

                hilo.start();
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.toString());
            LogEventos.registrarError("Servidor", ex);
        }
    }
}

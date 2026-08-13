package quickdelivery.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

    public class Servidor {

        public static void main(String[] args) {

            ServerSocket servidor = null;
            Socket cliente = null;

            try {
                System.out.println("Servidor QuickDelivery iniciado.");

                servidor = new ServerSocket(5200); // puerto 5200

                while (true) {

                    cliente = servidor.accept();
                    System.out.println("Cliente conectado.");

                    ProcesadorCliente procesador =
                            new ProcesadorCliente(cliente);
                    Thread hilo = new Thread(procesador);

                    hilo.start();
                }

            } catch (IOException ex) {
                System.out.println("Error: " + ex.toString());
            }
        }
    }


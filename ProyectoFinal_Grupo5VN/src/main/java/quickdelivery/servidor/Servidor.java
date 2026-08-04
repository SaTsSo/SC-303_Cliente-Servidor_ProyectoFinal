package quickdelivery.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

    public class Servidor {

        public static void main(String[] args) {

            ServerSocket servidor = null; // Socket del servidor.
            Socket cliente = null; // Socket del cliente.

            try {
                System.out.println("Servidor QuickDelivery iniciado.");

                servidor = new ServerSocket(5200); // 127.0.0.1

                while (true) {

                    cliente = servidor.accept();
                    System.out.println("Cliente conectado.");

                    ProcesadorCliente procesador =
                            new ProcesadorCliente(cliente);

                    procesador.procesar();
                }

            } catch (IOException ex) {
                System.out.println("Error: " + ex.toString());
            }
        }
    }


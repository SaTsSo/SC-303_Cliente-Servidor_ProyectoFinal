package quickdelivery.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import quickdelivery.protocolo.Protocolo;

public class ClientePrueba {

    public static void main(String[] args) {

        Socket cliente = null;
        DataInputStream entrada = null;
        DataOutputStream salida = null;

        try {

            cliente = new Socket("127.0.0.1", 5200);

            entrada = new DataInputStream(
                    cliente.getInputStream()
            );

            salida = new DataOutputStream(
                    cliente.getOutputStream()
            );

            System.out.println("Conectado al servidor.");

            salida.writeUTF(Protocolo.CONSULTAR_PAQUETE);
            salida.writeLong(1);
            salida.flush();

            String respuesta = entrada.readUTF();

            if (respuesta.equals(Protocolo.OK)) {

                long idPaquete = entrada.readLong();
                String descripcion = entrada.readUTF();
                String direccionOrigen = entrada.readUTF();
                String direccionDestino = entrada.readUTF();
                String peso = entrada.readUTF();
                int idEstado = entrada.readInt();
                String fechaRegistro = entrada.readUTF();

                System.out.println("Paquete encontrado:");
                System.out.println("ID: " + idPaquete);
                System.out.println("Descripción: " + descripcion);
                System.out.println("Origen: " + direccionOrigen);
                System.out.println("Destino: " + direccionDestino);
                System.out.println("Peso: " + peso);
                System.out.println("Estado: " + idEstado);
                System.out.println("Fecha de registro: " + fechaRegistro);

            } else {

                String mensaje = entrada.readUTF();
                System.out.println("Error: " + mensaje);
            }

            salida.writeUTF(Protocolo.DESCONECTAR);
            salida.flush();

            String respuestaDesconexion = entrada.readUTF();
            String mensajeDesconexion = entrada.readUTF();

            System.out.println("Respuesta: " + respuestaDesconexion);
            System.out.println("Mensaje: " + mensajeDesconexion);

        } catch (IOException ex) {

            System.out.println(
                    "Error en el cliente: " + ex.toString()
            );

        } finally {

            try {

                if (entrada != null) {
                    entrada.close();
                }

                if (salida != null) {
                    salida.close();
                }

                if (cliente != null) {
                    cliente.close();
                }

                System.out.println("Cliente finalizado.");

            } catch (IOException ex) {

                System.out.println(
                        "Error al cerrar el cliente: "
                                + ex.toString()
                );
            }
        }
    }
}
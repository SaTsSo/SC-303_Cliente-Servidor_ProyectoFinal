package quickdelivery.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import quickdelivery.protocolo.Protocolo;
import quickdelivery.dao.PaqueteDAO;
import quickdelivery.modelos.Paquete;

public class ProcesadorCliente implements Runnable {

    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;



    public ProcesadorCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public void procesar() {

        boolean conectado = true;

        try {

            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            while (conectado) {
                System.out.println("Esperando solicitud...");

                String solicitud = entrada.readUTF();

                System.out.println("Solicitud recibida: " + solicitud);

                if (solicitud.equals(Protocolo.DESCONECTAR)) {

                    salida.writeUTF(Protocolo.OK);
                    salida.writeUTF(
                            "Cliente desconectado correctamente."
                    );
                    salida.flush();

                    conectado = false;

                } else if (solicitud.equals(
                        Protocolo.CONSULTAR_PAQUETE)) {

                    long idPaquete = entrada.readLong();

                    System.out.println("ID recibido: " + idPaquete);

                    PaqueteDAO paqueteDAO = new PaqueteDAO();

                    System.out.println("Consultando BD...");

                    Paquete paquete =
                            paqueteDAO.consultarPaquetePorId(idPaquete);

                    System.out.println("Consulta terminada");

                    if (paquete != null) {

                        salida.writeUTF(Protocolo.OK);

                        salida.writeLong(
                                paquete.getIdPaquete()
                        );

                        // se realiza este cambio por si alguno de estos valores es null, no tire excepcion y cierre el socket
                        salida.writeUTF(
                                paquete.getDescripcion() == null ? "" : paquete.getDescripcion()
                        );
                        // se realiza este cambio por si alguno de estos valores es null, no tire excepcion y cierre el socket
                        salida.writeUTF(
                                paquete.getDireccionOrigen() == null ? "" : paquete.getDireccionOrigen()
                        );
                        // se realiza este cambio por si alguno de estos valores es null, no tire excepcion y cierre el socket
                        salida.writeUTF(
                                paquete.getDireccionDestino() == null ? "" : paquete.getDireccionDestino()
                        );
                        // se realiza este cambio por si alguno de estos valores es null, no tire excepcion y cierre el socket
                        salida.writeUTF(
                                paquete.getPeso() == null ? "" : paquete.getPeso()
                        );

                        salida.writeInt(
                                paquete.getIdEstado()
                        );
                        // se realiza este cambio por si alguno de estos valores es null, no tire excepcion y cierre el socket
                        salida.writeUTF(
                                paquete.getFechaRegistro() == null ? "" : paquete.getFechaRegistro()
                        );

                    } else {

                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF(
                                "Paquete no encontrado."
                        );
                    }

                    salida.flush();

                } else {

                    salida.writeUTF(Protocolo.ERROR);
                    salida.writeUTF(
                            "Solicitud no reconocida."
                    );
                    salida.flush();
                }
            }

        } catch (IOException ex) {

            System.out.println(
                    "Error al procesar el cliente: "
                            + ex.toString()
            );

            ex.printStackTrace();

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

                System.out.println(
                        "Conexión con el cliente finalizada."
                );

            } catch (IOException ex) {

                System.out.println(
                        "Error al cerrar la conexión: "
                                + ex.toString()
                );
            }
        }
    }

    @Override
    public void run() {
        procesar();
    }
}



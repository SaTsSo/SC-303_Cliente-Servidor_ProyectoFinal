package quickdelivery.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
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

                } else if (solicitud.equals(Protocolo.LISTAR_PAQUETES)) {

                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    List<Paquete> paquetes = paqueteDAO.listarPaquetes();

                    salida.writeUTF(Protocolo.OK);
                    salida.writeInt(paquetes.size());

                    for (Paquete paquete : paquetes) {
                        escribirPaquete(paquete);
                    }

                    salida.flush();

                } else if (solicitud.equals(Protocolo.CONSULTAR_PAQUETE)) {

                    long idPaquete = entrada.readLong();

                    System.out.println("ID recibido: " + idPaquete);

                    PaqueteDAO paqueteDAO = new PaqueteDAO();

                    System.out.println("Consultando BD...");

                    Paquete paquete =
                            paqueteDAO.consultarPaquetePorId(idPaquete);

                    System.out.println("Consulta terminada");

                    if (paquete != null) {

                        salida.writeUTF(Protocolo.OK);
                        escribirPaquete(paquete);

                    } else {

                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF(
                                "Paquete no encontrado."
                        );
                    }

                    salida.flush();

                } else if (solicitud.equals(Protocolo.INSERTAR_PAQUETE)) {

                    String descripcion = entrada.readUTF();
                    String direccionOrigen = entrada.readUTF();
                    String direccionDestino = entrada.readUTF();
                    String peso = entrada.readUTF();
                    int idEstado = entrada.readInt();
                    String fechaRegistro = entrada.readUTF();

                    Paquete paquete = new Paquete(
                            descripcion,
                            direccionOrigen,
                            direccionDestino,
                            peso,
                            idEstado,
                            fechaRegistro
                    );

                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    paqueteDAO.insertarPaquete(paquete);

                    salida.writeUTF(Protocolo.OK);
                    salida.writeUTF("Paquete insertado correctamente.");
                    salida.flush();

                } else if (solicitud.equals(Protocolo.MODIFICAR_PAQUETE)) {

                    long idPaquete = entrada.readLong();
                    String descripcion = entrada.readUTF();
                    String direccionOrigen = entrada.readUTF();
                    String direccionDestino = entrada.readUTF();
                    String peso = entrada.readUTF();

                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    Paquete existente =
                            paqueteDAO.consultarPaquetePorId(idPaquete);

                    if (existente != null) {

                        Paquete paquete = new Paquete();
                        paquete.setIdPaquete(idPaquete);
                        paquete.setDescripcion(descripcion);
                        paquete.setDireccionOrigen(direccionOrigen);
                        paquete.setDireccionDestino(direccionDestino);
                        paquete.setPeso(peso);

                        paqueteDAO.modificarPaquete(paquete);

                        salida.writeUTF(Protocolo.OK);
                        salida.writeUTF("Paquete modificado correctamente.");

                    } else {

                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF("Paquete no encontrado.");
                    }

                    salida.flush();

                } else if (solicitud.equals(Protocolo.ACTUALIZAR_ESTADO)) {

                    long idPaquete = entrada.readLong();
                    int idEstado = entrada.readInt();

                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    Paquete existente =
                            paqueteDAO.consultarPaquetePorId(idPaquete);

                    if (existente != null) {

                        paqueteDAO.actualizarEstadoPaquete(idPaquete, idEstado);

                        salida.writeUTF(Protocolo.OK);
                        salida.writeUTF(
                                "Estado del paquete actualizado correctamente."
                        );

                    } else {

                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF("Paquete no encontrado.");
                    }

                    salida.flush();

                } else if (solicitud.equals(Protocolo.ELIMINAR_PAQUETE)) {

                    long idPaquete = entrada.readLong();

                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    Paquete existente =
                            paqueteDAO.consultarPaquetePorId(idPaquete);

                    if (existente != null) {

                        paqueteDAO.eliminarPaquete(idPaquete);

                        salida.writeUTF(Protocolo.OK);
                        salida.writeUTF("Paquete eliminado correctamente.");

                    } else {

                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF("Paquete no encontrado.");
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

    private void escribirPaquete(Paquete paquete) throws IOException {

        salida.writeLong(paquete.getIdPaquete());

        // se realiza este cambio por si alguno de estos valores es null, no tire excepcion y cierre el socket
        salida.writeUTF(
                paquete.getDescripcion() == null ? "" : paquete.getDescripcion()
        );
        salida.writeUTF(
                paquete.getDireccionOrigen() == null ? "" : paquete.getDireccionOrigen()
        );
        salida.writeUTF(
                paquete.getDireccionDestino() == null ? "" : paquete.getDireccionDestino()
        );
        salida.writeUTF(
                paquete.getPeso() == null ? "" : paquete.getPeso()
        );

        salida.writeInt(paquete.getIdEstado());

        salida.writeUTF(
                paquete.getFechaRegistro() == null ? "" : paquete.getFechaRegistro()
        );
    }

    @Override
    public void run() {
        procesar();
    }
}

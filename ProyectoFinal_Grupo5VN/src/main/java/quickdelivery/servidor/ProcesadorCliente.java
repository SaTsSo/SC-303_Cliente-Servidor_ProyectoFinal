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
    private PaqueteDAO paqueteDAO;

    public ProcesadorCliente(Socket cliente) {
        this.cliente = cliente;
        this.paqueteDAO = new PaqueteDAO();
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

                try {
                    switch (solicitud) {

                        case Protocolo.LISTAR_PAQUETES:
                            listarPaquetes();
                            break;

                        case Protocolo.CONSULTAR_PAQUETE:
                            consultarPaquete();
                            break;

                        case Protocolo.INSERTAR_PAQUETE:
                            insertarPaquete();
                            break;

                        case Protocolo.MODIFICAR_PAQUETE:
                            modificarPaquete();
                            break;

                        case Protocolo.ACTUALIZAR_ESTADO:
                            actualizarEstado();
                            break;

                        case Protocolo.ELIMINAR_PAQUETE:
                            eliminarPaquete();
                            break;

                        case Protocolo.DESCONECTAR:
                            salida.writeUTF(Protocolo.OK);
                            salida.writeUTF("Cliente desconectado correctamente.");
                            salida.flush();
                            conectado = false;
                            break;

                        default:
                            salida.writeUTF(Protocolo.ERROR);
                            salida.writeUTF("Solicitud no reconocida.");
                            salida.flush();
                            break;
                    }
                } catch (Exception ex) {
                    System.out.println("Error en solicitud: " + ex.toString());
                    ex.printStackTrace();
                    try {
                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF(
                                "Error en el servidor: " + ex.getMessage()
                        );
                        salida.flush();
                    } catch (IOException ignored) {
                        conectado = false;
                    }
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

    private void listarPaquetes() throws IOException {

        List<Paquete> paquetes = paqueteDAO.listarPaquetes();

        salida.writeUTF(Protocolo.OK);
        salida.writeInt(paquetes.size());

        for (Paquete paquete : paquetes) {
            escribirPaquete(paquete);
        }

        salida.flush();
    }

    private void consultarPaquete() throws IOException {

        long idPaquete = entrada.readLong();

        System.out.println("ID recibido: " + idPaquete);
        System.out.println("Consultando BD...");

        Paquete paquete = paqueteDAO.consultarPaquetePorId(idPaquete);

        System.out.println("Consulta terminada");

        if (paquete != null) {
            salida.writeUTF(Protocolo.OK);
            escribirPaquete(paquete);
        } else {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        }

        salida.flush();
    }

    private void insertarPaquete() throws IOException {

        Paquete paquete = new Paquete();
        paquete.setDescripcion(entrada.readUTF());
        paquete.setDireccionOrigen(entrada.readUTF());
        paquete.setDireccionDestino(entrada.readUTF());
        paquete.setPeso(entrada.readUTF());
        paquete.setIdEstado(entrada.readInt());
        paquete.setFechaRegistro(entrada.readUTF());

        paqueteDAO.insertarPaquete(paquete);

        salida.writeUTF(Protocolo.OK);
        salida.writeUTF("Paquete insertado correctamente.");
        salida.flush();
    }

    private void modificarPaquete() throws IOException {

        Paquete paquete = new Paquete();
        paquete.setIdPaquete(entrada.readLong());
        paquete.setDescripcion(entrada.readUTF());
        paquete.setDireccionOrigen(entrada.readUTF());
        paquete.setDireccionDestino(entrada.readUTF());
        paquete.setPeso(entrada.readUTF());

        Paquete existente = paqueteDAO.consultarPaquetePorId(paquete.getIdPaquete());

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else {
            paqueteDAO.modificarPaquete(paquete);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Paquete modificado correctamente.");
        }

        salida.flush();
    }

    private void actualizarEstado() throws IOException {

        long idPaquete = entrada.readLong();
        int idEstado = entrada.readInt();

        Paquete existente = paqueteDAO.consultarPaquetePorId(idPaquete);

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else {
            paqueteDAO.actualizarEstadoPaquete(idPaquete, idEstado);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Estado del paquete actualizado correctamente.");
        }

        salida.flush();
    }

    private void eliminarPaquete() throws IOException {

        long idPaquete = entrada.readLong();

        Paquete existente = paqueteDAO.consultarPaquetePorId(idPaquete);

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else {
            paqueteDAO.eliminarPaquete(idPaquete);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Paquete eliminado correctamente.");
        }

        salida.flush();
    }

    private void escribirPaquete(Paquete paquete) throws IOException {
        salida.writeLong(paquete.getIdPaquete());
        salida.writeUTF(nuloAVacio(paquete.getDescripcion()));
        salida.writeUTF(nuloAVacio(paquete.getDireccionOrigen()));
        salida.writeUTF(nuloAVacio(paquete.getDireccionDestino()));
        salida.writeUTF(nuloAVacio(paquete.getPeso()));
        salida.writeInt(paquete.getIdEstado());
        salida.writeUTF(nuloAVacio(paquete.getFechaRegistro()));
    }

    private String nuloAVacio(String valor) {
        return valor == null ? "" : valor;
    }

    @Override
    public void run() {
        procesar();
    }
}

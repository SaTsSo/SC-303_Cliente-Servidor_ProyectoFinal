package quickdelivery.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import quickdelivery.protocolo.Protocolo;

/**
 * Cada vehículo corre en su propio hilo.
 * Lógica simple: se conecta, manda 3 ubicaciones y se desconecta.
 */
public class ClienteVehiculo implements Runnable {

    private int idVehiculo;

    public ClienteVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    @Override
    public void run() {
        Socket cliente = null;
        DataInputStream entrada = null;
        DataOutputStream salida = null;

        try {
            cliente = new Socket("127.0.0.1", 5200);
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            System.out.println("Vehículo " + idVehiculo + " conectado.");

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (int i = 1; i <= 3; i++) {
                String latitud = String.format("%.4f", 9.90 + (idVehiculo * 0.01) + (i * 0.001));
                String longitud = String.format("%.4f", -84.10 - (idVehiculo * 0.01) - (i * 0.001));
                String fecha = formato.format(new Date());

                salida.writeUTF(Protocolo.ACTUALIZAR_UBICACION);
                salida.writeLong(idVehiculo);
                salida.writeUTF(latitud);
                salida.writeUTF(longitud);
                salida.writeUTF(fecha);
                salida.flush();

                String respuesta = entrada.readUTF();
                String mensaje = entrada.readUTF();
                System.out.println(
                        "Vehículo " + idVehiculo + " ubicación " + i + ": "
                                + respuesta + " - " + mensaje
                );

                Thread.sleep(1000);
            }

            salida.writeUTF(Protocolo.DESCONECTAR);
            salida.flush();
            entrada.readUTF();
            entrada.readUTF();

            System.out.println("Vehículo " + idVehiculo + " finalizado.");

        } catch (Exception ex) {
            System.out.println("Error en vehículo " + idVehiculo + ": " + ex.getMessage());
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
            } catch (IOException ex) {
                System.out.println("Error al cerrar vehículo " + idVehiculo);
            }
        }
    }
}

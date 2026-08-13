package quickdelivery.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEventos {

    private static final String ARCHIVO_LOG = "ProyectoFinal_Grupo5VN/src/main/java/quickdelivery/quickdelivery_log.txt";

    public static void registrar(String evento) {

        try {
            FileWriter escritura = new FileWriter(ARCHIVO_LOG, true);
            PrintWriter salida = new PrintWriter(escritura);

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fecha = formato.format(new Date());

            salida.println("[" + fecha + "] " + evento);

            salida.close();
            escritura.close();

        } catch (IOException ex) {
            System.out.println("Error al escribir en el log: " + ex.toString());
        }
    }

    public static void registrarError(String origen, Exception ex) {
        registrar("ERROR - " + origen + " - " + ex.getMessage());
    }
}
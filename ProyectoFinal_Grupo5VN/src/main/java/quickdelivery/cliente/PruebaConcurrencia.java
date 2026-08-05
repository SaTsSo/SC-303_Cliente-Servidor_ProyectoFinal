package quickdelivery.cliente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PruebaConcurrencia {

    public static void main(String[] args) {

        // Pool con 10 hilos simultáneos
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Crear 25 vehículos
        for (int i = 1; i <= 25; i++) {

            executor.execute(new ClienteVehiculo(i));

        }

        executor.shutdown();

        System.out.println("Todos los vehículos fueron enviados al servidor.");

    }
}
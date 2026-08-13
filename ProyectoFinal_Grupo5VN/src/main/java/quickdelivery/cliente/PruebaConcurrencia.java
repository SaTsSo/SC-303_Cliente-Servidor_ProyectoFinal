package quickdelivery.cliente;

public class PruebaConcurrencia {

    public static void main(String[] args) {

        for (int i = 1; i <= 5; i++) {
            Thread hilo = new Thread(new ClienteVehiculo(i));
            hilo.start();
        }

        System.out.println("Se iniciaron 5 hilos de vehículos.");
    }
}

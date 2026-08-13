package quickdelivery.modelos;

public class Furgoneta extends Vehiculo {

    @Override
    public String obtenerNombreTipo() {
        return "Furgoneta";
    }

    @Override
    public String obtenerCapacidad() {
        return "500kg";
    }
}

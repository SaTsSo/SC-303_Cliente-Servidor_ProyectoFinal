package quickdelivery.modelos;

public class Camion extends Vehiculo {

    @Override
    public String obtenerNombreTipo() {
        return "Camion";
    }

    @Override
    public String obtenerCapacidad() {
        return "2000kg";
    }
}

package quickdelivery.modelos;

public class Moto extends Vehiculo {

    @Override
    public String obtenerNombreTipo() {
        return "Moto";
    }

    @Override
    public String obtenerCapacidad() {
        return "20kg";
    }
}

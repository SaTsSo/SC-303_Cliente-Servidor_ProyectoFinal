package quickdelivery.modelos;

public class Carro extends Vehiculo {

    @Override
    public String obtenerNombreTipo() {
        return "Carro";
    }

    @Override
    public String obtenerCapacidad() {
        return "400kg";
    }
}

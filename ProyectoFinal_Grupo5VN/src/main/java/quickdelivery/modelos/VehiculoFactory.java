package quickdelivery.modelos;

public class VehiculoFactory {

    public static Vehiculo crear(int idTipoVehiculo) {
        Vehiculo vehiculo;

        switch (idTipoVehiculo) {
            case 1:
                vehiculo = new Moto();
                break;
            case 2:
                vehiculo = new Furgoneta();
                break;
            case 3:
                vehiculo = new Camion();
                break;
            case 4:
                vehiculo = new Carro();
                break;
            default:
                vehiculo = new Vehiculo();
                break;
        }

        vehiculo.setIdTipoVehiculo(idTipoVehiculo);
        return vehiculo;
    }
}

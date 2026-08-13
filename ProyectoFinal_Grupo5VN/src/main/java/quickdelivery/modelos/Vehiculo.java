package quickdelivery.modelos;

/**
 * Clase padre de los vehículos (herencia).
 */
public class Vehiculo {

    private long idVehiculo;
    private String placa;
    private String marca;
    private String modelo;
    private int idTipoVehiculo;
    private String disponible;

    // Método que cada hijo puede cambiar (polimorfismo)
    public String obtenerNombreTipo() {
        return "Vehiculo";
    }

    // Método que cada hijo puede cambiar (polimorfismo)
    public String obtenerCapacidad() {
        return "N/D";
    }

    public String obtenerResumen() {
        return obtenerNombreTipo() + " - capacidad: " + obtenerCapacidad();
    }

    public long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getIdTipoVehiculo() {
        return idTipoVehiculo;
    }

    public void setIdTipoVehiculo(int idTipoVehiculo) {
        this.idTipoVehiculo = idTipoVehiculo;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }
}

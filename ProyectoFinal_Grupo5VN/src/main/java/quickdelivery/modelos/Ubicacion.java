package quickdelivery.modelos;

public class Ubicacion {

    private long idUbicacion;
    private long idVehiculo;
    private String latitud;
    private String longitud;
    private String fechaHora;

    public long getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(long idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}

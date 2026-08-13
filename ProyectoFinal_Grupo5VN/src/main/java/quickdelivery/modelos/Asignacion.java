package quickdelivery.modelos;

public class Asignacion {

    private long idAsignacion;
    private long idPaquete;
    private long idVehiculo;
    private String fechaAsignacion;

    public long getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(long idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(long idPaquete) {
        this.idPaquete = idPaquete;
    }

    public long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
}

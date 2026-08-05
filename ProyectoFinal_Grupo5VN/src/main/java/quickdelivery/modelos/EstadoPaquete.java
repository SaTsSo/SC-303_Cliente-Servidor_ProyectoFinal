package quickdelivery.modelos;

public class EstadoPaquete {

    private int idEstado;
    private String nombreEstado;

    public EstadoPaquete(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public EstadoPaquete() {
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    @Override
    public String toString() {
        return "EstadoPaquete{" +
                "idEstado=" + idEstado +
                ", nombreEstado='" + nombreEstado + '\'' +
                '}';
    }
}
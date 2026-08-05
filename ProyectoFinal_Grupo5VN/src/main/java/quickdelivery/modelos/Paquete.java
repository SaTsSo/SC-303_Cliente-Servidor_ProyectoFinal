package quickdelivery.modelos;

public class Paquete {

    private long idPaquete;
    private String descripcion;
    private String direccionOrigen;
    private String direccionDestino;
    private String peso;
    private int idEstado;
    private String fechaRegistro;

    public Paquete(String descripcion, String direccionOrigen, String direccionDestino,
                   String peso, int idEstado, String fechaRegistro) {
        this.descripcion = descripcion;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.peso = peso;
        this.idEstado = idEstado;
        this.fechaRegistro = fechaRegistro;
    }

    public Paquete() {
    }

    public long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(long idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "idPaquete=" + idPaquete +
                ", descripcion='" + descripcion + '\'' +
                ", direccionOrigen='" + direccionOrigen + '\'' +
                ", direccionDestino='" + direccionDestino + '\'' +
                ", peso='" + peso + '\'' +
                ", idEstado=" + idEstado +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                '}';
    }
}
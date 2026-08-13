package quickdelivery.util;
import quickdelivery.modelos.Sesion;

public class Permisos {
    public static boolean esAdministrador() {
        return Sesion.getUsuarioActual() != null
                && Sesion.getUsuarioActual().getIdRol() == Roles.ADMINISTRADOR;
    }

    public static boolean esDespachador() {
        return Sesion.getUsuarioActual() != null
                && Sesion.getUsuarioActual().getIdRol() == Roles.DESPACHADOR;
    }

    public static boolean esConductor() {
        return Sesion.getUsuarioActual() != null
                && Sesion.getUsuarioActual().getIdRol() == Roles.CONDUCTOR;
    }

    public static boolean puedeGestionarUsuarios() {
        return esAdministrador();
    }

    public static boolean puedeGestionarVehiculos() {
        return esAdministrador() || esDespachador();
    }

    public static boolean puedeGestionarPaquetes() {
        return esAdministrador() || esDespachador();
    }

    public static boolean puedeAsignarPaquetes() {
        return esAdministrador() || esDespachador();
    }

    public static boolean puedeVerPaquetesAsignados() {
        return esAdministrador() || esConductor();
    }

    public static boolean puedeActualizarEstadoPaquete() {
        return esAdministrador() || esConductor();
    }

    public static boolean puedeRegistrarIncidencias() {
        return esAdministrador() || esConductor();
    }

    public static boolean puedeVerLogs() {
        return esAdministrador();
    }

}

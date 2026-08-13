package quickdelivery.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import quickdelivery.modelos.Asignacion;
import quickdelivery.modelos.Paquete;
import quickdelivery.modelos.Ubicacion;
import quickdelivery.modelos.Usuario;
import quickdelivery.modelos.Vehiculo;
import quickdelivery.modelos.VehiculoFactory;
import quickdelivery.protocolo.Protocolo;

// cliente socket
public class ClienteSocket {

    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 5200;

    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;

    public void conectar() throws IOException {
        socket = new Socket(HOST, PUERTO);
        entrada = new DataInputStream(socket.getInputStream());
        salida = new DataOutputStream(socket.getOutputStream());
    }

    public void desconectar() throws IOException {
        if (salida != null) {
            salida.writeUTF(Protocolo.DESCONECTAR);
            salida.flush();
            entrada.readUTF();
            entrada.readUTF();
        }
        cerrar();
    }

    public Usuario login(String nombreUsuario, String contrasena) throws IOException {
        salida.writeUTF(Protocolo.LOGIN);
        salida.writeUTF(nombreUsuario);
        salida.writeUTF(contrasena);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            entrada.readUTF();
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(entrada.readLong());
        usuario.setNombreUsuario(entrada.readUTF());
        usuario.setNombreCompleto(entrada.readUTF());
        usuario.setEmail(entrada.readUTF());
        usuario.setIdRol(entrada.readInt());
        return usuario;
    }

    public List<Usuario> listarUsuarios() throws IOException {
        salida.writeUTF(Protocolo.LISTAR_USUARIOS);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        int cantidad = entrada.readInt();
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            usuarios.add(leerUsuario());
        }

        return usuarios;
    }

    public Usuario consultarUsuario(long id) throws IOException {
        salida.writeUTF(Protocolo.CONSULTAR_USUARIO);
        salida.writeLong(id);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        return leerUsuario();
    }

    public void insertarUsuario(Usuario usuario) throws IOException {
        salida.writeUTF(Protocolo.INSERTAR_USUARIO);
        salida.writeUTF(usuario.getNombreUsuario());
        salida.writeUTF(usuario.getContrasena());
        salida.writeUTF(usuario.getNombreCompleto());
        salida.writeUTF(usuario.getEmail());
        salida.writeInt(usuario.getIdRol());
        salida.flush();

        leerRespuestaSimple();
    }

    public void modificarUsuario(Usuario usuario) throws IOException {
        salida.writeUTF(Protocolo.MODIFICAR_USUARIO);
        salida.writeLong(usuario.getIdUsuario());
        salida.writeUTF(usuario.getNombreUsuario());
        salida.writeUTF(usuario.getContrasena() == null ? "" : usuario.getContrasena());
        salida.writeUTF(usuario.getNombreCompleto());
        salida.writeUTF(usuario.getEmail());
        salida.writeInt(usuario.getIdRol());
        salida.flush();

        leerRespuestaSimple();
    }

    public void eliminarUsuario(long id) throws IOException {
        salida.writeUTF(Protocolo.ELIMINAR_USUARIO);
        salida.writeLong(id);
        salida.flush();

        leerRespuestaSimple();
    }

    public List<Vehiculo> listarVehiculos() throws IOException {
        salida.writeUTF(Protocolo.LISTAR_VEHICULOS);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        int cantidad = entrada.readInt();
        List<Vehiculo> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            lista.add(leerVehiculo());
        }
        return lista;
    }

    public Vehiculo consultarVehiculo(long id) throws IOException {
        salida.writeUTF(Protocolo.CONSULTAR_VEHICULO);
        salida.writeLong(id);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }
        return leerVehiculo();
    }

    public void insertarVehiculo(Vehiculo vehiculo) throws IOException {
        salida.writeUTF(Protocolo.INSERTAR_VEHICULO);
        salida.writeUTF(vehiculo.getPlaca());
        salida.writeUTF(vehiculo.getMarca() == null ? "" : vehiculo.getMarca());
        salida.writeUTF(vehiculo.getModelo() == null ? "" : vehiculo.getModelo());
        salida.writeInt(vehiculo.getIdTipoVehiculo());
        salida.writeUTF(vehiculo.getDisponible());
        salida.flush();
        leerRespuestaSimple();
    }

    public void modificarVehiculo(Vehiculo vehiculo) throws IOException {
        salida.writeUTF(Protocolo.MODIFICAR_VEHICULO);
        salida.writeLong(vehiculo.getIdVehiculo());
        salida.writeUTF(vehiculo.getPlaca());
        salida.writeUTF(vehiculo.getMarca() == null ? "" : vehiculo.getMarca());
        salida.writeUTF(vehiculo.getModelo() == null ? "" : vehiculo.getModelo());
        salida.writeInt(vehiculo.getIdTipoVehiculo());
        salida.writeUTF(vehiculo.getDisponible());
        salida.flush();
        leerRespuestaSimple();
    }

    public void eliminarVehiculo(long id) throws IOException {
        salida.writeUTF(Protocolo.ELIMINAR_VEHICULO);
        salida.writeLong(id);
        salida.flush();
        leerRespuestaSimple();
    }

    public void asignarConductor(long idUsuario, String licencia, long idVehiculo) throws IOException {
        salida.writeUTF(Protocolo.ASIGNAR_CONDUCTOR);
        salida.writeLong(idUsuario);
        salida.writeUTF(licencia);
        salida.writeLong(idVehiculo);
        salida.flush();
        leerRespuestaSimple();
    }

    public void actualizarUbicacion(long idVehiculo, String latitud, String longitud, String fechaHora)
            throws IOException {
        salida.writeUTF(Protocolo.ACTUALIZAR_UBICACION);
        salida.writeLong(idVehiculo);
        salida.writeUTF(latitud);
        salida.writeUTF(longitud);
        salida.writeUTF(fechaHora);
        salida.flush();
        leerRespuestaSimple();
    }

    public List<Asignacion> listarAsignaciones() throws IOException {
        salida.writeUTF(Protocolo.LISTAR_ASIGNACIONES);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        int cantidad = entrada.readInt();
        List<Asignacion> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Asignacion a = new Asignacion();
            a.setIdAsignacion(entrada.readLong());
            a.setIdPaquete(entrada.readLong());
            a.setIdVehiculo(entrada.readLong());
            a.setFechaAsignacion(entrada.readUTF());
            lista.add(a);
        }
        return lista;
    }

    public void insertarAsignacion(Asignacion asignacion) throws IOException {
        salida.writeUTF(Protocolo.INSERTAR_ASIGNACION);
        salida.writeLong(asignacion.getIdPaquete());
        salida.writeLong(asignacion.getIdVehiculo());
        salida.writeUTF(asignacion.getFechaAsignacion());
        salida.flush();
        leerRespuestaSimple();
    }

    public void eliminarAsignacion(long id) throws IOException {
        salida.writeUTF(Protocolo.ELIMINAR_ASIGNACION);
        salida.writeLong(id);
        salida.flush();
        leerRespuestaSimple();
    }

    public List<Ubicacion> listarUbicaciones() throws IOException {
        salida.writeUTF(Protocolo.LISTAR_UBICACIONES);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        int cantidad = entrada.readInt();
        List<Ubicacion> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Ubicacion u = new Ubicacion();
            u.setIdUbicacion(entrada.readLong());
            u.setIdVehiculo(entrada.readLong());
            u.setLatitud(entrada.readUTF());
            u.setLongitud(entrada.readUTF());
            u.setFechaHora(entrada.readUTF());
            lista.add(u);
        }
        return lista;
    }

    public List<Paquete> listarPaquetes() throws IOException {
        salida.writeUTF(Protocolo.LISTAR_PAQUETES);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        int cantidad = entrada.readInt();
        List<Paquete> paquetes = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            paquetes.add(leerPaquete());
        }

        return paquetes;
    }

    public Paquete consultarPaquete(long id) throws IOException {
        salida.writeUTF(Protocolo.CONSULTAR_PAQUETE);
        salida.writeLong(id);
        salida.flush();

        String respuesta = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(entrada.readUTF());
        }

        return leerPaquete();
    }

    public void insertarPaquete(Paquete paquete) throws IOException {
        salida.writeUTF(Protocolo.INSERTAR_PAQUETE);
        salida.writeUTF(paquete.getDescripcion());
        salida.writeUTF(paquete.getDireccionOrigen());
        salida.writeUTF(paquete.getDireccionDestino());
        salida.writeUTF(paquete.getPeso());
        salida.writeInt(paquete.getIdEstado());
        salida.writeUTF(paquete.getFechaRegistro());
        salida.flush();

        leerRespuestaSimple();
    }

    public void modificarPaquete(Paquete paquete) throws IOException {
        salida.writeUTF(Protocolo.MODIFICAR_PAQUETE);
        salida.writeLong(paquete.getIdPaquete());
        salida.writeUTF(paquete.getDescripcion());
        salida.writeUTF(paquete.getDireccionOrigen());
        salida.writeUTF(paquete.getDireccionDestino());
        salida.writeUTF(paquete.getPeso());
        salida.flush();

        leerRespuestaSimple();
    }

    public void eliminarPaquete(long id) throws IOException {
        salida.writeUTF(Protocolo.ELIMINAR_PAQUETE);
        salida.writeLong(id);
        salida.flush();

        leerRespuestaSimple();
    }

    private Usuario leerUsuario() throws IOException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(entrada.readLong());
        usuario.setNombreUsuario(entrada.readUTF());
        usuario.setNombreCompleto(entrada.readUTF());
        usuario.setEmail(entrada.readUTF());
        usuario.setIdRol(entrada.readInt());
        return usuario;
    }

    private Vehiculo leerVehiculo() throws IOException {
        long id = entrada.readLong();
        String placa = entrada.readUTF();
        String marca = entrada.readUTF();
        String modelo = entrada.readUTF();
        int idTipo = entrada.readInt();
        String disponible = entrada.readUTF();

        Vehiculo v = VehiculoFactory.crear(idTipo);
        v.setIdVehiculo(id);
        v.setPlaca(placa);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setDisponible(disponible);
        return v;
    }

    private Paquete leerPaquete() throws IOException {
        Paquete paquete = new Paquete();
        paquete.setIdPaquete(entrada.readLong());
        paquete.setDescripcion(entrada.readUTF());
        paquete.setDireccionOrigen(entrada.readUTF());
        paquete.setDireccionDestino(entrada.readUTF());
        paquete.setPeso(entrada.readUTF());
        paquete.setIdEstado(entrada.readInt());
        paquete.setFechaRegistro(entrada.readUTF());
        return paquete;
    }

    private void leerRespuestaSimple() throws IOException {
        String respuesta = entrada.readUTF();
        String mensaje = entrada.readUTF();
        if (!respuesta.equals(Protocolo.OK)) {
            throw new IOException(mensaje);
        }
    }

    private void cerrar() throws IOException {
        if (entrada != null) {
            entrada.close();
        }
        if (salida != null) {
            salida.close();
        }
        if (socket != null) {
            socket.close();
        }
    }
}

package quickdelivery.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import quickdelivery.modelos.Paquete;
import quickdelivery.modelos.Usuario;
import quickdelivery.protocolo.Protocolo;

/**
 * Cliente socket usado por la capa Modelo/Controlador de la GUI
 * para enviar y recibir operaciones CRUD según el protocolo.
 */
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

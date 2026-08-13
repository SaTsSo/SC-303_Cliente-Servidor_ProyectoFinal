package quickdelivery.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import quickdelivery.protocolo.Protocolo;
import quickdelivery.dao.AsignacionDAO;
import quickdelivery.dao.PaqueteDAO;
import quickdelivery.dao.UsuarioDAO;
import quickdelivery.dao.VehiculoDAO;
import quickdelivery.modelos.Asignacion;
import quickdelivery.modelos.Paquete;
import quickdelivery.modelos.Ubicacion;
import quickdelivery.modelos.Usuario;
import quickdelivery.modelos.Vehiculo;
import quickdelivery.modelos.VehiculoFactory;
import quickdelivery.util.LogEventos;

public class ProcesadorCliente implements Runnable {

    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private PaqueteDAO paqueteDAO;
    private UsuarioDAO usuarioDAO;
    private VehiculoDAO vehiculoDAO;
    private AsignacionDAO asignacionDAO;

    public ProcesadorCliente(Socket cliente) {
        this.cliente = cliente;
        this.paqueteDAO = new PaqueteDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.vehiculoDAO = new VehiculoDAO();
        this.asignacionDAO = new AsignacionDAO();
    }

    public void procesar() {

        boolean conectado = true;

        try {

            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            while (conectado) {
                System.out.println("Esperando solicitud...");

                String solicitud = entrada.readUTF();

                System.out.println("Solicitud recibida: " + solicitud);

                try {
                    switch (solicitud) {

                        case Protocolo.LOGIN:
                            login();
                            break;

                        case Protocolo.LISTAR_USUARIOS:
                            listarUsuarios();
                            break;

                        case Protocolo.CONSULTAR_USUARIO:
                            consultarUsuario();
                            break;

                        case Protocolo.INSERTAR_USUARIO:
                            insertarUsuario();
                            break;

                        case Protocolo.MODIFICAR_USUARIO:
                            modificarUsuario();
                            break;

                        case Protocolo.ELIMINAR_USUARIO:
                            eliminarUsuario();
                            break;

                        case Protocolo.LISTAR_VEHICULOS:
                            listarVehiculos();
                            break;

                        case Protocolo.CONSULTAR_VEHICULO:
                            consultarVehiculo();
                            break;

                        case Protocolo.INSERTAR_VEHICULO:
                            insertarVehiculo();
                            break;

                        case Protocolo.MODIFICAR_VEHICULO:
                            modificarVehiculo();
                            break;

                        case Protocolo.ELIMINAR_VEHICULO:
                            eliminarVehiculo();
                            break;

                        case Protocolo.ASIGNAR_CONDUCTOR:
                            asignarConductor();
                            break;

                        case Protocolo.ACTUALIZAR_UBICACION:
                            actualizarUbicacion();
                            break;

                        case Protocolo.LISTAR_ASIGNACIONES:
                            listarAsignaciones();
                            break;

                        case Protocolo.INSERTAR_ASIGNACION:
                            insertarAsignacion();
                            break;

                        case Protocolo.ELIMINAR_ASIGNACION:
                            eliminarAsignacion();
                            break;

                        case Protocolo.LISTAR_UBICACIONES:
                            listarUbicaciones();
                            break;

                        case Protocolo.LISTAR_PAQUETES:
                            listarPaquetes();
                            break;

                        case Protocolo.CONSULTAR_PAQUETE:
                            consultarPaquete();
                            break;

                        case Protocolo.INSERTAR_PAQUETE:
                            insertarPaquete();
                            break;

                        case Protocolo.MODIFICAR_PAQUETE:
                            modificarPaquete();
                            break;

                        case Protocolo.ACTUALIZAR_ESTADO:
                            actualizarEstado();
                            break;

                        case Protocolo.ELIMINAR_PAQUETE:
                            eliminarPaquete();
                            break;

                        case Protocolo.DESCONECTAR:
                            salida.writeUTF(Protocolo.OK);
                            salida.writeUTF("Cliente desconectado correctamente.");
                            salida.flush();
                            conectado = false;
                            break;

                        default:
                            salida.writeUTF(Protocolo.ERROR);
                            salida.writeUTF("Solicitud no reconocida.");
                            salida.flush();
                            LogEventos.registrar("Solicitud no reconocida: " + solicitud);
                            break;
                    }
                } catch (Exception ex) {
                    System.out.println("Error en solicitud: " + ex.toString());
                    ex.printStackTrace();
                    LogEventos.registrarError("Solicitud " + solicitud, ex);
                    try {
                        salida.writeUTF(Protocolo.ERROR);
                        salida.writeUTF(
                                "Error en el servidor: " + ex.getMessage()
                        );
                        salida.flush();
                    } catch (IOException ignored) {
                        conectado = false;
                    }
                }
            }

        } catch (IOException ex) {

            System.out.println(
                    "Error al procesar el cliente: "
                            + ex.toString()
            );

            ex.printStackTrace();
            LogEventos.registrarError("Conexión con el cliente", ex);

        } finally {

            try {

                if (entrada != null) {
                    entrada.close();
                }

                if (salida != null) {
                    salida.close();
                }

                if (cliente != null) {
                    cliente.close();
                }

                System.out.println(
                        "Conexión con el cliente finalizada."
                );
                LogEventos.registrar("Conexión con el cliente finalizada.");

            } catch (IOException ex) {

                System.out.println(
                        "Error al cerrar la conexión: "
                                + ex.toString()
                );
                LogEventos.registrarError("Cierre de conexión", ex);
            }
        }
    }

    private void login() throws IOException {
        String nombreUsuario = entrada.readUTF();
        String contrasena = entrada.readUTF();

        Usuario usuario = usuarioDAO.loginUsuario(nombreUsuario, contrasena);

        if (usuario != null) {
            salida.writeUTF(Protocolo.OK);
            salida.writeLong(usuario.getIdUsuario());
            salida.writeUTF(nuloAVacio(usuario.getNombreUsuario()));
            salida.writeUTF(nuloAVacio(usuario.getNombreCompleto()));
            salida.writeUTF(nuloAVacio(usuario.getEmail()));
            salida.writeInt(usuario.getIdRol());
            LogEventos.registrar("Login exitoso: " + usuario.getNombreUsuario());
        } else {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Usuario o contraseña incorrectos.");
            LogEventos.registrar("Login fallido: " + nombreUsuario);
        }

        salida.flush();
    }

    private void listarUsuarios() throws IOException {
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();

        salida.writeUTF(Protocolo.OK);
        salida.writeInt(usuarios.size());

        for (Usuario usuario : usuarios) {
            escribirUsuario(usuario);
        }

        salida.flush();
    }

    private void consultarUsuario() throws IOException {
        long idUsuario = entrada.readLong();

        if (idUsuario <= 0) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("El ID del usuario no es valido.");
            salida.flush();
            return;
        }

        Usuario usuario = usuarioDAO.consultarUsuarioPorId(idUsuario);

        if (usuario != null) {
            salida.writeUTF(Protocolo.OK);
            escribirUsuario(usuario);
        } else {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Usuario no encontrado.");
        }

        salida.flush();
    }

    private void insertarUsuario() throws IOException {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(entrada.readUTF());
        usuario.setContrasena(entrada.readUTF());
        usuario.setNombreCompleto(entrada.readUTF());
        usuario.setEmail(entrada.readUTF());
        usuario.setIdRol(entrada.readInt());

        if (!validarDatosUsuario(usuario, true)) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Los datos ingresados no son validos.");
            salida.flush();
            return;
        }

        usuarioDAO.insertarUsuario(usuario);
        LogEventos.registrar("Usuario insertado: " + usuario.getNombreUsuario());
        salida.writeUTF(Protocolo.OK);
        salida.writeUTF("Usuario insertado correctamente.");
        salida.flush();
    }

    private void modificarUsuario() throws IOException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(entrada.readLong());
        usuario.setNombreUsuario(entrada.readUTF());
        usuario.setContrasena(entrada.readUTF());
        usuario.setNombreCompleto(entrada.readUTF());
        usuario.setEmail(entrada.readUTF());
        usuario.setIdRol(entrada.readInt());

        if (usuario.getIdUsuario() <= 0 || !validarDatosUsuario(usuario, false)) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Los datos ingresados no son validos.");
            salida.flush();
            return;
        }

        Usuario existente = usuarioDAO.consultarUsuarioPorId(usuario.getIdUsuario());

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Usuario no encontrado.");
        } else {
            usuarioDAO.modificarUsuario(usuario);
            LogEventos.registrar("Usuario modificado: idUsuario=" + usuario.getIdUsuario());
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Usuario modificado correctamente.");
        }

        salida.flush();
    }

    private void eliminarUsuario() throws IOException {
        long idUsuario = entrada.readLong();

        if (idUsuario <= 0) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("El ID del usuario no es valido.");
            salida.flush();
            return;
        }

        Usuario existente = usuarioDAO.consultarUsuarioPorId(idUsuario);

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Usuario no encontrado.");
        } else {
            usuarioDAO.eliminarUsuario(idUsuario);
            LogEventos.registrar("Usuario eliminado: idUsuario=" + idUsuario);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Usuario eliminado correctamente.");
        }

        salida.flush();
    }

    private boolean validarDatosUsuario(Usuario usuario, boolean contrasenaObligatoria) {
        if (usuario == null) {
            return false;
        }

        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            return false;
        }

        if (contrasenaObligatoria
                && (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty())) {
            return false;
        }

        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().trim().isEmpty()) {
            return false;
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            return false;
        }

        int idRol = usuario.getIdRol();
        return idRol >= 1 && idRol <= 3;
    }

    private void escribirUsuario(Usuario usuario) throws IOException {
        salida.writeLong(usuario.getIdUsuario());
        salida.writeUTF(nuloAVacio(usuario.getNombreUsuario()));
        salida.writeUTF(nuloAVacio(usuario.getNombreCompleto()));
        salida.writeUTF(nuloAVacio(usuario.getEmail()));
        salida.writeInt(usuario.getIdRol());
    }

    private void listarVehiculos() throws IOException {
        List<Vehiculo> lista = vehiculoDAO.listarVehiculos();
        salida.writeUTF(Protocolo.OK);
        salida.writeInt(lista.size());
        for (Vehiculo v : lista) {
            escribirVehiculo(v);
        }
        salida.flush();
    }

    private void consultarVehiculo() throws IOException {
        long id = entrada.readLong();
        Vehiculo v = vehiculoDAO.consultarPorId(id);
        if (v != null) {
            salida.writeUTF(Protocolo.OK);
            escribirVehiculo(v);
        } else {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Vehículo no encontrado.");
        }
        salida.flush();
    }

    private void insertarVehiculo() throws IOException {
        String placa = entrada.readUTF();
        String marca = entrada.readUTF();
        String modelo = entrada.readUTF();
        int idTipo = entrada.readInt();
        String disponible = entrada.readUTF();

        Vehiculo v = VehiculoFactory.crear(idTipo);
        v.setPlaca(placa);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setDisponible(disponible);

        if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("La placa es obligatoria.");
        } else {
            vehiculoDAO.insertar(v);
            LogEventos.registrar("Vehículo insertado: " + v.getPlaca());
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Vehículo insertado (" + v.obtenerResumen() + ").");
        }
        salida.flush();
    }

    private void modificarVehiculo() throws IOException {
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

        if (vehiculoDAO.consultarPorId(v.getIdVehiculo()) == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Vehículo no encontrado.");
        } else {
            vehiculoDAO.modificar(v);
            LogEventos.registrar("Vehículo modificado: idVehiculo=" + v.getIdVehiculo());
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Vehículo modificado (" + v.obtenerResumen() + ").");
        }
        salida.flush();
    }

    private void eliminarVehiculo() throws IOException {
        long id = entrada.readLong();
        if (vehiculoDAO.consultarPorId(id) == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Vehículo no encontrado.");
        } else {
            vehiculoDAO.eliminar(id);
            LogEventos.registrar("Vehículo eliminado: idVehiculo=" + id);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Vehículo eliminado.");
        }
        salida.flush();
    }

    private void asignarConductor() throws IOException {
        long idUsuario = entrada.readLong();
        String licencia = entrada.readUTF();
        long idVehiculo = entrada.readLong();

        if (vehiculoDAO.consultarPorId(idVehiculo) == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Vehículo no encontrado.");
        } else {
            vehiculoDAO.asignarConductor(idUsuario, licencia, idVehiculo);
            LogEventos.registrar("Conductor asignado: idUsuario=" + idUsuario
                    + ", idVehiculo=" + idVehiculo);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Conductor asignado.");
        }
        salida.flush();
    }

    private void actualizarUbicacion() throws IOException {
        long idVehiculo = entrada.readLong();
        String latitud = entrada.readUTF();
        String longitud = entrada.readUTF();
        String fechaHora = entrada.readUTF();

        if (vehiculoDAO.consultarPorId(idVehiculo) == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Vehículo no encontrado.");
        } else {
            vehiculoDAO.guardarUbicacion(idVehiculo, latitud, longitud, fechaHora);
            vehiculoDAO.actualizarDisponible(idVehiculo, "NO");
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Ubicación actualizada.");
        }
        salida.flush();
    }

    private void listarAsignaciones() throws IOException {
        List<Asignacion> lista = asignacionDAO.listarAsignaciones();
        salida.writeUTF(Protocolo.OK);
        salida.writeInt(lista.size());
        for (Asignacion a : lista) {
            salida.writeLong(a.getIdAsignacion());
            salida.writeLong(a.getIdPaquete());
            salida.writeLong(a.getIdVehiculo());
            salida.writeUTF(nuloAVacio(a.getFechaAsignacion()));
        }
        salida.flush();
    }

    private void insertarAsignacion() throws IOException {
        Asignacion a = new Asignacion();
        a.setIdPaquete(entrada.readLong());
        a.setIdVehiculo(entrada.readLong());
        a.setFechaAsignacion(entrada.readUTF());

        if (paqueteDAO.consultarPaquetePorId(a.getIdPaquete()) == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else if (vehiculoDAO.consultarPorId(a.getIdVehiculo()) == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Vehículo no encontrado.");
        } else {
            asignacionDAO.insertar(a);
            paqueteDAO.actualizarEstadoPaquete(a.getIdPaquete(), 2);
            vehiculoDAO.actualizarDisponible(a.getIdVehiculo(), "NO");
            LogEventos.registrar("Paquete asignado: idPaquete=" + a.getIdPaquete()
                    + ", idVehiculo=" + a.getIdVehiculo());
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Asignación creada.");
        }
        salida.flush();
    }

    private void eliminarAsignacion() throws IOException {
        long id = entrada.readLong();
        asignacionDAO.eliminar(id);
        LogEventos.registrar("Asignación eliminada: idAsignacion=" + id);
        salida.writeUTF(Protocolo.OK);
        salida.writeUTF("Asignación eliminada.");
        salida.flush();
    }

    private void listarUbicaciones() throws IOException {
        List<Ubicacion> lista = vehiculoDAO.listarUbicaciones();
        salida.writeUTF(Protocolo.OK);
        salida.writeInt(lista.size());
        for (Ubicacion u : lista) {
            salida.writeLong(u.getIdUbicacion());
            salida.writeLong(u.getIdVehiculo());
            salida.writeUTF(nuloAVacio(u.getLatitud()));
            salida.writeUTF(nuloAVacio(u.getLongitud()));
            salida.writeUTF(nuloAVacio(u.getFechaHora()));
        }
        salida.flush();
    }

    private void escribirVehiculo(Vehiculo v) throws IOException {
        salida.writeLong(v.getIdVehiculo());
        salida.writeUTF(nuloAVacio(v.getPlaca()));
        salida.writeUTF(nuloAVacio(v.getMarca()));
        salida.writeUTF(nuloAVacio(v.getModelo()));
        salida.writeInt(v.getIdTipoVehiculo());
        salida.writeUTF(nuloAVacio(v.getDisponible()));
    }

    private void listarPaquetes() throws IOException {

        List<Paquete> paquetes = paqueteDAO.listarPaquetes();

        salida.writeUTF(Protocolo.OK);
        salida.writeInt(paquetes.size());

        for (Paquete paquete : paquetes) {
            escribirPaquete(paquete);
        }

        salida.flush();
    }

    private void consultarPaquete() throws IOException {

        long idPaquete = entrada.readLong();

        System.out.println("ID recibido: " + idPaquete);

        if (idPaquete <= 0) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("El ID del paquete no es valido.");
            salida.flush();
            return;
        }

        System.out.println("Consultando BD...");
        Paquete paquete = paqueteDAO.consultarPaquetePorId(idPaquete);

        System.out.println("Consulta terminada");

        if (paquete != null) {
            salida.writeUTF(Protocolo.OK);
            escribirPaquete(paquete);
        } else {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        }

        salida.flush();
    }

    private void insertarPaquete() throws IOException {

        Paquete paquete = new Paquete();
        paquete.setDescripcion(entrada.readUTF());
        paquete.setDireccionOrigen(entrada.readUTF());
        paquete.setDireccionDestino(entrada.readUTF());
        paquete.setPeso(entrada.readUTF());
        paquete.setIdEstado(entrada.readInt());
        paquete.setFechaRegistro(entrada.readUTF());

        if (!validarDatosPaquete(paquete)) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Los datos ingresados no son validos.");
            salida.flush();
            return;
        }

        paqueteDAO.insertarPaquete(paquete);
        LogEventos.registrar("Paquete registrado: " + paquete.getDescripcion()
                + " (" + paquete.getDireccionOrigen() + " -> "
                + paquete.getDireccionDestino() + ")");

        salida.writeUTF(Protocolo.OK);
        salida.writeUTF("Paquete insertado correctamente.");
        salida.flush();
    }

    private void modificarPaquete() throws IOException {

        Paquete paquete = new Paquete();
        paquete.setIdPaquete(entrada.readLong());
        paquete.setDescripcion(entrada.readUTF());
        paquete.setDireccionOrigen(entrada.readUTF());
        paquete.setDireccionDestino(entrada.readUTF());
        paquete.setPeso(entrada.readUTF());

        if (paquete.getIdPaquete() <= 0
                || !validarDatosPaquete(paquete)) {

            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Los datos ingresados no son validos.");
            salida.flush();
            return;
        }

        Paquete existente = paqueteDAO.consultarPaquetePorId(paquete.getIdPaquete());

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else {
            paqueteDAO.modificarPaquete(paquete);
            LogEventos.registrar("Paquete modificado: idPaquete=" + paquete.getIdPaquete());
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Paquete modificado correctamente.");
        }

        salida.flush();
    }

    private void actualizarEstado() throws IOException {

        long idPaquete = entrada.readLong();
        int idEstado = entrada.readInt();

        if (idPaquete <= 0 || idEstado < 1 || idEstado > 4) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Los datos ingresados no son validos.");
            salida.flush();
            return;
        }

        Paquete existente = paqueteDAO.consultarPaquetePorId(idPaquete);

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else {
            paqueteDAO.actualizarEstadoPaquete(idPaquete, idEstado);
            LogEventos.registrar("Estado actualizado: idPaquete=" + idPaquete
                    + ", nuevoEstado=" + idEstado);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Estado del paquete actualizado correctamente.");
        }

        salida.flush();
    }

    private void eliminarPaquete() throws IOException {

        long idPaquete = entrada.readLong();

        if (idPaquete <= 0) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("El ID del paquete no es valido.");
            salida.flush();
            return;
        }

        Paquete existente = paqueteDAO.consultarPaquetePorId(idPaquete);

        if (existente == null) {
            salida.writeUTF(Protocolo.ERROR);
            salida.writeUTF("Paquete no encontrado.");
        } else {
            paqueteDAO.eliminarPaquete(idPaquete);
            LogEventos.registrar("Paquete eliminado: idPaquete=" + idPaquete);
            salida.writeUTF(Protocolo.OK);
            salida.writeUTF("Paquete eliminado correctamente.");
        }

        salida.flush();
    }
    private boolean validarDatosPaquete(Paquete paquete) {

        if (paquete == null) {
            return false;
        }

        if (paquete.getDescripcion() == null
                || paquete.getDescripcion().trim().isEmpty()
                || paquete.getDescripcion().length() > 255) {
            return false;
        }

        if (paquete.getDireccionOrigen() == null
                || paquete.getDireccionOrigen().trim().isEmpty()
                || paquete.getDireccionOrigen().length() > 255) {
            return false;
        }

        if (paquete.getDireccionDestino() == null
                || paquete.getDireccionDestino().trim().isEmpty()
                || paquete.getDireccionDestino().length() > 255) {
            return false;
        }

        if (paquete.getPeso() != null
                && paquete.getPeso().length() > 20) {
            return false;
        }

        return true;
    }
    private void escribirPaquete(Paquete paquete) throws IOException {
        salida.writeLong(paquete.getIdPaquete());
        salida.writeUTF(nuloAVacio(paquete.getDescripcion()));
        salida.writeUTF(nuloAVacio(paquete.getDireccionOrigen()));
        salida.writeUTF(nuloAVacio(paquete.getDireccionDestino()));
        salida.writeUTF(nuloAVacio(paquete.getPeso()));
        salida.writeInt(paquete.getIdEstado());
        salida.writeUTF(nuloAVacio(paquete.getFechaRegistro()));
    }

    private String nuloAVacio(String valor) {
        return valor == null ? "" : valor;
    }

    @Override
    public void run() {
        procesar();
    }
}
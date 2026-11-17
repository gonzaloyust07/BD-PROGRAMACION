package controlador;

import conection.LigaDAO;
import datas.Comunidad;
import datas.EquipoLiga;
import datas.JugadorLiga;
import vista.VentanaAgregar;
import vista.VentanaAgregarJugador;
import vista.VentanaLogin;
import vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorApp {
    private final VentanaLogin vLogin;
    private final VentanaPrincipal vPrincipal;
    private boolean actualizandoVista;

    public ControladorApp(VentanaLogin login, VentanaPrincipal principal) {
        vLogin = login;
        vPrincipal = principal;
        vLogin.escucharBotonLogin(e -> procesarLogin());
        configurarInteracciones();
    }

    private void procesarLogin() {
        try {
            if (!LigaDAO.autenticar(vLogin.getUsuario().trim(), vLogin.getPassword())) {
                JOptionPane.showMessageDialog(vLogin, "Usuario o contraseña inválidos.",
                        "Login Fallido", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cargarDatosDesdeBaseDeDatos();
            vLogin.dispose();
            vPrincipal.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vLogin,
                    "No se pudo iniciar sesión o cargar los datos de MySQL:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarInteracciones() {
        vPrincipal.getListaEquipos().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !actualizandoVista) {
                vPrincipal.actualizarTituloEquipo();
                cargarJugadoresSeleccionados();
            }
        });

        configurarMenuListaEquipos();
        configurarMenuTabla(vPrincipal.getTablaJugadores(), vPrincipal.getMenuJugadores());
        vPrincipal.getItemEditarEquipo().addActionListener(e -> abrirEdicionEquipo());
        vPrincipal.getItemEliminarEquipo().addActionListener(e -> eliminarEquipoSeleccionado());
        vPrincipal.getItemEditarJugador().addActionListener(e -> abrirEdicionJugador());
        vPrincipal.getItemEliminarJugador().addActionListener(e -> eliminarJugadorSeleccionado());
        vPrincipal.getItemPDFJugador().addActionListener(e -> abrirPDFJugador());
        vPrincipal.getBtnAddEquipo().addActionListener(e -> abrirFormularioEquipo(null));
        vPrincipal.getBtnAddJugador().addActionListener(e -> abrirFormularioJugador(null));
    }

    private void configurarMenuTabla(JTable tabla, JPopupMenu menu) {
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenu(e);
            }

            private void mostrarMenu(MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    return;
                }
                int fila = tabla.rowAtPoint(e.getPoint());
                if (fila >= 0) {
                    tabla.setRowSelectionInterval(fila, fila);
                    menu.show(tabla, e.getX(), e.getY());
                }
            }
        });
    }

    private void configurarMenuListaEquipos() {
        vPrincipal.getListaEquipos().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenu(e);
            }

            private void mostrarMenu(MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    return;
                }
                int indice = vPrincipal.getListaEquipos().locationToIndex(e.getPoint());
                if (indice >= 0 && vPrincipal.getListaEquipos()
                        .getCellBounds(indice, indice).contains(e.getPoint())) {
                    vPrincipal.getListaEquipos().setSelectedIndex(indice);
                    if (vPrincipal.getEquipoSeleccionadoId() != 0) {
                        vPrincipal.getMenuEquipos().show(
                                vPrincipal.getListaEquipos(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void cargarDatosDesdeBaseDeDatos() throws SQLException {
        List<EquipoLiga> equipos = LigaDAO.obtenerEquipos();
        int idEquipo = vPrincipal.getEquipoSeleccionadoId();
        List<JugadorLiga> jugadores = LigaDAO.obtenerJugadores(idEquipo);
        actualizandoVista = true;
        try {
            vPrincipal.actualizarDatos(equipos, jugadores);
        } finally {
            actualizandoVista = false;
        }
        cargarJugadoresSeleccionados();
    }

    private void cargarJugadoresSeleccionados() {
        try {
            vPrincipal.actualizarJugadores(
                    LigaDAO.obtenerJugadores(vPrincipal.getEquipoSeleccionadoId()));
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void abrirFormularioEquipo(EquipoLiga equipo) {
        List<Comunidad> comunidades;
        try {
            comunidades = LigaDAO.obtenerComunidades();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
            return;
        }
        if (comunidades.isEmpty()) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "No hay comunidades autónomas disponibles en la base de datos.",
                    "No se puede guardar el equipo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        VentanaAgregar ventana = new VentanaAgregar(vPrincipal, comunidades, equipo);
        ventana.escucharBotones(
                evento -> guardarEquipo(ventana),
                evento -> ventana.dispose()
        );
        ventana.setVisible(true);
    }

    private void guardarEquipo(VentanaAgregar ventana) {
        if (ventana.getNombre().isEmpty() || ventana.getLocalidad().isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Completa el nombre del equipo y la localidad.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer fundacion = null;
        if (!ventana.getFundacion().isEmpty()) {
            try {
                fundacion = Integer.parseInt(ventana.getFundacion());
                if (fundacion < 1800 || fundacion > java.time.Year.now().getValue()) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ventana,
                        "El año de fundación debe ser un año válido.",
                        "Dato no válido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Comunidad comunidad = ventana.getComunidad();
        if (comunidad == null) {
            JOptionPane.showMessageDialog(ventana, "Selecciona una comunidad autónoma.",
                    "Dato no válido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (ventana.getIdEquipo() == 0) {
                int id = LigaDAO.insertarEquipo(ventana.getNombre(), ventana.getRama(),
                        ventana.getDivision(), ventana.getLocalidad(), fundacion, comunidad.getId());
                cargarDatosDesdeBaseDeDatos();
                vPrincipal.seleccionarEquipo(id);
            } else {
                LigaDAO.actualizarEquipo(ventana.getIdEquipo(), ventana.getNombre(), ventana.getRama(),
                        ventana.getDivision(), ventana.getLocalidad(), fundacion, comunidad.getId());
                cargarDatosDesdeBaseDeDatos();
            }
            ventana.dispose();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void abrirEdicionEquipo() {
        EquipoLiga equipo = vPrincipal.getEquipoSeleccionadoEnLista();
        if (equipo != null) {
            abrirFormularioEquipo(equipo);
        }
    }

    private void eliminarEquipoSeleccionado() {
        int idEquipo = vPrincipal.getEquipoSeleccionadoId();
        if (idEquipo == 0) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(vPrincipal,
                "¿Quieres eliminar este equipo y su plantilla de la base de datos?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            LigaDAO.eliminarEquipo(idEquipo);
            cargarDatosDesdeBaseDeDatos();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void abrirFormularioJugador(JugadorLiga jugador) {
        List<EquipoLiga> equipos;
        try {
            equipos = LigaDAO.obtenerEquipos();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
            return;
        }
        if (equipos.isEmpty()) {
            JOptionPane.showMessageDialog(vPrincipal, "Agrega un equipo antes de agregar jugadores.",
                    "No hay equipos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        VentanaAgregarJugador ventana = new VentanaAgregarJugador(vPrincipal, equipos, jugador);
        if (jugador == null && vPrincipal.getEquipoSeleccionadoId() != 0) {
            ventana.seleccionarEquipo(vPrincipal.getEquipoSeleccionadoId());
        }
        ventana.escucharBotones(
                evento -> guardarJugador(ventana),
                evento -> ventana.dispose()
        );
        ventana.setVisible(true);
    }

    private void guardarJugador(VentanaAgregarJugador ventana) {
        if (ventana.getNombre().isEmpty() || ventana.getNacionalidad().isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                    "Completa el nombre y la nacionalidad del jugador.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int numero;
        int edad;
        try {
            numero = Integer.parseInt(ventana.getNumero());
            edad = Integer.parseInt(ventana.getEdad());
            if (numero < 0 || edad < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana,
                    "El dorsal debe ser cero o mayor y la edad debe ser positiva.",
                    "Dato no válido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        EquipoLiga equipo = ventana.getEquipo();
        if (equipo == null) {
            JOptionPane.showMessageDialog(ventana, "Selecciona un equipo.",
                    "Dato no válido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (ventana.getIdJugador() == 0) {
                LigaDAO.insertarJugador(ventana.getNombre(), ventana.getPosicion(), numero, edad,
                        ventana.getNacionalidad(), ventana.isCapitan(), equipo.getId());
            } else {
                LigaDAO.actualizarJugador(ventana.getIdJugador(), ventana.getNombre(),
                        ventana.getPosicion(), numero, edad, ventana.getNacionalidad(),
                        ventana.isCapitan(), equipo.getId());
            }
            cargarDatosDesdeBaseDeDatos();
            vPrincipal.seleccionarEquipo(equipo.getId());
            ventana.dispose();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void abrirEdicionJugador() {
        if (vPrincipal.getIdJugadorSeleccionado() == 0) {
            return;
        }
        JugadorLiga jugador = vPrincipal.getJugadorSeleccionadoEnTabla();
        if (jugador != null) {
            abrirFormularioJugador(jugador);
        }
    }

    private void eliminarJugadorSeleccionado() {
        int idJugador = vPrincipal.getIdJugadorSeleccionado();
        if (idJugador == 0) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(vPrincipal,
                "¿Quieres eliminar este jugador de la base de datos?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            LigaDAO.eliminarJugador(idJugador);
            cargarDatosDesdeBaseDeDatos();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void abrirPDFJugador() {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter("Documentos PDF", "pdf"));
        if (selector.showOpenDialog(vPrincipal) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = selector.getSelectedFile();
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "Este equipo no permite abrir archivos PDF desde la aplicación.",
                    "No se puede abrir el PDF", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Desktop.getDesktop().open(archivo);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "No se pudo abrir el PDF:\n" + ex.getMessage(),
                    "Error al abrir el PDF", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarErrorBD(SQLException ex) {
        JOptionPane.showMessageDialog(vPrincipal,
                "No se pudo completar la operación en MySQL:\n" + ex.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}

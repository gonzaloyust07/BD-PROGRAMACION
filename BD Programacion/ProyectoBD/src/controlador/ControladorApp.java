package controlador;

import vista.VentanaLogin;
import vista.VentanaPrincipal;
import vista.VentanaAgregar;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class ControladorApp {
    private VentanaLogin vLogin;
    private VentanaPrincipal vPrincipal;
    private ArrayList<String> listaNombresEquipos;

    public ControladorApp(VentanaLogin login, VentanaPrincipal principal) {
        this.vLogin = login;
        this.vPrincipal = principal;
        this.listaNombresEquipos = new ArrayList<>();

        // Enlazar login
        this.vLogin.escucharBotonLogin(e -> procesarLogin());

        // Enlazar eventos de la pantalla principal
        configurarInteracciones();
    }

    private void procesarLogin() {
        String u = vLogin.getUsuario();
        String p = vLogin.getPassword();

        if (u.equals("admin") && p.equals("admin")) {
            vLogin.dispose();
            vPrincipal.setVisible(true);
            cargarDatosEjemplo();
        } else {
            JOptionPane.showMessageDialog(vLogin, "Usuario o contraseña inválidos.", "Login Fallido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarInteracciones() {
        // Clic derecho en la tabla
        vPrincipal.getTablaDatos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int fila = vPrincipal.getTablaDatos().rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        vPrincipal.getTablaDatos().setRowSelectionInterval(fila, fila);
                        vPrincipal.getMenuContextual().show(vPrincipal.getTablaDatos(), e.getX(), e.getY());
                    }
                }
            }
        });

        // Eliminar registro
        vPrincipal.getItemEliminar().addActionListener(e -> {
            int fila = vPrincipal.getTablaDatos().getSelectedRow();
            if (fila >= 0) {
                vPrincipal.getModeloTabla().removeRow(fila);
                JOptionPane.showMessageDialog(vPrincipal, "Registro eliminado correctamente.");
            }
        });

        // Selector de PDF
        vPrincipal.getItemCargarPDF().addActionListener(e -> {
            JFileChooser buscador = new JFileChooser();
            buscador.setDialogTitle("Cargar Expediente PDF del Jugador");
            int retorno = buscador.showOpenDialog(vPrincipal);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                File archivo = buscador.getSelectedFile();
                JOptionPane.showMessageDialog(vPrincipal, "Fichero cargado con éxito: " + archivo.getName());
            }
        });

        // ACCIÓN: AGREGAR JUGADOR
        vPrincipal.getBtnAddJugador().addActionListener(e -> {
            VentanaAgregar pop = new VentanaAgregar(vPrincipal, true, listaNombresEquipos);
            pop.escucharBotones(
                    ok -> {
                        vPrincipal.getModeloTabla().addRow(new Object[]{
                                vPrincipal.getModeloTabla().getRowCount() + 1,
                                pop.getCampo1(), pop.getCampo2(), pop.getBooleano(), pop.getSeleccionCombo()
                        });
                        pop.dispose();
                    },
                    cancel -> pop.dispose()
            );
            pop.setVisible(true);
        });

        // ACCIÓN: AGREGAR EQUIPO (¡Ahora funcionará perfectamente!)
        vPrincipal.getBtnAddEquipo().addActionListener(e -> {
            VentanaAgregar pop = new VentanaAgregar(vPrincipal, false, new ArrayList<>());
            pop.escucharBotones(
                    ok -> {
                        String nombreEquipo = pop.getCampo1().trim();
                        String ciudadEstadio = pop.getCampo2().trim();

                        if (nombreEquipo.isEmpty() || ciudadEstadio.isEmpty()) {
                            JOptionPane.showMessageDialog(pop, "Por favor, rellene todos los campos.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        listaNombresEquipos.add(nombreEquipo);

                        vPrincipal.getModeloTabla().addRow(new Object[]{
                                vPrincipal.getModeloTabla().getRowCount() + 1,
                                nombreEquipo,
                                "Sede: " + ciudadEstadio,
                                false,
                                "Equipo Registrado"
                        });

                        pop.dispose();
                        JOptionPane.showMessageDialog(vPrincipal, "¡Equipo '" + nombreEquipo + "' añadido correctamente!");
                    },
                    cancel -> pop.dispose()
            );
            pop.setVisible(true);
        });
    }

    private void cargarDatosEjemplo() {
        listaNombresEquipos.add("CV Almería");
        listaNombresEquipos.add("Madrid Vóley");

        vPrincipal.getModeloTabla().addRow(new Object[]{1, "Adrián Díaz", "Colocador", true, "CV Almería"});
        vPrincipal.getModeloTabla().addRow(new Object[]{2, "Hugo Gómez", "Líbero", false, "Madrid Vóley"});
    }
}

package vista;

import datas.EquipoLiga;
import datas.JugadorLiga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaAgregarJugador extends JDialog {
    private static final Color FONDO = new Color(246, 244, 238);
    private static final Color VERDE = new Color(14, 90, 67);
    private static final Color VERDE_HOVER = new Color(22, 122, 91);
    private static final Color VERDE_TEXTO = new Color(22, 38, 32);
    private static final Color GRIS = new Color(226, 223, 215);
    private static final Color GRIS_HOVER = new Color(205, 201, 191);
    private static final Color ARENA = new Color(197, 185, 165);

    private final int idJugador;
    private final JTextField txtNombre;
    private final JComboBox<String> comboPosicion;
    private final JTextField txtNumero;
    private final JTextField txtEdad;
    private final JTextField txtNacionalidad;
    private final JCheckBox chkCapitan;
    private final JComboBox<EquipoLiga> comboEquipo;
    private final JButton btnAceptar;
    private final JButton btnCancelar;

    public VentanaAgregarJugador(Frame padre, List<EquipoLiga> equipos) {
        this(padre, equipos, null);
    }

    public VentanaAgregarJugador(Frame padre, List<EquipoLiga> equipos, JugadorLiga jugador) {
        super(padre, jugador == null ? "Agregar jugador" : "Modificar jugador", true);
        idJugador = jugador == null ? 0 : jugador.getId();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(18, 24, 10, 24));

        txtNombre = crearCampoTexto();
        agregarFila(formulario, 0, "Nombre completo:", txtNombre);

        comboPosicion = new JComboBox<>(new String[]{
                "Colocador", "Opuesto", "Receptor", "Central", "Líbero"
        });
        estilizarCombo(comboPosicion);
        agregarFila(formulario, 1, "Posición:", comboPosicion);

        txtNumero = crearCampoTexto();
        agregarFila(formulario, 2, "Dorsal:", txtNumero);

        txtEdad = crearCampoTexto();
        agregarFila(formulario, 3, "Edad:", txtEdad);

        txtNacionalidad = crearCampoTexto();
        agregarFila(formulario, 4, "Nacionalidad:", txtNacionalidad);

        chkCapitan = new JCheckBox("¿Es capitán?");
        chkCapitan.setOpaque(false);
        chkCapitan.setForeground(VERDE_TEXTO);
        agregarFila(formulario, 5, "", chkCapitan);

        comboEquipo = new JComboBox<>();
        for (EquipoLiga equipo : equipos) {
            comboEquipo.addItem(equipo);
        }
        estilizarCombo(comboEquipo);
        agregarFila(formulario, 6, "Equipo:", comboEquipo);

        if (jugador != null) {
            txtNombre.setText(jugador.getNombre());
            comboPosicion.setSelectedItem(jugador.getPosicion());
            txtNumero.setText(String.valueOf(jugador.getNumero()));
            txtEdad.setText(String.valueOf(jugador.getEdad()));
            txtNacionalidad.setText(jugador.getNacionalidad());
            chkCapitan.setSelected(jugador.isCapitan());
            for (int i = 0; i < comboEquipo.getItemCount(); i++) {
                if (comboEquipo.getItemAt(i).getId() == jugador.getIdEquipo()) {
                    comboEquipo.setSelectedIndex(i);
                    break;
                }
            }
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panelBotones.setBackground(FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 16));
        btnAceptar = new JButton(jugador == null ? "Aceptar" : "Guardar cambios");
        btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnAceptar, VERDE, Color.WHITE, VERDE_HOVER);
        estilizarBoton(btnCancelar, GRIS, VERDE_TEXTO, GRIS_HOVER);
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(FONDO);
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(panelBotones, BorderLayout.SOUTH);
        setContentPane(contenido);
        pack();
        setLocationRelativeTo(padre);
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setBackground(Color.WHITE);
        campo.setForeground(VERDE_TEXTO);
        campo.setPreferredSize(new Dimension(250, 34));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ARENA),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return campo;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(250, 34));
        combo.setBackground(Color.WHITE);
        combo.setForeground(VERDE_TEXTO);
        combo.setBorder(BorderFactory.createLineBorder(ARENA));
    }

    private void agregarFila(JPanel panel, int fila, String texto, JComponent control) {
        GridBagConstraints etiqueta = new GridBagConstraints();
        etiqueta.gridx = 0;
        etiqueta.gridy = fila;
        etiqueta.anchor = GridBagConstraints.WEST;
        etiqueta.insets = new Insets(6, 0, 6, 14);
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(VERDE_TEXTO);
        panel.add(label, etiqueta);

        GridBagConstraints campo = new GridBagConstraints();
        campo.gridx = 1;
        campo.gridy = fila;
        campo.weightx = 1;
        campo.fill = GridBagConstraints.HORIZONTAL;
        campo.insets = new Insets(6, 0, 6, 0);
        panel.add(control, campo);
    }

    private void estilizarBoton(JButton boton, Color fondo, Color texto, Color hover) {
        boton.setPreferredSize(new Dimension(130, 38));
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setMargin(new Insets(8, 14, 8, 14));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }

    public int getIdJugador() { return idJugador; }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getPosicion() { return (String) comboPosicion.getSelectedItem(); }
    public String getNumero() { return txtNumero.getText().trim(); }
    public String getEdad() { return txtEdad.getText().trim(); }
    public String getNacionalidad() { return txtNacionalidad.getText().trim(); }
    public boolean isCapitan() { return chkCapitan.isSelected(); }
    public EquipoLiga getEquipo() { return (EquipoLiga) comboEquipo.getSelectedItem(); }

    public void seleccionarEquipo(int idEquipo) {
        for (int i = 0; i < comboEquipo.getItemCount(); i++) {
            if (comboEquipo.getItemAt(i).getId() == idEquipo) {
                comboEquipo.setSelectedIndex(i);
                return;
            }
        }
    }

    public void escucharBotones(ActionListener accionAceptar, ActionListener accionCancelar) {
        btnAceptar.addActionListener(accionAceptar);
        btnCancelar.addActionListener(accionCancelar);
    }
}

package vista;

import datas.Comunidad;
import datas.EquipoLiga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaAgregar extends JDialog {
    private static final Color FONDO = new Color(246, 244, 238);
    private static final Color VERDE = new Color(14, 90, 67);
    private static final Color VERDE_HOVER = new Color(22, 122, 91);
    private static final Color VERDE_TEXTO = new Color(22, 38, 32);
    private static final Color GRIS = new Color(226, 223, 215);
    private static final Color GRIS_HOVER = new Color(205, 201, 191);
    private static final Color ARENA = new Color(197, 185, 165);
    private static final String[] DIVISIONES = {
            "Superliga / Liga Iberdrola",
            "Superliga 2",
            "Primera División Nacional",
            "Primera División Autonómica",
            "Segunda División Autonómica",
            "División Provincial",
            "Liga Local / Municipal"
    };

    private final int idEquipo;
    private final JTextField txtNombre;
    private final JComboBox<String> comboRama;
    private final JComboBox<String> comboDivision;
    private final JTextField txtLocalidad;
    private final JTextField txtFundacion;
    private final JComboBox<Comunidad> comboComunidad;
    private final JButton btnAceptar;
    private final JButton btnCancelar;

    public VentanaAgregar(Frame padre, List<Comunidad> comunidades) {
        this(padre, comunidades, null);
    }

    public VentanaAgregar(Frame padre, List<Comunidad> comunidades, EquipoLiga equipo) {
        super(padre, equipo == null ? "Agregar equipo" : "Modificar equipo", true);
        idEquipo = equipo == null ? 0 : equipo.getId();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(18, 24, 10, 24));

        txtNombre = crearCampoTexto();
        agregarFila(formulario, 0, "Nombre del equipo:", txtNombre);

        comboRama = new JComboBox<>(new String[]{"Masculino", "Femenino", "Mixto"});
        estilizarCombo(comboRama);
        agregarFila(formulario, 1, "Rama:", comboRama);

        comboDivision = new JComboBox<>(DIVISIONES);
        estilizarCombo(comboDivision);
        agregarFila(formulario, 2, "División:", comboDivision);

        txtLocalidad = crearCampoTexto();
        agregarFila(formulario, 3, "Localidad:", txtLocalidad);

        txtFundacion = crearCampoTexto();
        agregarFila(formulario, 4, "Año de fundación:", txtFundacion);

        comboComunidad = new JComboBox<>();
        for (Comunidad comunidad : comunidades) {
            comboComunidad.addItem(comunidad);
        }
        estilizarCombo(comboComunidad);
        agregarFila(formulario, 5, "Comunidad autónoma:", comboComunidad);

        if (equipo != null) {
            txtNombre.setText(equipo.getNombre());
            comboRama.setSelectedItem(equipo.getRama());
            comboDivision.setSelectedItem(equipo.getDivision());
            txtLocalidad.setText(equipo.getLocalidad());
            if (equipo.getFundacion() != null) {
                txtFundacion.setText(String.valueOf(equipo.getFundacion()));
            }
            for (int i = 0; i < comboComunidad.getItemCount(); i++) {
                if (comboComunidad.getItemAt(i).getNombre().equals(equipo.getComunidad())) {
                    comboComunidad.setSelectedIndex(i);
                    break;
                }
            }
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panelBotones.setBackground(FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 16));
        btnAceptar = new JButton(equipo == null ? "Aceptar" : "Guardar cambios");
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

    public int getIdEquipo() { return idEquipo; }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getRama() { return (String) comboRama.getSelectedItem(); }
    public String getDivision() { return (String) comboDivision.getSelectedItem(); }
    public String getLocalidad() { return txtLocalidad.getText().trim(); }
    public String getFundacion() { return txtFundacion.getText().trim(); }
    public Comunidad getComunidad() { return (Comunidad) comboComunidad.getSelectedItem(); }

    public void escucharBotones(ActionListener accionAceptar, ActionListener accionCancelar) {
        btnAceptar.addActionListener(accionAceptar);
        btnCancelar.addActionListener(accionCancelar);
    }
}

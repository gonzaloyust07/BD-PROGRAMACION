package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaAgregar extends JDialog {
    private JTextField txtCampo1;
    private JTextField txtCampo2;
    private JCheckBox chkBooleano;
    private JComboBox<Object> comboOpciones;
    private JButton btnAceptar;
    private JButton btnCancelar;

    public VentanaAgregar(Frame padre, boolean esJugador, ArrayList<?> datosLista) {
        super(padre, esJugador ? "Agregar Jugador" : "Agregar Equipo", true);
        setSize(380, 250);
        setLocationRelativeTo(padre);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (esJugador) {
            panelFormulario.setLayout(new GridLayout(4, 2, 8, 8));

            panelFormulario.add(new JLabel("Nombre Jugador:"));
            txtCampo1 = new JTextField();
            panelFormulario.add(txtCampo1);

            panelFormulario.add(new JLabel("Posición:"));
            txtCampo2 = new JTextField();
            panelFormulario.add(txtCampo2);

            panelFormulario.add(new JLabel("¿Es Capitán?:"));
            chkBooleano = new JCheckBox();
            panelFormulario.add(chkBooleano);

            panelFormulario.add(new JLabel("Seleccionar Equipo:"));
            comboOpciones = new JComboBox<>();
            for (Object item : datosLista) {
                comboOpciones.addItem(item);
            }
            panelFormulario.add(comboOpciones);
        } else {
            panelFormulario.setLayout(new GridLayout(2, 2, 8, 8));

            panelFormulario.add(new JLabel("Nombre Equipo:"));
            txtCampo1 = new JTextField();
            panelFormulario.add(txtCampo1);

            panelFormulario.add(new JLabel("Ciudad / Sede:"));
            txtCampo2 = new JTextField();
            panelFormulario.add(txtCampo2);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // LAS FUNCIONES ESENCIALES PARA QUE EL CONTROLADOR LEA LOS DATOS
    public String getCampo1() { return txtCampo1.getText(); }
    public String getCampo2() { return txtCampo2.getText(); }
    public boolean getBooleano() { return chkBooleano != null && chkBooleano.isSelected(); }
    public Object getSeleccionCombo() { return comboOpciones != null ? comboOpciones.getSelectedItem() : null; }

    public void escucharBotones(ActionListener accionAceptar, ActionListener accionCancelar) {
        btnAceptar.addActionListener(accionAceptar);
        btnCancelar.addActionListener(accionCancelar);
    }
}

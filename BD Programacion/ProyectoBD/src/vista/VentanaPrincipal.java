package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;
    private JPopupMenu menuContextual;
    private JMenuItem itemModificar;
    private JMenuItem itemEliminar;
    private JMenuItem itemCargarPDF;

    // ATENCIÓN: Definimos las variables globales de los botones obligatorios
    private JButton btnAddJugador;
    private JButton btnAddEquipo;

    public VentanaPrincipal() {
        setTitle("Liga de Vóley - Panel Principal");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] cabeceras = {"ID Interno", "Nombre / Equipo", "Detalle / Sede", "Capitán (Boolean)", "Tipo Registro"};

        modeloTabla = new DefaultTableModel(cabeceras, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };

        tablaDatos = new JTable(modeloTabla);
        tablaDatos.setAutoCreateRowSorter(true);

        menuContextual = new JPopupMenu();
        itemModificar = new JMenuItem("Actualizar datos");
        itemEliminar = new JMenuItem("Eliminar registro");
        itemCargarPDF = new JMenuItem("Asociar/Cargar PDF");
        menuContextual.add(itemModificar);
        menuContextual.add(itemEliminar);
        menuContextual.add(itemCargarPDF);

        add(new JScrollPane(tablaDatos), BorderLayout.CENTER);

        // CREACIÓN FÍSICA DE LOS BOTONES ABAJO
        JPanel panelInferior = new JPanel();
        btnAddJugador = new JButton("Agregar Jugador");
        btnAddEquipo = new JButton("Agregar Equipo");

        panelInferior.add(btnAddJugador);
        panelInferior.add(btnAddEquipo);
        add(panelInferior, BorderLayout.SOUTH);
    }

    // FUNCIONES (GETTERS) QUE LE FALTABAN A TU CLASE
    public JTable getTablaDatos() { return tablaDatos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JPopupMenu getMenuContextual() { return menuContextual; }
    public JMenuItem getItemModificar() { return itemModificar; }
    public JMenuItem getItemEliminar() { return itemEliminar; }
    public JMenuItem getItemCargarPDF() { return itemCargarPDF; }
    public JButton getBtnAddJugador() { return btnAddJugador; }
    public JButton getBtnAddEquipo() { return btnAddEquipo; } // <-- Aquí está la función que faltaba
}
package vista;

import datas.EquipoLiga;
import datas.JugadorLiga;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private static final Color FONDO = new Color(246, 244, 238);
    private static final Color VERDE = new Color(14, 90, 67);
    private static final Color VERDE_HOVER = new Color(22, 122, 91);
    private static final Color VERDE_TEXTO = new Color(22, 38, 32);
    private static final Color ARENA = new Color(216, 195, 158);
    private static final Color ARENA_HOVER = new Color(197, 176, 138);
    private static final Color MENTA = new Color(200, 224, 214);
    private static final Color FILA_ALTERNATIVA = new Color(250, 248, 243);
    private static final Color BORDE_CELDA = new Color(226, 223, 215);

    private final JTable tablaJugadores;
    private final DefaultTableModel modeloJugadoresTabla;
    private final DefaultListModel<EquipoItem> modeloListaEquipos;
    private final JList<EquipoItem> listaEquipos;
    private final JLabel etiquetaEquipoSeleccionado;
    private final JPopupMenu menuEquipos;
    private final JPopupMenu menuJugadores;
    private final JMenuItem itemEditarEquipo;
    private final JMenuItem itemEliminarEquipo;
    private final JMenuItem itemEditarJugador;
    private final JMenuItem itemEliminarJugador;
    private final JMenuItem itemPDFJugador;
    private final JButton btnAddEquipo;
    private final JButton btnAddJugador;
    private List<EquipoLiga> equiposCargados = new ArrayList<>();

    public VentanaPrincipal() {
        setTitle("Vóley Gestión - Panel Principal");
        setSize(1100, 660);
        setMinimumSize(new Dimension(850, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloJugadoresTabla = new DefaultTableModel(new String[]{
                "ID Interno", "Nombre", "Posición", "Dorsal", "Edad",
                "Nacionalidad", "Capitán", "Equipo", "ID Equipo"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return "Capitán".equals(getColumnName(columnIndex)) ? Boolean.class : Object.class;
            }
        };
        tablaJugadores = crearTabla();
        tablaJugadores.removeColumn(tablaJugadores.getColumnModel().getColumn(7));
        configurarAnchuras(tablaJugadores, new int[]{210, 130, 80, 65, 140, 80, 180});
        tablaJugadores.getColumnModel().getColumn(5).setCellRenderer(new RenderizadorBooleano());

        modeloListaEquipos = new DefaultListModel<>();
        modeloListaEquipos.addElement(new EquipoItem(0, "Todos"));
        listaEquipos = new JList<>(modeloListaEquipos);
        listaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEquipos.setFont(new Font("SansSerif", Font.PLAIN, 15));
        listaEquipos.setForeground(VERDE_TEXTO);
        listaEquipos.setBackground(Color.WHITE);
        listaEquipos.setSelectionBackground(MENTA);
        listaEquipos.setSelectionForeground(VERDE_TEXTO);
        listaEquipos.setFixedCellHeight(38);
        listaEquipos.setSelectedIndex(0);

        etiquetaEquipoSeleccionado = new JLabel("Todos los jugadores");
        etiquetaEquipoSeleccionado.setFont(new Font("SansSerif", Font.BOLD, 16));
        etiquetaEquipoSeleccionado.setForeground(VERDE_TEXTO);
        etiquetaEquipoSeleccionado.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 0));

        menuEquipos = new JPopupMenu();
        itemEditarEquipo = new JMenuItem("Modificar equipo");
        itemEliminarEquipo = new JMenuItem("Eliminar equipo");
        menuEquipos.add(itemEditarEquipo);
        menuEquipos.add(itemEliminarEquipo);

        menuJugadores = new JPopupMenu();
        itemEditarJugador = new JMenuItem("Modificar jugador");
        itemEliminarJugador = new JMenuItem("Eliminar jugador");
        itemPDFJugador = new JMenuItem("Abrir ficha PDF");
        menuJugadores.add(itemEditarJugador);
        menuJugadores.add(itemEliminarJugador);
        menuJugadores.addSeparator();
        menuJugadores.add(itemPDFJugador);

        Image fondo = new ImageIcon(cargarImagen("/imagenes/LogoSinFondo.png")).getImage();
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelPrincipal.setBackground(FONDO);

        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        panelCabecera.setOpaque(false);
        ImageIcon logoOriginal = new ImageIcon(cargarImagen("/imagenes/logo.jpg"));
        int altoLogo = 78;
        int anchoLogo = logoOriginal.getIconWidth() * altoLogo / logoOriginal.getIconHeight();
        JLabel etiquetaLogo = new JLabel(new ImageIcon(logoOriginal.getImage()
                .getScaledInstance(anchoLogo, altoLogo, Image.SCALE_SMOOTH)));
        panelCabecera.add(etiquetaLogo);

        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 4));
        titulos.setOpaque(false);
        JLabel titulo = new JLabel("Vóley Gestión");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(VERDE_TEXTO);
        JLabel subtitulo = new JLabel("Plantillas y jugadores");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(VERDE_TEXTO);
        titulos.add(titulo);
        titulos.add(subtitulo);
        panelCabecera.add(titulos);
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);

        JScrollPane panelListaEquipos = new JScrollPane(listaEquipos);
        panelListaEquipos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(VERDE), "Equipos"));
        panelListaEquipos.setPreferredSize(new Dimension(230, 0));

        JScrollPane panelTabla = new JScrollPane(tablaJugadores);
        panelTabla.setBorder(BorderFactory.createLineBorder(VERDE, 1));
        JPanel panelTablaConTitulo = new JPanel(new BorderLayout());
        panelTablaConTitulo.setOpaque(false);
        panelTablaConTitulo.add(etiquetaEquipoSeleccionado, BorderLayout.NORTH);
        panelTablaConTitulo.add(panelTabla, BorderLayout.CENTER);

        JPanel panelVistaDatos = new JPanel(new BorderLayout(12, 0));
        panelVistaDatos.setOpaque(false);
        panelVistaDatos.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        panelVistaDatos.add(panelListaEquipos, BorderLayout.WEST);
        panelVistaDatos.add(panelTablaConTitulo, BorderLayout.CENTER);
        panelPrincipal.add(panelVistaDatos, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        panelInferior.setBackground(FONDO);
        btnAddJugador = new JButton("Agregar Jugador");
        btnAddEquipo = new JButton("Agregar Equipo");
        estilizarBoton(btnAddJugador, VERDE, Color.WHITE, VERDE_HOVER);
        estilizarBoton(btnAddEquipo, ARENA, VERDE_TEXTO, ARENA_HOVER);
        panelInferior.add(btnAddJugador);
        panelInferior.add(btnAddEquipo);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        setContentPane(panelPrincipal);
    }

    private JTable crearTabla() {
        JTable tabla = new JTable(modeloJugadoresTabla);
        tabla.removeColumn(tabla.getColumnModel().getColumn(0));
        tabla.setAutoCreateColumnsFromModel(false);
        TableRowSorter<DefaultTableModel> ordenador = new TableRowSorter<>(modeloJugadoresTabla);
        tabla.setRowSorter(ordenador);
        for (int i = 0; i < modeloJugadoresTabla.getColumnCount(); i++) {
            ordenador.setSortable(i, false);
        }
        tabla.setRowHeight(34);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabla.setForeground(VERDE_TEXTO);
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(MENTA);
        tabla.setSelectionForeground(VERDE_TEXTO);
        tabla.setGridColor(BORDE_CELDA);
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component celda = super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        celda.setBackground(row % 2 == 0 ? Color.WHITE : FILA_ALTERNATIVA);
                    }
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                    return celda;
                }
            });
        }
        tabla.getTableHeader().setDefaultRenderer(new JTableHeaderRenderizador());
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 38));
        tabla.getTableHeader().setReorderingAllowed(false);
        return tabla;
    }

    private void configurarAnchuras(JTable tabla, int[] anchuras) {
        for (int i = 0; i < anchuras.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchuras[i]);
        }
    }

    private URL cargarImagen(String ruta) {
        URL recurso = getClass().getResource(ruta);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el recurso: " + ruta);
        }
        return recurso;
    }

    public void actualizarDatos(List<EquipoLiga> equipos, List<JugadorLiga> jugadores) {
        int idSeleccionado = getEquipoSeleccionadoId();
        equiposCargados = new ArrayList<>(equipos);
        modeloListaEquipos.clear();
        modeloListaEquipos.addElement(new EquipoItem(0, "Todos"));
        for (EquipoLiga equipo : equiposCargados) {
            modeloListaEquipos.addElement(new EquipoItem(equipo.getId(), equipo.getNombre()));
        }
        actualizarJugadores(jugadores);

        boolean encontrado = false;
        for (int i = 0; i < modeloListaEquipos.size(); i++) {
            if (modeloListaEquipos.get(i).getId() == idSeleccionado) {
                listaEquipos.setSelectedIndex(i);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            listaEquipos.setSelectedIndex(0);
        }
        actualizarTituloEquipo();
    }

    public void actualizarJugadores(List<JugadorLiga> jugadores) {
        modeloJugadoresTabla.setRowCount(0);
        for (JugadorLiga jugador : jugadores) {
            modeloJugadoresTabla.addRow(jugador.aFila());
        }
    }

    public int getEquipoSeleccionadoId() {
        EquipoItem seleccionado = listaEquipos.getSelectedValue();
        return seleccionado == null ? 0 : seleccionado.getId();
    }

    public String getEquipoSeleccionadoNombre() {
        EquipoItem seleccionado = listaEquipos.getSelectedValue();
        return seleccionado == null ? "Todos" : seleccionado.getNombre();
    }

    public void seleccionarEquipo(int idEquipo) {
        for (int i = 0; i < modeloListaEquipos.size(); i++) {
            if (modeloListaEquipos.get(i).getId() == idEquipo) {
                listaEquipos.setSelectedIndex(i);
                return;
            }
        }
    }

    public EquipoLiga getEquipoSeleccionadoEnLista() {
        int idEquipo = getEquipoSeleccionadoId();
        for (EquipoLiga equipo : equiposCargados) {
            if (equipo.getId() == idEquipo) {
                return equipo;
            }
        }
        return null;
    }

    public void actualizarTituloEquipo() {
        String nombre = getEquipoSeleccionadoNombre();
        etiquetaEquipoSeleccionado.setText("Todos".equals(nombre) ? "Todos los jugadores" : nombre);
    }

    public int getIdJugadorSeleccionado() {
        int filaSeleccionada = tablaJugadores.getSelectedRow();
        if (filaSeleccionada < 0) {
            return 0;
        }
        int filaModelo = tablaJugadores.convertRowIndexToModel(filaSeleccionada);
        return ((Number) modeloJugadoresTabla.getValueAt(filaModelo, 0)).intValue();
    }

    public JugadorLiga getJugadorSeleccionadoEnTabla() {
        int fila = tablaJugadores.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        int filaModelo = tablaJugadores.convertRowIndexToModel(fila);
        return new JugadorLiga(
                ((Number) modeloJugadoresTabla.getValueAt(filaModelo, 0)).intValue(),
                String.valueOf(modeloJugadoresTabla.getValueAt(filaModelo, 1)),
                String.valueOf(modeloJugadoresTabla.getValueAt(filaModelo, 2)),
                ((Number) modeloJugadoresTabla.getValueAt(filaModelo, 3)).intValue(),
                ((Number) modeloJugadoresTabla.getValueAt(filaModelo, 4)).intValue(),
                String.valueOf(modeloJugadoresTabla.getValueAt(filaModelo, 5)),
                Boolean.TRUE.equals(modeloJugadoresTabla.getValueAt(filaModelo, 6)),
                ((Number) modeloJugadoresTabla.getValueAt(filaModelo, 8)).intValue(),
                String.valueOf(modeloJugadoresTabla.getValueAt(filaModelo, 7)));
    }

    private static class EquipoItem {
        private final int id;
        private final String nombre;

        private EquipoItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }

        @Override
        public String toString() { return nombre; }
    }

    private static class RenderizadorBooleano extends JCheckBox
            implements javax.swing.table.TableCellRenderer {
        private RenderizadorBooleano() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(Boolean.TRUE.equals(value));
            setBackground(isSelected ? MENTA : (row % 2 == 0 ? Color.WHITE : FILA_ALTERNATIVA));
            return this;
        }
    }

    private static class JTableHeaderRenderizador extends DefaultTableCellRenderer {
        private JTableHeaderRenderizador() {
            setOpaque(true);
            setBackground(VERDE);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(VERDE);
            setForeground(Color.WHITE);
            setFont(table.getTableHeader().getFont());
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
    }

    private void estilizarBoton(JButton boton, Color fondo, Color texto, Color hover) {
        boton.setPreferredSize(new Dimension(165, 40));
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

    public JList<?> getListaEquipos() { return listaEquipos; }
    public JTable getTablaJugadores() { return tablaJugadores; }
    public JPopupMenu getMenuEquipos() { return menuEquipos; }
    public JPopupMenu getMenuJugadores() { return menuJugadores; }
    public JMenuItem getItemEditarEquipo() { return itemEditarEquipo; }
    public JMenuItem getItemEliminarEquipo() { return itemEliminarEquipo; }
    public JMenuItem getItemEditarJugador() { return itemEditarJugador; }
    public JMenuItem getItemEliminarJugador() { return itemEliminarJugador; }
    public JMenuItem getItemPDFJugador() { return itemPDFJugador; }
    public JButton getBtnAddEquipo() { return btnAddEquipo; }
    public JButton getBtnAddJugador() { return btnAddJugador; }
}

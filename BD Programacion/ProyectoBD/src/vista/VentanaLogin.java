package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class VentanaLogin extends JFrame {
    private static final Color FONDO = new Color(246, 244, 238);
    private static final Color VERDE = new Color(14, 90, 67);
    private static final Color VERDE_HOVER = new Color(22, 122, 91);
    private static final Color VERDE_TEXTO = new Color(22, 38, 32);

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public VentanaLogin() {
        setTitle("Acceso - Vóley Gestión");
        setSize(420, 290);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new BorderLayout(12, 16));
        contenido.setBackground(FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(18, 24, 20, 24));

        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        cabecera.setOpaque(false);
        ImageIcon logoOriginal = new ImageIcon(cargarImagen("/imagenes/LogoSinFondo.png"));
        int altoLogo = 66;
        int anchoLogo = logoOriginal.getIconWidth() * altoLogo / logoOriginal.getIconHeight();
        JLabel logo = new JLabel(new ImageIcon(logoOriginal.getImage()
                .getScaledInstance(anchoLogo, altoLogo, Image.SCALE_SMOOTH)));
        cabecera.add(logo);

        JLabel titulo = new JLabel("Vóley Gestión");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(VERDE_TEXTO);
        cabecera.add(titulo);
        contenido.add(cabecera, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 12));
        formulario.setOpaque(false);
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        JLabel etiquetaPassword = new JLabel("Contraseña:");
        etiquetaUsuario.setForeground(VERDE_TEXTO);
        etiquetaPassword.setForeground(VERDE_TEXTO);
        etiquetaUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        etiquetaPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formulario.add(etiquetaUsuario);
        txtUsuario = crearCampo();
        formulario.add(txtUsuario);
        formulario.add(etiquetaPassword);
        txtPassword = new JPasswordField();
        estilizarCampo(txtPassword);
        formulario.add(txtPassword);
        contenido.add(formulario, BorderLayout.CENTER);

        btnLogin = new JButton("Ingresar");
        btnLogin.setPreferredSize(new Dimension(130, 38));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(VERDE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setMargin(new Insets(8, 14, 8, 14));
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnLogin.setBackground(VERDE_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnLogin.setBackground(VERDE);
            }
        });
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(btnLogin);
        contenido.add(panelBoton, BorderLayout.SOUTH);

        setContentPane(contenido);
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        estilizarCampo(campo);
        return campo;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setBackground(Color.WHITE);
        campo.setForeground(VERDE_TEXTO);
        campo.setPreferredSize(new Dimension(220, 34));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(197, 185, 165)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    private URL cargarImagen(String ruta) {
        URL recurso = getClass().getResource(ruta);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el recurso: " + ruta);
        }
        return recurso;
    }

    public String getUsuario() { return txtUsuario.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }

    public void escucharBotonLogin(ActionListener listenForLoginButton) {
        btnLogin.addActionListener(listenForLoginButton);
    }
}

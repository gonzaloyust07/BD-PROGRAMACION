package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public VentanaLogin() {
        setTitle("Acceso - Liga de Vóley");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel(" Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel(" Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnLogin = new JButton("Ingresar");
        add(new JLabel("")); // Celda vacía
        add(btnLogin);
    }

    public String getUsuario() { return txtUsuario.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }

    public void escucharBotonLogin(ActionListener listenForLoginButton) {
        btnLogin.addActionListener(listenForLoginButton);
    }
}

package vista.panelPrincipal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

import controlador.inicioSesionRegistro.ControladorRegistro;

/**
 * Clase VentanaRegistrarUsuario para la ventana de registro de usuario
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaRegistrarUsuario extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7231915893607363330L;

	private GridLayout registroLayout = new GridLayout(3, 2, 10, 15);

	private JLabel lblNombreRegistro = new JLabel("Introduce el nombre de Usuario: ");
	private JLabel lblContrasenaRegistro = new JLabel("Introduce la contrase�a: ");

	private JPasswordField txtContrasenaRegistro = new JPasswordField(10);
	private JTextField txtNombreRegistro = new JTextField(10);

	private JButton btAceptarRegistro = new JButton("Confirmar");

	/**
	 * Constructor de VentanaRegistrarUsuario
	 */
	public VentanaRegistrarUsuario() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Registro");
		setBounds(0, 0, 400, 150);
		setLocationRelativeTo(null);
		setLayout(registroLayout);
		anadirComponentes();

		ControladorRegistro cRegistro = new ControladorRegistro(this);
		this.setControlador(cRegistro);
	}

	/**
	 * Metodo para anadir los componentes
	 */
	public void anadirComponentes() {

		add(lblNombreRegistro);
		add(txtNombreRegistro);
		add(lblContrasenaRegistro);
		add(txtContrasenaRegistro);
		add(new JLabel(""));
		add(btAceptarRegistro);

	}

	/**
	 * Metodo para establecer el controlador del boton de aceptar
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		btAceptarRegistro.addActionListener(c);
	}

	/**
	 * Metodo para obtener el nombre introducido en el JTextField
	 * 
	 * @return String con el nombre
	 */
	public String getVentanaNombreRegistro() {
		return txtNombreRegistro.getText();
	}

	/**
	 * Metodo para obtener el nombre introducido en el JPasswordField
	 * 
	 * @return String con la contrasena
	 */
	@SuppressWarnings("deprecation")
	public String getVentanaConstrasenaRegistro() {
		return txtContrasenaRegistro.getText();
	}

	/**
	 * Metodo para limpiar los JTextField
	 */
	public void limpiarVenatanaRegistro() {
		txtNombreRegistro.setText("");
		txtContrasenaRegistro.setText("");
	}

}

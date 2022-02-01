package vista.panelPrincipal.pestanaIniciarSesion;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Clase PestanaIniciarSesionFormulario para el formulario de inicio de sesion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaIniciarSesionFormulario extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4531708263598930593L;

	GridLayout inicioLayout = new GridLayout(5, 2, 10, 10);

	private JLabel lblNombreUsuario = new JLabel("Nombre de Usuario: ");
	private JLabel lblContrasena = new JLabel("Contrase�a Usuario: ");

	private JPasswordField txtContrasenaInicio = new JPasswordField(10);
	private JTextField txtNombreInicio = new JTextField(10);

	private JButton btInicioSesion = new JButton("Iniciar");

	/**
	 * Constructor de PestanaIniciarSesionFormulario
	 */
	public PestanaIniciarSesionFormulario() {

		setLayout(inicioLayout);
		anadirComponentes();

	}

	/**
	 * Metodo para anadir los componentes
	 */
	public void anadirComponentes() {

		add(lblNombreUsuario);
		add(txtNombreInicio);
		add(lblContrasena);
		add(txtContrasenaInicio);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btInicioSesion);
		add(new JLabel(""));
		add(new JLabel(""));

	}

	/**
	 * Metodo para establecer el controlador del boton de inicio de sesion
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		btInicioSesion.addActionListener(c);
	}

	/**
	 * Metodo para obtener el nombre del usuario del JTextField
	 * 
	 * @return String con el nombre
	 */
	public String getPestanaNombreInicioSesion() {
		return txtNombreInicio.getText();
	}

	/**
	 * Metodo para obtener la contrasena del usuario del JPasswordField
	 * 
	 * @return String con la contrasena
	 */
	@SuppressWarnings("deprecation")
	public String getPestanaConstrasenaInicio() {
		return txtContrasenaInicio.getText();
	}

	/**
	 * Metodo para limpiar los textfield
	 */
	public void limpiarPestanaInicioSesion() {
		txtNombreInicio.setText("");
		txtContrasenaInicio.setText("");
	}
}

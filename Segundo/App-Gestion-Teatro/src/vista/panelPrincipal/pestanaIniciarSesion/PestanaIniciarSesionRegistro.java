package vista.panelPrincipal.pestanaIniciarSesion;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Clase PestanaIniciarSesionRegistro para el registro
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaIniciarSesionRegistro extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7168292950699313013L;

	GridLayout registroLayout = new GridLayout(4, 1, 10, 30);

	private JLabel lblRegistro = new JLabel("	�Aun No tienes cuenta?, Haztela para no perderte nada!!");
	private JButton btRegistrarse = new JButton("Registrarse");

	/**
	 * Constructor de PestanaIniciarSesionRegistro
	 */
	public PestanaIniciarSesionRegistro() {

		setLayout(registroLayout);
		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes
	 */
	public void anadirComponentes() {

		add(new Label(""));
		add(new Label(""));
		add(lblRegistro);
		add(btRegistrarse);
	}

	/**
	 * Metodo para establecer el controlador del boton de registro
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		btRegistrarse.addActionListener(c);
	}
}

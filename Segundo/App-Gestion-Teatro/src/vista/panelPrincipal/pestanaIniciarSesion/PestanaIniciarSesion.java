package vista.panelPrincipal.pestanaIniciarSesion;

import java.awt.*;
import javax.swing.*;
import controlador.inicioSesionRegistro.ControladorBotonRegistro;
import controlador.inicioSesionRegistro.ControladorIniciarSesion;

/**
 * Clase PestanaIniciarSesion para la pestana de iniciar sesion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaIniciarSesion extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 802031953487331332L;

	GridLayout sesionLayout = new GridLayout(2, 1, 10, 40);

	/**
	 * Constructor de PestanaIniciarSesion
	 */
	public PestanaIniciarSesion() {

		setLayout(sesionLayout);
		PestanaIniciarSesionFormulario vInicioSesionFormulario = new PestanaIniciarSesionFormulario();
		PestanaIniciarSesionRegistro vInicioSesionRegistro = new PestanaIniciarSesionRegistro();
		add(vInicioSesionFormulario);
		add(vInicioSesionRegistro);

		ControladorIniciarSesion cInicioSesionFormulario = new ControladorIniciarSesion(vInicioSesionFormulario);
		vInicioSesionFormulario.setControlador(cInicioSesionFormulario);
		ControladorBotonRegistro cRegistro = new ControladorBotonRegistro(vInicioSesionRegistro);
		vInicioSesionRegistro.setControlador(cRegistro);
	}
}

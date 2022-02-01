package vista.panelAdministrador.pestanaCrearEvento.rellenarCampos;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controlador.crearEvento.ControladorCbEventoACrear;

/**
 * Clase PestanaCrearEventoBorder para la pestana en el borde de crear evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearEventoBorder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7546367837604361344L;
	private BorderLayout layoutRellenarCampos = new BorderLayout(2, 2);

	PestanaCrearEventoWest vistaCbCrearEventos;
	PestanaCrearEventoCenter vistaCrearEventos;

	/**
	 * Constructor de PestanaCrearEventoBorder
	 */
	public PestanaCrearEventoBorder() {

		setLayout(layoutRellenarCampos);

		vistaCbCrearEventos = new PestanaCrearEventoWest();
		vistaCrearEventos = new PestanaCrearEventoCenter();
		add(vistaCbCrearEventos, BorderLayout.WEST);
		add(vistaCrearEventos, BorderLayout.CENTER);

		ControladorCbEventoACrear cTipoEventoCrear = new ControladorCbEventoACrear(vistaCbCrearEventos,
				vistaCrearEventos);

		vistaCbCrearEventos.setControlador(cTipoEventoCrear);
	}

	/**
	 * Metodo para obtener la pestana en el centro de crear un evento
	 * 
	 * @return PestanaCrearEventoCenter objeto con la pestana
	 */
	public PestanaCrearEventoCenter getPestanaCrearEventoCenter() {
		return vistaCrearEventos;
	}

	/**
	 * Metodo para obtener la pestana en el west de crear un evento
	 * 
	 * @return PestanaCrearEventoWest objeto con la pestana
	 */
	public PestanaCrearEventoWest getPestanaCrearEventoWest() {
		return vistaCbCrearEventos;
	}
}

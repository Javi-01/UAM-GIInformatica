package vista.panelAdministrador.pestanaGestionRepresentaciones;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controlador.representaciones.ControladorFiltrarGestionRepres;

/**
 * Clase PestanaGestionRepresentacionesBorder para la pestana de gestion de
 * representaciones en border
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaGestionRepresentacionesBorder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4489794169739137581L;

	private PestanaGestionRepresentacionesCenter gestionRepresCenter;
	private PestanaGestionRepresentacionesWest gestionRepresWest;

	private BorderLayout layoutGestion = new BorderLayout(2, 2);

	/**
	 * Constructor de PestanaGestionRepresentacionesBorder
	 */
	public PestanaGestionRepresentacionesBorder() {
		setLayout(layoutGestion);

		gestionRepresCenter = new PestanaGestionRepresentacionesCenter();
		gestionRepresWest = new PestanaGestionRepresentacionesWest();

		add(gestionRepresCenter, BorderLayout.CENTER);
		add(gestionRepresWest, BorderLayout.WEST);

		ControladorFiltrarGestionRepres cFiltroRepres = new ControladorFiltrarGestionRepres(gestionRepresCenter,
				gestionRepresWest);

		getPestanaGestionRepresWest().setControlador(cFiltroRepres);
	}

	/**
	 * Metodo para mostrar la pestana de gestion de representaciones center
	 * 
	 * @return PestanaGestionRepresentacionesCenter objeto con la pestana
	 */
	public PestanaGestionRepresentacionesCenter getPestanaGestionRepresCenter() {
		return gestionRepresCenter;
	}

	/**
	 * Metodo para mostrar la pestana de gestion de representaciones west
	 * 
	 * @return PestanaGestionRepresentacionesWest objeto con la pestana
	 */
	public PestanaGestionRepresentacionesWest getPestanaGestionRepresWest() {
		return gestionRepresWest;
	}

}

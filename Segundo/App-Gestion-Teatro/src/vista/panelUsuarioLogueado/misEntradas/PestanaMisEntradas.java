package vista.panelUsuarioLogueado.misEntradas;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Clase PestanaMisEntradas para el card del panel de mis entradas reservadas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaMisEntradas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1972529188212791492L;

	private PestanaMisEntradasCentre misEntradasCentre;
	private PestanaMisEntradasWest misEntradasWest;

	private BorderLayout layoutEntradas = new BorderLayout(2, 2);

	/**
	 * Constructor de PestanaMisEntradas
	 */
	public PestanaMisEntradas() {

		setLayout(layoutEntradas);

		misEntradasCentre = new PestanaMisEntradasCentre();
		misEntradasWest = new PestanaMisEntradasWest();

		add(misEntradasCentre, BorderLayout.CENTER);
		add(misEntradasWest, BorderLayout.WEST);

	}

	/**
	 * Metodo para obtener la pestana central de mis entradas
	 * 
	 * @return PestanaMisEntradasCentre con el card
	 */
	public PestanaMisEntradasCentre getPestanaMisEntradasCentre() {
		return misEntradasCentre;
	}

	/**
	 * Metodo para obtener la pestana west de mis entradas
	 * 
	 * @return PestanaMisEntradasWest con el card
	 */
	public PestanaMisEntradasWest getPestanaMisEntradasWest() {
		return misEntradasWest;
	}
}

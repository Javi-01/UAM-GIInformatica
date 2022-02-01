package vista.panelAdministrador.pestanaEstadisticas;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * Clase PestanaEstadisticasWest para la pestana west de estadisticas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaEstadisticasWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6792784805417029078L;
	PestanaEstadisticasWestFiltro cbEstadisticasWest;
	PestanaEstadisticasWestTipoBusqueda estadisticaWestSeleccion;

	GridLayout layoutEstadisticaWest = new GridLayout(2, 1, 10, 10);

	/**
	 * Constructor de PestanaEstadisticasWest
	 */
	public PestanaEstadisticasWest() {

		setLayout(layoutEstadisticaWest);

		cbEstadisticasWest = new PestanaEstadisticasWestFiltro();
		estadisticaWestSeleccion = new PestanaEstadisticasWestTipoBusqueda();
		add(cbEstadisticasWest);
		add(estadisticaWestSeleccion);
	}

	/**
	 * Metodo para obtener la dimension establecida
	 * 
	 * @return Dimension con la nueva dimension
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}

	/**
	 * Metodo para obtener el Card con el filro
	 * 
	 * @return PestanaEstadisticasWestFiltro con el card
	 */
	public PestanaEstadisticasWestFiltro getEstadisticasFiltro() {
		return cbEstadisticasWest;
	}

	/**
	 * Metodo para obtener el Card con el tipo de busqueda
	 * 
	 * @return PestanaEstadisticasWestTipoBusqueda con el card
	 */
	public PestanaEstadisticasWestTipoBusqueda getEstadisticasTipoBusqueda() {
		return estadisticaWestSeleccion;
	}
}

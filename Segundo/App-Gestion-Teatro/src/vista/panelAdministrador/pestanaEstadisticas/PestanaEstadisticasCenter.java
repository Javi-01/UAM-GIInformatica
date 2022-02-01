package vista.panelAdministrador.pestanaEstadisticas;

import java.awt.CardLayout;

import javax.swing.JPanel;

import vista.panelAdministrador.pestanaEstadisticas.ocupacion.*;
import vista.panelAdministrador.pestanaEstadisticas.recaudacion.*;

/**
 * Clase PestanaEstadisticasCenter para la pestana central de estadisticas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaEstadisticasCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7546718896121849598L;

	private final static String titulocardEstadisticasEventoOcupacion = "EventoOcupacion";
	private final static String titulocardEstadisticasEventoRecaudacion = "EventoRecaudacion";
	private final static String titulocardEstadisticasZonaOcupacion = "ZonaOcupacion ";
	private final static String titulocardEstadisticasZonaRecaudacion = "ZonaRecaudacion ";
	private final static String titulocardEstadisticasGeneralOcupacion = "GeneralOcupacion";
	private final static String titulocardEstadisticasGeneralRecaudacion = "GeneralRecaudacion ";

	private CardEstadisticasCenterEventoOcupacion cardEstadisticasEventoOcupacion;
	private CardEstadisticasCenterEventoRecaudacion cardEstadisticasEventoRecaudacion;

	private CardEstadisticasCenterZonaOcupacion cardEstadisticasZonaOcupacion;
	private CardEstadisticasCenterZonaRecaudacion cardEstadisticasZonaRecaudacion;

	private CardEstadisticasCenterGeneralOcupacion cardEstadisticasGeneralOcupacion;
	private CardEstadisticasCenterGeneralRecaudacion cardEstadisticasGeneralRecaudacion;

	private CardLayout layoutEstadisticasEventosCenter = new CardLayout();

	/**
	 * Constructor de PestanaEstadisticasCenter
	 */
	public PestanaEstadisticasCenter() {

		setLayout(layoutEstadisticasEventosCenter);

		cardEstadisticasEventoOcupacion = new CardEstadisticasCenterEventoOcupacion();
		cardEstadisticasEventoRecaudacion = new CardEstadisticasCenterEventoRecaudacion();
		cardEstadisticasZonaOcupacion = new CardEstadisticasCenterZonaOcupacion();
		cardEstadisticasZonaRecaudacion = new CardEstadisticasCenterZonaRecaudacion();
		cardEstadisticasGeneralOcupacion = new CardEstadisticasCenterGeneralOcupacion();
		cardEstadisticasGeneralRecaudacion = new CardEstadisticasCenterGeneralRecaudacion();

		add(cardEstadisticasGeneralOcupacion, titulocardEstadisticasGeneralOcupacion);
		add(cardEstadisticasGeneralRecaudacion, titulocardEstadisticasGeneralRecaudacion);
		add(cardEstadisticasEventoOcupacion, titulocardEstadisticasEventoOcupacion);
		add(cardEstadisticasEventoRecaudacion, titulocardEstadisticasEventoRecaudacion);
		add(cardEstadisticasZonaRecaudacion, titulocardEstadisticasZonaRecaudacion);
		add(cardEstadisticasZonaOcupacion, titulocardEstadisticasZonaOcupacion);
	}

	/**
	 * Metodo para mostrar el LayOut de las estadisticas generales por ocupacion
	 */
	public void showEstadisticasGeneralOcupacion() {
		layoutEstadisticasEventosCenter.show(this, titulocardEstadisticasGeneralOcupacion);
	}

	/**
	 * Metodo para mostrar el LayOut de las estadisticas generales por recaudacion
	 */
	public void showEstadisticasGeneralRecaudacion() {
		layoutEstadisticasEventosCenter.show(this, titulocardEstadisticasGeneralRecaudacion);
	}

	/**
	 * Metodo para mostrar el LayOut de las estadisticas por eventos por ocupacion
	 */
	public void showEstadisticasEventoOcupacion() {
		layoutEstadisticasEventosCenter.show(this, titulocardEstadisticasEventoOcupacion);
	}

	/**
	 * Metodo para mostrar el LayOut de las estadisticas por eventos por recaudacion
	 */
	public void showEstadisticasEventoRecaudacion() {
		layoutEstadisticasEventosCenter.show(this, titulocardEstadisticasEventoRecaudacion);
	}

	/**
	 * Metodo para mostrar el LayOut de las estadisticas porzonas por ocupacion
	 */
	public void showEstadisticasZonaOcupacion() {
		layoutEstadisticasEventosCenter.show(this, titulocardEstadisticasZonaOcupacion);
	}

	/**
	 * Metodo para mostrar el LayOut de las estadisticas porzonas por recaudacion
	 */
	public void showEstadisticasZonaRecaudacion() {
		layoutEstadisticasEventosCenter.show(this, titulocardEstadisticasZonaRecaudacion);
	}

	/**
	 * Metodo para obtener el Card de las estadisticas generales de ocupacion
	 * 
	 * @return CardEstadisticasCenterGeneralOcupacion con el card
	 */
	public CardEstadisticasCenterGeneralOcupacion getEstadisticasCenterGeneralOcupacion() {
		return cardEstadisticasGeneralOcupacion;
	}

	/**
	 * Metodo para obtener el Card de las estadisticas generales de recaudacion
	 * 
	 * @return CardEstadisticasCenterGeneralRecaudacion con el card
	 */
	public CardEstadisticasCenterGeneralRecaudacion getEstadisticasCenterGeneralRecaudacion() {
		return cardEstadisticasGeneralRecaudacion;
	}

	/**
	 * Metodo para obtener el Card de las estadisticas por zona de ocupacion
	 * 
	 * @return CardEstadisticasCenterZonaOcupacion con el card
	 */
	public CardEstadisticasCenterZonaOcupacion getEstadisticasCenterZonaOcupacion() {
		return cardEstadisticasZonaOcupacion;
	}

	/**
	 * Metodo para obtener el Card de las estadisticas por zona de recaudacion
	 * 
	 * @return CardEstadisticasCenterZonaRecaudacion con el card
	 */
	public CardEstadisticasCenterZonaRecaudacion getEstadisticasCenterZonaRecaudacion() {
		return cardEstadisticasZonaRecaudacion;
	}

	/**
	 * Metodo para obtener el Card de las estadisticas por evento de ocupacion
	 * 
	 * @return CardEstadisticasCenterEventoOcupacion con el card
	 */
	public CardEstadisticasCenterEventoOcupacion getEstadisticasCenterEventoOcupacion() {
		return cardEstadisticasEventoOcupacion;
	}

	/**
	 * Metodo para obtener el Card de las estadisticas por evento de recaudacion
	 * 
	 * @return CardEstadisticasCenterEventoOcupacion con el card
	 */
	public CardEstadisticasCenterEventoRecaudacion getEstadisticasCenterEventoRecaudacion() {
		return cardEstadisticasEventoRecaudacion;
	}

	/**
	 * Metodo para limpiar las tablas
	 */
	public void limpiarTablaEstadisticas() {
		getEstadisticasCenterGeneralOcupacion().destruirTablaEstadisticasOcupacion();
		getEstadisticasCenterGeneralRecaudacion().destruirTablaEstadisticasRecaudacion();
		getEstadisticasCenterZonaOcupacion().destruirTablaEstadisticasOcupacion();
		getEstadisticasCenterZonaRecaudacion().destruirTablaEstadisticasRecaudacion();
		getEstadisticasCenterEventoOcupacion().destruirTablaEstadisticasOcupacion();
		getEstadisticasCenterEventoOcupacion().destruirTablaEstadisticasOcupacion();
		getEstadisticasCenterEventoRecaudacion().destruirTablaEstadisticasRecaudacion();
	}
}

package vista.panelAdministrador.pestanaZonasYAforo;

import java.awt.CardLayout;

import javax.swing.JPanel;

import vista.panelAdministrador.pestanaGestionRepresentaciones.tabla.PestanaTablaEventosGestionRepresentaciones;
import vista.panelAdministrador.pestanaZonasYAforo.tablas.PestanaZonasAforoTablaButacas;
import vista.panelAdministrador.pestanaZonasYAforo.tablas.PestanaZonasAforoTablaZonas;

/**
 * Clase PestanaZonasAforo para la pestana del aforo y las zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaZonasAforo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 946426192720234829L;

	private final static String cardZyABorder = "borderZyA";
	private final static String cardVistaEventos = "vistaEventos";
	private final static String cardVistaZonas = "vistaZonas";
	private final static String cardVistaButacas = "vistaButacas";

	private PestanaZonasAforoBorder zonasYAforoBorder;
	private PestanaZonasAforoTablaZonas tablaZonas;
	private PestanaTablaEventosGestionRepresentaciones tablaEventos;
	private PestanaZonasAforoTablaButacas tablaButacas;

	private CardLayout layoutAbono = new CardLayout();

	/**
	 * Constructor de PestanaZonasAforo
	 */
	public PestanaZonasAforo() {

		setLayout(layoutAbono);

		zonasYAforoBorder = new PestanaZonasAforoBorder();
		tablaEventos = new PestanaTablaEventosGestionRepresentaciones();
		tablaZonas = new PestanaZonasAforoTablaZonas();
		tablaButacas = new PestanaZonasAforoTablaButacas();

		add(zonasYAforoBorder, cardZyABorder);
		add(tablaEventos, cardVistaEventos);
		add(tablaZonas, cardVistaZonas);
		add(tablaButacas, cardVistaButacas);

	}

	/**
	 * Metodo para mostrar el layout de borderZyA
	 */
	public void showPestanaZonasAforoBorder() {
		layoutAbono.show(this, cardZyABorder);
	}

	/**
	 * Metodo para mostrar el layout de vistaZonas
	 */
	public void showPestanaTablaZonas() {
		layoutAbono.show(this, cardVistaZonas);
	}

	/**
	 * Metodo para mostrar el layout de vistaEventos
	 */
	public void showPestanaTablaEventos() {
		layoutAbono.show(this, cardVistaEventos);
	}

	/**
	 * Metodo para mostrar el layout de vistaButacas
	 */
	public void showPestanaTablaButacas() {
		layoutAbono.show(this, cardVistaButacas);
	}

	/**
	 * Metodo para obtener la pestana de zonas y aforo
	 * 
	 * @return PestanaZonasAforoBorder objeto con la pestana
	 */
	public PestanaZonasAforoBorder getPestanaZonasYAforoBorder() {
		return zonasYAforoBorder;
	}

	/**
	 * Metodo para obtener la pestana de la tabla de las zonas
	 * 
	 * @return PestanaZonasAforoTablaZonas objeto con la pestana
	 */
	public PestanaZonasAforoTablaZonas getPestanaTablaZonas() {
		return tablaZonas;
	}

	/**
	 * Metodo para obtener la pestana de la tabla de eventos
	 * 
	 * @return PestanaTablaEventosGestionRepresentaciones objeto con la pestana
	 */
	public PestanaTablaEventosGestionRepresentaciones getPestanaTablaEventos() {
		return tablaEventos;
	}

	/**
	 * Metodo para obtener la pestana de la tabla de butacas
	 * 
	 * @return PestanaTablaEventosGestionRepresentaciones objeto con la pestana
	 */
	public PestanaZonasAforoTablaButacas getPestanaTablaButacas() {
		return tablaButacas;
	}

}

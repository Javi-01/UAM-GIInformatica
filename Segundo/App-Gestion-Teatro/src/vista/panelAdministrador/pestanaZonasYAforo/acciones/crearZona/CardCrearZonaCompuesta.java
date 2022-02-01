package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import modelo.zona.Zona;
import vista.panelAdministrador.pestanaZonasYAforo.tablas.CrearZonasSeleccionadasZonaCompuesta;

/**
 * Clase CardCrearZonaCompuesta para el card para crear una zona compuesta
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaCompuesta extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6818891979758660128L;

	private CrearZonasSeleccionadasZonaCompuesta zonasDetro;
	private CardCrearZonaCompuestaComponentes zonaComponentes;
	private CardCrearZonaCompuestaAceptar zonaBotonAceptar;

	private ArrayList<Zona> zonas = new ArrayList<>();
	private GridLayout layoutCompuesta = new GridLayout(3, 1, 25, 25);

	/**
	 * Constructor de CardCrearZonaCompuesta
	 */
	public CardCrearZonaCompuesta() {

		setLayout(layoutCompuesta);
		zonasDetro = new CrearZonasSeleccionadasZonaCompuesta();
		zonaComponentes = new CardCrearZonaCompuestaComponentes();
		zonaBotonAceptar = new CardCrearZonaCompuestaAceptar();

		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes al card
	 */
	private void anadirComponentes() {

		add(zonaComponentes);
		add(zonasDetro);
		add(zonaBotonAceptar);
	}

	/**
	 * Metodo para obtener el panel para las zonas dentro de la zona compuesta
	 * 
	 * @return CrearZonasSeleccionadasZonaCompuesta objeto con las zonas
	 */
	public CrearZonasSeleccionadasZonaCompuesta getTablaZonas() {
		return zonasDetro;
	}

	/**
	 * Metodo para obtener el card con las componentes de la zona compuesta
	 * 
	 * @return CardCrearZonaCompuestaComponentes objeto con las componentes de la
	 *         zona compuesta
	 */
	public CardCrearZonaCompuestaComponentes getZonaComponentes() {
		return zonaComponentes;
	}

	/**
	 * Metodo para obtener el card para aceptar la creacion de la zona compuesta
	 * 
	 * @return CardCrearZonaCompuestaAceptar objeto con el card
	 */
	public CardCrearZonaCompuestaAceptar getZonaBotonAceptar() {
		return zonaBotonAceptar;
	}

	/**
	 * Metodo para obtener las zonas de la zona compuesta
	 * 
	 * @return ArrayList Zona con las zonas de la zona compuesta
	 */
	public ArrayList<Zona> getZonasCompuesta() {
		return zonas;
	}

	/**
	 * Metodo para limpiar la zona compuesta
	 */
	public void limpiarZonaCompuesta() {
		this.zonas.removeAll(zonas);
	}

	/**
	 * Metodo para anadir zonas a la zona compuesta
	 * 
	 * @param zona 	zona a añadir a la añadir a la zona compuesta 
	 */
	public void addZonasCompuesta(Zona zona) {
		this.zonas.add(zona);
	}

}

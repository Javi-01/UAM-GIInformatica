package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.CardLayout;

import javax.swing.JPanel;

import vista.panelAdministrador.pestanaZonasYAforo.tablas.CardCrearZonaTeatroTabla;

/**
 * Clase CardCrearZonaTeatroCenter para crear zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaTeatroCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2532500477416179081L;

	private final static String cardPie = "pie";
	private final static String cardSentado = "sentado";
	private final static String cardCompuesta = "compuesta";
	private final static String cardTablaZona = "tablaZona";

	private CardCrearZonaPie zonaPie;
	private CardCrearZonaSentado zonaSentado;
	private CardCrearZonaCompuesta zonaCompuesta;
	private CardCrearZonaTeatroTabla tablaZona;

	private CardLayout layoutCrear = new CardLayout();

	/**
	 * Constructor de CardCrearZonaTeatroCenter
	 */
	public CardCrearZonaTeatroCenter() {

		setLayout(layoutCrear);

		zonaPie = new CardCrearZonaPie();
		zonaSentado = new CardCrearZonaSentado();
		zonaCompuesta = new CardCrearZonaCompuesta();
		tablaZona = new CardCrearZonaTeatroTabla();

		add(zonaPie, cardPie);
		add(zonaSentado, cardSentado);
		add(zonaCompuesta, cardCompuesta);
		add(tablaZona, cardTablaZona);
	}

	/**
	 * Metodo para mostrar el LayOut de la zona de pie
	 */
	public void showCardZonaPie() {
		layoutCrear.show(this, cardPie);
	}

	/**
	 * Metodo para mostrar el LayOut de la zona sentado
	 */
	public void showCardZonaSentado() {
		layoutCrear.show(this, cardSentado);
	}

	/**
	 * Metodo para mostrar el LayOut de la zona compuesta
	 */
	public void showCardZonaCompuesta() {
		layoutCrear.show(this, cardCompuesta);
	}

	/**
	 * Metodo para mostrar el LayOut de la tabla de la zona
	 */
	public void showCardTablaZona() {
		layoutCrear.show(this, cardTablaZona);
	}

	/**
	 * Metodo para obtener el Card de crear zona pie
	 * 
	 * @return CardCrearZonaPie con el card
	 */
	public CardCrearZonaPie getCardZonaPie() {
		return zonaPie;
	}

	/**
	 * Metodo para obtener el Card de crear zona sentado
	 * 
	 * @return CardCrearZonaPie con el card
	 */
	public CardCrearZonaSentado getCardZonaSentado() {
		return zonaSentado;
	}

	/**
	 * Metodo para obtener el Card de crear zona compuesta
	 * 
	 * @return CardCrearZonaPie con el card
	 */
	public CardCrearZonaCompuesta getCardZonaCompuesta() {
		return zonaCompuesta;
	}

	/**
	 * Metodo para obtener el Card de la tabla de zonas
	 * 
	 * @return CardCrearZonaPie con el card
	 */
	public CardCrearZonaTeatroTabla getCardZonaTabla() {
		return tablaZona;
	}

}

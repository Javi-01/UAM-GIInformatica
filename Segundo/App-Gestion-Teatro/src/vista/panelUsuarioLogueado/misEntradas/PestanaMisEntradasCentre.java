package vista.panelUsuarioLogueado.misEntradas;

import java.awt.CardLayout;

import javax.swing.JPanel;

import modelo.entrada.Entrada;

/**
 * Clase PestanaMisEntradasCentre para el card central del panel de mis entradas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaMisEntradasCentre extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1518881360158710297L;

	private static final String cardCompra = "compradas";
	private static final String cardReservadas = "reservadas";

	private CardMisEntradasCompradas entradasCompradas;
	private CardMisEntradasReservadas entradasReservadas;
	private VentanaElegirOpcionEnEntrada opcionReserva;
	private VentanaTarjetaMisEntradas vistaTarjeta;

	private Entrada entrada;

	private CardLayout layoutEntradas = new CardLayout();

	/**
	 * Constructor de PestanaMisEntradasCentre
	 */
	public PestanaMisEntradasCentre() {
		setLayout(layoutEntradas);

		entradasCompradas = new CardMisEntradasCompradas();
		entradasReservadas = new CardMisEntradasReservadas();
		opcionReserva = new VentanaElegirOpcionEnEntrada();
		vistaTarjeta = new VentanaTarjetaMisEntradas();

		add(entradasCompradas, cardCompra);
		add(entradasReservadas, cardReservadas);
	}

	/**
	 * Metodo para mostrar el LayOut de las entradas compradas
	 */
	public void showMisEntradasCompradas() {
		layoutEntradas.show(this, cardCompra);
	}

	/**
	 * Metodo para mostrar el LayOut de las entradas reservadas
	 */
	public void showMisEntradasReservadas() {
		layoutEntradas.show(this, cardReservadas);
	}

	/**
	 * Metodo para obtener el Card de mis entradas compradas
	 * 
	 * @return CardMisEntradasCompradas con el card
	 */
	public CardMisEntradasCompradas getCardEntradasCompradas() {
		return entradasCompradas;
	}

	/**
	 * Metodo para obtener el Card de mis entradas reservadas
	 * 
	 * @return CardMisEntradasReservadas con el card
	 */
	public CardMisEntradasReservadas getCardEntradasReservadas() {
		return entradasReservadas;
	}

	/**
	 * Metodo para obtener la ventana para elegir la opcion de entrada
	 * 
	 * @return VentanaElegirOpcionEnEntrada con el card
	 */
	public VentanaElegirOpcionEnEntrada getVentanaOpcionReserva() {
		return opcionReserva;
	}

	/**
	 * Metodo para obtener la entrada elegida
	 * 
	 * @return Entrada con la entrada
	 */
	public Entrada getMisEntradasEntradaAsociada() {
		return entrada;
	}

	/**
	 * Metodo para establecer la entrada elegida
	 * 
	 * @param entrada Entrada con la entrada
	 */
	public void setMisEntradasEntradaAsociada(Entrada entrada) {
		this.entrada = entrada;
	}

	/**
	 * Metodo para obtener la ventana para comprar con targeta
	 * 
	 * @return VentanaTarjetaMisEntradas con el card
	 */
	public VentanaTarjetaMisEntradas getVentanaVistaTarjeta() {
		return vistaTarjeta;
	}

}

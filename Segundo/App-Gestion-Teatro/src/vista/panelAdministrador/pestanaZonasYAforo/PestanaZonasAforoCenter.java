package vista.panelAdministrador.pestanaZonasYAforo;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import modelo.evento.Evento;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.CardCambiarEntradasMaximasCompra;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.CardCrearZonasTeatroBorder;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.CardDeshabilitarButacas;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.CardReducirAforoEventos;

/**
 * Clase PestanaZonasAforoCenter para la pestana center del aforo de las zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaZonasAforoCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6552641567012600383L;

	private final static String cardDeshabilitar = "deshabilitar";
	private final static String cardReducirAforo = "reducirAforo";
	private final static String cardMaxEntradas = "maxEntradas";
	private final static String cardCrearZona = "crearZona";

	private CardDeshabilitarButacas deshabilitarButacas;
	private CardReducirAforoEventos reducirAforo;
	private CardCrearZonasTeatroBorder crearZonasTeatro;
	private CardCambiarEntradasMaximasCompra cambiarMaxEntradas;

	private CardLayout layoutCenter = new CardLayout();

	private Evento evento;

	/**
	 * Constructor de PestanaZonasAforoCenter
	 */
	public PestanaZonasAforoCenter() {
		TitledBorder centro = new TitledBorder("Gestion Zonas y Aforo");
		setBorder(centro);

		setLayout(layoutCenter);

		crearZonasTeatro = new CardCrearZonasTeatroBorder();
		deshabilitarButacas = new CardDeshabilitarButacas();
		reducirAforo = new CardReducirAforoEventos();
		cambiarMaxEntradas = new CardCambiarEntradasMaximasCompra();

		add(crearZonasTeatro, cardCrearZona);
		add(reducirAforo, cardReducirAforo);
		add(deshabilitarButacas, cardDeshabilitar);
		add(cambiarMaxEntradas, cardMaxEntradas);
	}

	/**
	 * Metodo para mostrar el layout para deshabilitar butacas
	 */
	public void showCardDeshabilitarButacas() {
		layoutCenter.show(this, cardDeshabilitar);
	}

	/**
	 * Metodo para mostrar el layout para reducir aforo
	 */
	public void showCardReducirAforo() {
		layoutCenter.show(this, cardReducirAforo);
	}

	/**
	 * Metodo para mostrar el layout para cambiar el numero maximo de entradas a
	 * comprar
	 */
	public void showCardCambiarMaxEntradas() {
		layoutCenter.show(this, cardMaxEntradas);
	}

	/**
	 * Metodo para mostrar el layout para crear una zona de teatro
	 */
	public void showCardCrearZonaTeatro() {
		layoutCenter.show(this, cardCrearZona);
	}

	/**
	 * Metodo para obtener el card para crear zonas
	 * 
	 * @return CardCrearZonasTeatroBorder objeto con el card
	 */
	public CardCrearZonasTeatroBorder getCardCrearZonaTeatro() {
		return crearZonasTeatro;
	}

	/**
	 * Metodo para obtener el card para deshabilitar butacas
	 * 
	 * @return CardDeshabilitarButacas objeto con el card
	 */
	public CardDeshabilitarButacas getCardDeshabilitarButacas() {
		return deshabilitarButacas;
	}

	/**
	 * Metodo para obtener el card para reducir aforo
	 * 
	 * @return CardReducirAforoEventos objeto con el card
	 */
	public CardReducirAforoEventos getCardReducirAforo() {
		return reducirAforo;
	}

	/**
	 * Metodo para obtener el card para establecer las entradas maximas a comprar
	 * 
	 * @return CardCambiarEntradasMaximasCompra objeto con el card
	 */
	public CardCambiarEntradasMaximasCompra getCardCambiarMaxEntradas() {
		return cambiarMaxEntradas;
	}

	/**
	 * Metodo para establecer el evento
	 * 
	 * @param eve Evento a establecer
	 */
	public void setEventoZonasAforo(Evento eve) {
		this.evento = eve;
	}

	/**
	 * Metodo para obtener el evento
	 * 
	 * @return Evento de la clase
	 */
	public Evento getEventoZonaAforo() {
		return evento;
	}
}

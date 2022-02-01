package vista.panelAdministrador.pestanaGestionRepresentaciones;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import modelo.evento.Evento;
import modelo.evento.Representacion;
import vista.panelAdministrador.pestanaGestionRepresentaciones.acciones.CardAnadirRepresentacionCenter;
import vista.panelAdministrador.pestanaGestionRepresentaciones.acciones.CardCancelarRepresentacionCenter;
import vista.panelAdministrador.pestanaGestionRepresentaciones.acciones.CardPosponerRepresentacionCenter;

/**
 * Clase PestanaGestionRepresentacionesCenter para la pestana de gestion de
 * representaciones en center
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaGestionRepresentacionesCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1536236921438037351L;

	private final static String cardCrearRepre = "CrearRepre";
	private final static String cardPosponerRepre = "PosponerRepre";
	private final static String cardCancelarRepre = "CancelarRepre";

	private Evento eventoRepre;
	private Representacion repre;

	private CardAnadirRepresentacionCenter anadirRepre;
	private CardPosponerRepresentacionCenter posponerRepre;
	private CardCancelarRepresentacionCenter cancelarRepre;

	private CardLayout layoutRepres = new CardLayout();

	/**
	 * Constructor de PestanaGestionRepresentacionesCenter
	 */
	public PestanaGestionRepresentacionesCenter() {

		TitledBorder centro = new TitledBorder("Vista gestion Representaciones");
		setBorder(centro);

		setLayout(layoutRepres);

		anadirRepre = new CardAnadirRepresentacionCenter();
		posponerRepre = new CardPosponerRepresentacionCenter();
		cancelarRepre = new CardCancelarRepresentacionCenter();

		add(anadirRepre, cardCrearRepre);
		add(posponerRepre, cardPosponerRepre);
		add(cancelarRepre, cardCancelarRepre);
	}

	/**
	 * Metodo para mostrar el LayOut del card de crear representacion
	 */
	public void showCardAnadirRepre() {
		layoutRepres.show(this, cardCrearRepre);
	}

	/**
	 * Metodo para mostrar el LayOut del card de posponer representacion
	 */
	public void showCardPosponerRepre() {
		layoutRepres.show(this, cardPosponerRepre);
	}

	/**
	 * Metodo para mostrar el LayOut del card de cancelar representacion
	 */
	public void showCardCancelarRepre() {
		layoutRepres.show(this, cardCancelarRepre);
	}

	/**
	 * Metodo para obtener el card center para anadir una representacion
	 * 
	 * @return CardAnadirRepresentacionCenter objeto con el card
	 */
	public CardAnadirRepresentacionCenter getCardAnadirRepre() {
		return anadirRepre;
	}

	/**
	 * Metodo para obtener el card center para posponer una representacion
	 * 
	 * @return CardPosponerRepresentacionCenter objeto con el card
	 */
	public CardPosponerRepresentacionCenter getCardPosponerRepre() {
		return posponerRepre;
	}

	/**
	 * Metodo para obtener el card center para cancelar una representacion
	 * 
	 * @return CardPosponerRepresentacionCenter objeto con el card
	 */
	public CardCancelarRepresentacionCenter getCardCancelarRepre() {
		return cancelarRepre;
	}

	/**
	 * Metodo para obtener el Evento de la representacion
	 * 
	 * @return Evento de la representacion
	 */
	public Evento getEventoGestionRepre() {
		return eventoRepre;
	}

	/**
	 * Metodo para establecer el evento de la representacion
	 * 
	 * @param eventoRepre Evento de la representacion
	 */
	public void setEventoGestionRepre(Evento eventoRepre) {
		this.eventoRepre = eventoRepre;
	}

	/**
	 * Metodo para obtener la Representacion a gestionar
	 * 
	 * @return Representacion a gestionar
	 */
	public Representacion getRepresentacionGestion() {
		return repre;
	}

	/**
	 * Metodo para establecer la representacion
	 * 
	 * @param repre Representacion con la representacion
	 */
	public void setRepresentacionGestion(Representacion repre) {
		this.repre = repre;
	}
}

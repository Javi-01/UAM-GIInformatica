package vista.panelAdministrador.pestanaCrearEvento.rellenarCampos;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.tipo.CardCrearEventoConciertoCenter;
import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.tipo.CardCrearEventoDanzaCenter;
import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.tipo.CardCrearEventoTeatroCenter;

/**
 * Clase PestanaCrearEventoCenter para la pestana en el centro de crear evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearEventoCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7546718896121849598L;

	private final static String tituloCardDanza = "Danza";
	private final static String tituloCardConcierto = "Concierto";
	private final static String tituloCardTeatro = "Teatro";

	private CardCrearEventoDanzaCenter cardDanza;
	private CardCrearEventoConciertoCenter cardConcierto;
	private CardCrearEventoTeatroCenter cardTeatro;

	private CardLayout layoutCrearEventos = new CardLayout();

	/**
	 * Constructor de PestanaCrearEventoCenter
	 */
	public PestanaCrearEventoCenter() {

		TitledBorder centro = new TitledBorder("A�adir campos del evento");
		setBorder(centro);
		setLayout(layoutCrearEventos);

		cardDanza = new CardCrearEventoDanzaCenter();
		cardConcierto = new CardCrearEventoConciertoCenter();
		cardTeatro = new CardCrearEventoTeatroCenter();

		add(cardDanza, tituloCardDanza);
		add(cardTeatro, tituloCardTeatro);
		add(cardConcierto, tituloCardConcierto);
	}

	/**
	 * Metodo para mostrar el Layout de crear evento para danza
	 */
	public void showCrearEventoDanza() {
		layoutCrearEventos.show(this, tituloCardDanza);
	}

	/**
	 * Metodo para mostrar el Layout de crear evento para concierto
	 */
	public void showCrearEventoConcierto() {
		layoutCrearEventos.show(this, tituloCardConcierto);
	}

	/**
	 * Metodo para mostrar el Layout de crear evento para teatro
	 */
	public void showCrearEventoTeatro() {
		layoutCrearEventos.show(this, tituloCardTeatro);
	}

	/**
	 * Metodo para obtener el titilo del Card de Danza
	 * 
	 * @return String con el titulo
	 */
	public String getCardEventoDanzaTitulo() {
		return tituloCardDanza;
	}

	/**
	 * Metodo para obtener el titilo del Card de Concierto
	 * 
	 * @return String con el titulo
	 */
	public String getCardEventoConciertoTitulo() {
		return tituloCardConcierto;
	}

	/**
	 * Metodo para obtener el titilo del Card de Teatro
	 * 
	 * @return String con el titulo
	 */
	public String getCardEventoTeatroTitulo() {
		return tituloCardTeatro;
	}

	/**
	 * Metodo para obtener el Card para crear el evento de danza
	 * 
	 * @return CardCrearEventoDanzaCenter con el Card
	 */
	public CardCrearEventoDanzaCenter getCardEventoDanza() {
		return cardDanza;
	}

	/**
	 * Metodo para obtener el Card para crear el evento de concierto
	 * 
	 * @return CardCrearEventoDanzaCenter con el Card
	 */
	public CardCrearEventoConciertoCenter getCardEventoConcierto() {
		return cardConcierto;
	}

	/**
	 * Metodo para obtener el Card para crear el evento de teatro
	 * 
	 * @return CardCrearEventoDanzaCenter con el Card
	 */
	public CardCrearEventoTeatroCenter getCardEventoTeatro() {
		return cardTeatro;
	}

	/**
	 * Metodo para limpiar los campos para la creacion
	 */
	public void limpiarCampos() {
		getCardEventoConcierto().limpiarCardCrearEventoConcierto();
		getCardEventoTeatro().limpiarCardCrearEventoTeatro();
		getCardEventoDanza().limpiarCardCrearEventoDanza();
	}
}

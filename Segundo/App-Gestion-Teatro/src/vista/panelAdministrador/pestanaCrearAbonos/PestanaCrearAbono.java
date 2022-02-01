package vista.panelAdministrador.pestanaCrearAbonos;

import java.awt.CardLayout;

import javax.swing.JPanel;

import vista.panelAdministrador.pestanaCrearAbonos.ciclo.CardCrearCicloCompuesto;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.CardCrearCicloEventos;

/**
 * Clase PestanaCrearAbono para la ventana necesaria para crear los abonos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearAbono extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6274286879417346454L;

	private final static String cardCrear = "CrearAbono";
	private final static String cardAnadirCiclos = "A�adir ciclos";
	private final static String cardAnadirEventos = "A�adir eventos";

	private PestanaCrearAbonoBorder borderAbono;
	private CardCrearCicloCompuesto anadirCicloCompuesto;
	private CardCrearCicloEventos anadirCicloEventos;

	private CardLayout layoutAbono = new CardLayout();

	/**
	 * Constructor de PestanaCrearAbono
	 */
	public PestanaCrearAbono() {

		setLayout(layoutAbono);

		anadirCicloCompuesto = new CardCrearCicloCompuesto();
		anadirCicloEventos = new CardCrearCicloEventos();
		borderAbono = new PestanaCrearAbonoBorder();

		add(borderAbono, cardCrear);
		add(anadirCicloCompuesto, cardAnadirCiclos);
		add(anadirCicloEventos, cardAnadirEventos);

	}

	/**
	 * Metodo para mostrar la pestana de los abonos
	 */
	public void showPestanaBorderAbono() {
		layoutAbono.show(this, cardCrear);
	}

	/**
	 * Metodo para mostrar la pestana para anadir ciclos compuestos
	 */
	public void showPestanaAnadirCicloCompuesto() {
		layoutAbono.show(this, cardAnadirCiclos);
	}

	/**
	 * Metodo para mostrar la pestana para anadir ciclos simples de eventos
	 */
	public void showPestanaAnadirCicloEventos() {
		layoutAbono.show(this, cardAnadirEventos);
	}

	/**
	 * Metodo para obtener la pestana para crear abonos
	 * 
	 * @return PestanaCrearAbonoBorder objeto de la pesatana en el border de crear
	 *         abono
	 */
	public PestanaCrearAbonoBorder getPestanaCrearAbonoBorder() {
		return borderAbono;
	}

	/**
	 * Metodo para obtener el Card de anadir cilocs compuestos
	 * 
	 * @return CardCrearCicloCompuesto objeto del card para crear ciclos compuestos
	 */
	public CardCrearCicloCompuesto getCardAnadirCompuesto() {
		return anadirCicloCompuesto;
	}

	/**
	 * Metodo para obtener el Card de anadir cilocs simples
	 * 
	 * @return CardCrearCicloCompuesto objeto del card para crear ciclos simples
	 */
	public CardCrearCicloEventos getCardAnadirCicloEvento() {
		return anadirCicloEventos;
	}
}

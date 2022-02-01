package vista.panelAdministrador.pestanaCrearEvento;

import java.awt.CardLayout;

import javax.swing.JPanel;

import modelo.evento.Evento;
import vista.panelAdministrador.pestanaCrearEvento.precioZonas.PestanaCrearEventoPrecioZona;
import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.PestanaCrearEventoBorder;

/**
 * Clase PestanaCrearEvento para crear un evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearEvento extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1930655822030567627L;

	private Evento eventoCrear;

	private final static String cardRellenarCampos = "CrearEvento";
	private final static String CardPrecioZonas = "PrecioZonas";

	private PestanaCrearEventoBorder crearEventoBorder;
	private PestanaCrearEventoPrecioZona crearEventoPrecioZona;

	private CardLayout layoutCrearEvento = new CardLayout();

	/**
	 * Constructor de PestanaCrearEvento
	 */
	public PestanaCrearEvento() {
		setLayout(layoutCrearEvento);

		crearEventoBorder = new PestanaCrearEventoBorder();
		crearEventoPrecioZona = new PestanaCrearEventoPrecioZona();

		add(crearEventoBorder, cardRellenarCampos);
		add(crearEventoPrecioZona, CardPrecioZonas);
	}

	/**
	 * Metodo para mostrar la pestana donde se rellenan los campos
	 */
	public void showPestanaRellenarCampos() {
		layoutCrearEvento.show(this, cardRellenarCampos);
	}

	/**
	 * Metodo para mostrar la pestana de los precios de las zonas del evento
	 */
	public void showPestanaPrecioZonas() {
		layoutCrearEvento.show(this, CardPrecioZonas);
	}

	/**
	 * Metodo para obtener la pestana en el borde de crear un evento
	 * 
	 * @return PestanaCrearEventoBorder objeto con la pestana
	 */
	public PestanaCrearEventoBorder getPestanaCrearEventoBorder() {
		return this.crearEventoBorder;
	}

	/**
	 * Metodo para obtener la pestana de los precios de las zonas
	 * 
	 * @return PestanaCrearEventoPrecioZona objeto con la pestana
	 */
	public PestanaCrearEventoPrecioZona getPestanaCrearEventoPrecioZona() {
		return this.crearEventoPrecioZona;
	}

	/**
	 * Metodo para obtener la pestana para crear el evento
	 * 
	 * @return Evento con el evento a crear
	 */
	public Evento getEventoPestanaCrear() {
		return eventoCrear;
	}

	/**
	 * Metodo para establecer la pestana para crear el evento
	 * 
	 * @param eventoCrear	evento a crear
	 */
	public void setEventoPestanaCrear(Evento eventoCrear) {
		this.eventoCrear = eventoCrear;
	}
}

package vista.panelAdministrador.pestanaZonasYAforo.acciones;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controlador.zonasAforo.ControladorBotonAnadirZona;
import controlador.zonasAforo.ControladorBotonCrearZona;
import controlador.zonasAforo.ControladorFiltroTipoZonaCrear;
import controlador.zonasAforo.ControladorSeleccionarZonaCrearCompuesta;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroCenter;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroWest;

/**
 * Clase CardCrearZonasTeatroBorder para la card border para crear zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonasTeatroBorder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8622058928265451440L;

	private CardCrearZonaTeatroCenter cardCrearZonaCenter;
	private CardCrearZonaTeatroWest cardCrearZonaWest;

	private BorderLayout layoutCrear = new BorderLayout(2, 2);

	/**
	 * Constructor de CardCrearZonasTeatroBorder
	 */
	public CardCrearZonasTeatroBorder() {

		setLayout(layoutCrear);

		cardCrearZonaCenter = new CardCrearZonaTeatroCenter();
		cardCrearZonaWest = new CardCrearZonaTeatroWest();

		add(cardCrearZonaCenter, BorderLayout.CENTER);
		add(cardCrearZonaWest, BorderLayout.WEST);

		ControladorFiltroTipoZonaCrear cTipoZonaCrear = new ControladorFiltroTipoZonaCrear(cardCrearZonaWest,
				cardCrearZonaCenter);

		getCardCrearZonaWest().setControlador(cTipoZonaCrear);

		ControladorBotonCrearZona cCrearZona = new ControladorBotonCrearZona(cardCrearZonaCenter, cardCrearZonaWest);

		getCardCrearZonaCenter().getCardZonaPie().setControlador(cCrearZona);
		getCardCrearZonaCenter().getCardZonaSentado().setControlador(cCrearZona);
		getCardCrearZonaCenter().getCardZonaCompuesta().getZonaBotonAceptar().setControladorCrear(cCrearZona);

		ControladorBotonAnadirZona cAnadirZona = new ControladorBotonAnadirZona(cardCrearZonaCenter);

		getCardCrearZonaCenter().getCardZonaCompuesta().getZonaComponentes().setControlador(cAnadirZona);

		ControladorSeleccionarZonaCrearCompuesta cSeleccionarZona = new ControladorSeleccionarZonaCrearCompuesta(
				cardCrearZonaCenter);

		getCardCrearZonaCenter().getCardZonaTabla().setControlador(cSeleccionarZona);
	}

	/**
	 * Metodo para obtener el card de crear zonas en el centro
	 * 
	 * @return CardCrearZonaTeatroCenter
	 */
	public CardCrearZonaTeatroCenter getCardCrearZonaCenter() {
		return cardCrearZonaCenter;
	}

	/**
	 * Metodo para obtener el card de crear zonas en el west
	 * 
	 * @return CardCrearZonaTeatroWest
	 */
	public CardCrearZonaTeatroWest getCardCrearZonaWest() {
		return cardCrearZonaWest;
	}

}

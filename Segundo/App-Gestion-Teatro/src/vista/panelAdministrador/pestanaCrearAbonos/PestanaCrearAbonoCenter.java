package vista.panelAdministrador.pestanaCrearAbonos;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import vista.panelAdministrador.pestanaCrearAbonos.anual.PestanaCardCrearAnual;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.PestanaCardCrearCiclo;

/**
 * Clase PestanaCrearAbonoCenter para la ventana necesaria para crear los abonos
 * en el centro
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearAbonoCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6274286879417346454L;

	private final static String cardAbonoAnual = "CrearAbono";
	private final static String cardCiclo = "Crear ciclo";

	private PestanaCardCrearAnual crearAnual;
	private PestanaCardCrearCiclo crearCiclo;

	private CardLayout layoutCrearAbono = new CardLayout();

	/**
	 * Constructor de PestanaCrearAbonoCenter
	 */
	public PestanaCrearAbonoCenter() {

		TitledBorder centro = new TitledBorder("Vista crear Abonos");
		setBorder(centro);
		setLayout(layoutCrearAbono);

		crearCiclo = new PestanaCardCrearCiclo();
		crearAnual = new PestanaCardCrearAnual();

		add(crearAnual, cardAbonoAnual);
		add(crearCiclo, cardCiclo);

	}

	/**
	 * Metodo para mostrar la pestana para crear abonos anuales
	 */
	public void showPestanaCrearAbonoAnual() {
		layoutCrearAbono.show(this, cardAbonoAnual);
	}

	/**
	 * Metodo para mostrar la pestana crear ciclos
	 */
	public void showPestanaCrearCiclo() {
		layoutCrearAbono.show(this, cardCiclo);
	}

	/**
	 * Metodo para obtener la pestana para crear abonos anuales
	 * 
	 * @return PestanaCardCrearAnual objeto de la card para crear el anual
	 */
	public PestanaCardCrearAnual getPestanaCrearAbonoAnual() {
		return this.crearAnual;
	}

	/**
	 * Metodo para obtener la pestana crear ciclos
	 * 
	 * @return PestanaCardCrearCiclo objeto de la card para crear el ciclo
	 */
	public PestanaCardCrearCiclo getPestanaCrearCiclo() {
		return this.crearCiclo;
	}
}

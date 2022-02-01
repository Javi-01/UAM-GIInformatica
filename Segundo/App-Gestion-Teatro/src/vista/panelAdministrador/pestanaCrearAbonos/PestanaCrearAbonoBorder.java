package vista.panelAdministrador.pestanaCrearAbonos;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import controlador.abonos.ControladorFiltroCrearAbono;

/**
 * Clase PestanaCrearAbonoBorder para la ventana necesaria para crear los abonos
 * en el border del frame
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearAbonoBorder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6274286879417346454L;

	PestanaCrearAbonoCenter vistaCrearAbonoCenter;
	PestanaCrearAbonoWest vistaCrearAbonoWest;

	BorderLayout layoutCrearAbono = new BorderLayout(2, 2);

	/**
	 * Constructor de PestanaCrearAbonoBorder
	 */
	public PestanaCrearAbonoBorder() {

		setLayout(layoutCrearAbono);

		vistaCrearAbonoCenter = new PestanaCrearAbonoCenter();
		vistaCrearAbonoWest = new PestanaCrearAbonoWest();

		ControladorFiltroCrearAbono cFiltroCrearAbono = new ControladorFiltroCrearAbono(vistaCrearAbonoCenter,
				vistaCrearAbonoWest);

		add(vistaCrearAbonoWest, BorderLayout.WEST);
		add(vistaCrearAbonoCenter, BorderLayout.CENTER);

		vistaCrearAbonoWest.setControlador(cFiltroCrearAbono);
	}

	/**
	 * Metodo para obtener la pestana de crear abonos en el centro del frame
	 * 
	 * @return PestanaCrearAbonoCenter objeto de la pesatana en el centro de crear
	 *         abono
	 */
	public PestanaCrearAbonoCenter getPestanaCrearAbonoCenter() {
		return vistaCrearAbonoCenter;
	}

	/**
	 * Metodo para obtener la pestana de crear abonos en el west del frame
	 * 
	 * @return PestanaCrearAbonoWest objeto de la pesatana en el west de crear abono
	 */
	public PestanaCrearAbonoWest getPestanaCrearAbonoWest() {
		return vistaCrearAbonoWest;
	}
}

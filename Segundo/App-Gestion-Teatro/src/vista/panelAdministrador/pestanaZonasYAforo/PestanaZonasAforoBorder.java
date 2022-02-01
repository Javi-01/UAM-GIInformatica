package vista.panelAdministrador.pestanaZonasYAforo;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Clase PestanaZonasAforoBorder para la pestana border de las zonas y aforo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaZonasAforoBorder extends JPanel {

	private static final long serialVersionUID = 7669975780185581059L;

	private PestanaZonasAforoCenter vistaZonasAforoCenter;
	private PestanaZonasAforoWest vistaZonasAforoWest;

	private BorderLayout layoutBorder = new BorderLayout(2, 2);

	/**
	 * Constructor de PestanaZonasAforoBorder
	 */
	public PestanaZonasAforoBorder() {
		setLayout(layoutBorder);

		vistaZonasAforoCenter = new PestanaZonasAforoCenter();
		vistaZonasAforoWest = new PestanaZonasAforoWest();

		add(vistaZonasAforoCenter, BorderLayout.CENTER);
		add(vistaZonasAforoWest, BorderLayout.WEST);
	}

	/**
	 * Metodo para obtener la pestana del aforo de las zonas
	 * 
	 * @return PestanaZonasAforoCenter objeto con la pestana
	 */
	public PestanaZonasAforoCenter getVistaZonasAforoCenter() {
		return vistaZonasAforoCenter;
	}

	/**
	 * Metodo para obtener la pestana del aforo de las zonas
	 * 
	 * @return PestanaZonasAforoCenter objeto con la pestana
	 */
	public PestanaZonasAforoWest getVistaZonasAforoWest() {
		return vistaZonasAforoWest;
	}

}

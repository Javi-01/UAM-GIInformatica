package vista.panelAdministrador.pestanaGestionRepresentaciones;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaGestionRepresentacionesWest para la pestana de gestion de
 * representaciones en west
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaGestionRepresentacionesWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4808906677513352534L;

	private static final String[] accionesRepres = { "Añadir", "Cancelar", "Posponer" };
	JComboBox<String> cbRepresentaciones = new JComboBox<>(accionesRepres);

	/**
	 * Constructor de PestanaGestionRepresentacionesWest
	 */
	public PestanaGestionRepresentacionesWest() {

		TitledBorder oeste = new TitledBorder("Opciones");
		setBorder(oeste);
		add(cbRepresentaciones);
	}

	/**
	 * Metodo para establecer el controlador del JComboBox
	 * 
	 * @param c ItemListener con el controlador
	 */
	public void setControlador(ItemListener c) {
		cbRepresentaciones.addItemListener(c);
	}

	/**
	 * Metodo para obtener el JCombobox de las posibles acciones para
	 * representaciones
	 * 
	 * @return JComboBox con las posibles acciones para representaciones
	 */
	public JComboBox<String> getCombobox() {
		return this.cbRepresentaciones;
	}

	/**
	 * Metodo para obtener la dimension establecida
	 * 
	 * @return Dimension con la nueva dimension
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}
}

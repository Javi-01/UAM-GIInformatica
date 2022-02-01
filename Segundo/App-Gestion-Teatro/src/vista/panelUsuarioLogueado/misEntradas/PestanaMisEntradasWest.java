package vista.panelUsuarioLogueado.misEntradas;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaMisEntradasCentre para el card west del panel de mis entradas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaMisEntradasWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8806711538041267601L;

	private static final String[] tiposEntradas = { "Compradas", "Reservadas" };
	JComboBox<String> cbEntradas = new JComboBox<>(tiposEntradas);

	/**
	 * Constructor de PestanaMisEntradasWest
	 */
	public PestanaMisEntradasWest() {

		TitledBorder oeste = new TitledBorder("Filtrar Mis Entradas");
		setBorder(oeste);
		add(cbEntradas);
	}

	/**
	 * Metodo para establecer el controlador del JComboBox del tipo de entradas
	 * 
	 * @param c ItemListener con el controlador
	 */
	public void setControlador(ItemListener c) {
		cbEntradas.addItemListener(c);
	}

	/**
	 * Metodo para obtener el JComboBox del tipo de entradas
	 * 
	 * @return JComboBox con el JComboBox del tipo de entradas
	 */
	public JComboBox<String> getCombobox() {
		return this.cbEntradas;
	}

	/**
	 * Metodo para obtener la dimension establecida
	 * 
	 * @return Dimension con la dimension
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}
}

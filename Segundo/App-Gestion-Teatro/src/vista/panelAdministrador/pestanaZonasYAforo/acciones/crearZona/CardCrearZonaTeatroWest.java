package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase CardCrearZonaTeatroWest para crear zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaTeatroWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7505020455034703949L;

	private static final String[] tiposCrearZona = { "Zona Pie", "Zona Sentado", "Zona Compuesta" };
	JComboBox<String> cbCrearZona = new JComboBox<>(tiposCrearZona);

	/**
	 * Constructor de CardCrearZonaTeatroWest
	 */
	public CardCrearZonaTeatroWest() {

		TitledBorder oeste = new TitledBorder("Zona");
		setBorder(oeste);
		add(cbCrearZona);
	}

	/**
	 * Metodo para establecer el controlador del JComboBox de crear zona
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ItemListener c) {
		cbCrearZona.addItemListener(c);
	}

	/**
	 * Metodo para obtener el combobox de las zonas a crear
	 * 
	 * @return JComboBox con jcombobox
	 */
	public JComboBox<String> getCombobox() {
		return this.cbCrearZona;
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

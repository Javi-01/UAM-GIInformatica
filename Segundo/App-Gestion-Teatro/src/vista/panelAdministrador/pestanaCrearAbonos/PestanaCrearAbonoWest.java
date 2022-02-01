package vista.panelAdministrador.pestanaCrearAbonos;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaCrearAbonoCenter para la ventana necesaria para crear los abonos
 * en el centro
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearAbonoWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5608392411162038484L;

	private static final String[] tiposAbonos = { "Abono Anual", "Ciclo" };
	JComboBox<String> cbRepresentaciones = new JComboBox<>(tiposAbonos);

	/**
	 * Constructor de PestanaCrearAbonoWest
	 */
	public PestanaCrearAbonoWest() {

		TitledBorder oeste = new TitledBorder("Tipo Abono");
		setBorder(oeste);
		add(cbRepresentaciones);
	}

	/**
	 * Metodo establecer el controlador de cbRepresentaciones
	 * 
	 * @param c ItemListener con el controlador a anadir
	 */
	public void setControlador(ItemListener c) {
		cbRepresentaciones.addItemListener(c);
	}

	/**
	 * Metodo obtener el combobox de cbRepresentaciones
	 * 
	 * @return JComboBox con los tipos de abonos
	 */
	public JComboBox<String> getCombobox() {
		return this.cbRepresentaciones;
	}

	/**
	 * Metodo para obtener y establecer la dimension
	 * 
	 * @return Dimension nueva dimension
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}
}

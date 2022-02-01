package vista.panelUsuarioLogueado.pestanaCompraAbono;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaCompraAbonoWest para la pestana compra de abono west
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCompraAbonoWest extends JPanel {

	private static final long serialVersionUID = -8367350996184929624L;

	private static final String[] tiposAbonos = { "Abono Anual", "Ciclo" };
	JComboBox<String> cbAbonos = new JComboBox<>(tiposAbonos);

	/**
	 * Constructor de PestanaCompraAbonoWest
	 */
	public PestanaCompraAbonoWest() {

		TitledBorder oeste = new TitledBorder("Selecciona Abono");
		setBorder(oeste);
		add(cbAbonos);
	}

	/**
	 * Metodo para establecer el controlador del JComboBox
	 * 
	 * @param c ItemListener con el controlador
	 */
	public void setControlador(ItemListener c) {
		cbAbonos.addItemListener(c);
	}

	/**
	 * Metodo para obtener el JComboBox
	 * 
	 * @return JComboBox
	 */
	public JComboBox<String> getCombobox() {
		return this.cbAbonos;
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

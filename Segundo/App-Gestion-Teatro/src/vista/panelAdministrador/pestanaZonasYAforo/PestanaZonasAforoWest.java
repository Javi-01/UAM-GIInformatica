package vista.panelAdministrador.pestanaZonasYAforo;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaZonasAforoWest para la pestana west del aforo de las zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaZonasAforoWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3714715969253392710L;

	private static final String[] tiposCambiosZyA = { "Crear Zonas", "Reducir aforo", "Deshabilitar butaca",
			"Entradas a comprar" };
	JComboBox<String> cbCambiarZyA = new JComboBox<>(tiposCambiosZyA);

	/**
	 * Constructor de PestanaZonasAforoWest
	 */
	public PestanaZonasAforoWest() {

		TitledBorder oeste = new TitledBorder("Opciones");
		setBorder(oeste);
		add(cbCambiarZyA);
	}

	/**
	 * Metodo para establecer el controlador del JComboBox
	 * 
	 * @param c ItemListener con el metodo
	 */
	public void setControlador(ItemListener c) {
		cbCambiarZyA.addItemListener(c);
	}

	/**
	 * Metodo para obtener el JComboBox
	 * 
	 * @return JComboBox
	 */
	public JComboBox<String> getCombobox() {
		return this.cbCambiarZyA;
	}

	/**
	 * Metodo para obtener la dimension establecida
	 * 
	 * @return Dimension con la nueva dimention
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}
}

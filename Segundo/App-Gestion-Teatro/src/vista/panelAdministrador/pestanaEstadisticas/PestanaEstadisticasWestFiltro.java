package vista.panelAdministrador.pestanaEstadisticas;

import java.awt.GridLayout;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaEstadisticasWestFiltro para la pestana west del filto de
 * estadisticas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaEstadisticasWestFiltro extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3775671348187822009L;

	private static final String[] tiposEstadisticas = { "Ocupacion", "Recaudacion" };

	JComboBox<String> cbEstadisticas = new JComboBox<>(tiposEstadisticas);
	GridLayout layoutFiltroEstadisticas = new GridLayout(5, 1, 10, 10);

	/**
	 * Constructor de PestanaEstadisticasWestFiltro
	 */
	public PestanaEstadisticasWestFiltro() {
		TitledBorder filtro = new TitledBorder("Filtrar Busqueda");
		setBorder(filtro);

		setLayout(layoutFiltroEstadisticas);

		add(cbEstadisticas);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
	}

	/**
	 * Metodo para establecer el controlador del JCombobox de los tipos de
	 * estadisticas
	 * 
	 * @param c ItemListener con el controlador
	 */
	public void setControlador(ItemListener c) {
		cbEstadisticas.addItemListener(c);
	}

	/**
	 * Metodo para obtener el JComboBox con los tipos de estadisticas
	 * 
	 * @return JComboBox con los tipos de estadisticas
	 */
	public JComboBox<String> getComboboxFiltro() {
		return this.cbEstadisticas;
	}
}

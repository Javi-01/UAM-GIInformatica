package vista.panelAdministrador.pestanaEstadisticas;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaEstadisticasWestTipoBusqueda para la pestana west del tipo de
 * busqueda de estadisticas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaEstadisticasWestTipoBusqueda extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1039241677197771323L;

	private JTextField txtBuscarEstadistica = new JTextField(12);
	private JButton btBuscarEstadisticas = new JButton("Buscar");

	GridLayout layoutFiltroEstadisticas = new GridLayout(5, 1, 10, 10);

	/**
	 * Constructor de PestanaEstadisticasWestFiltro
	 */
	public PestanaEstadisticasWestTipoBusqueda() {

		TitledBorder filtro = new TitledBorder("Filtro avanzado");
		setBorder(filtro);
		setLayout(layoutFiltroEstadisticas);

		add(new JLabel("Nombre un Evento o Zona"));
		add(txtBuscarEstadistica);
		add(btBuscarEstadisticas);
		add(new JLabel(""));
		add(new JLabel(""));
	}

	/**
	 * Metodo para establecer el controlador del boton de buscar
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		btBuscarEstadisticas.addActionListener(c);
	}

	/**
	 * Metodo para obtener el nombre para filtrar las estadisticas
	 * 
	 * @return String con la cadena a filtrar
	 */
	public String getNombreFiltroEstadisticas() {
		return txtBuscarEstadistica.getText();
	}

	/**
	 * Metodo para limpiar el campo del JTextField
	 */
	public void limpiarFiltroEstadisticas() {
		txtBuscarEstadistica.setText("");
	}

}

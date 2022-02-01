package vista.panelPrincipal.pestanaRepresentaciones;

import java.awt.*;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaFiltrarRepresentacionesWest para la pestana de filtro de
 * representaciones
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaFiltrarRepresentacionesWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1434162000713188068L;

	private static final String[] tiposEventos = { "Danza", "Teatro", "Concierto" };
	JComboBox<String> cbRepresentaciones = new JComboBox<>(tiposEventos);

	/**
	 * Constructor de PestanaFiltrarRepresentacionesWest
	 */
	public PestanaFiltrarRepresentacionesWest() {

		TitledBorder oeste = new TitledBorder("Filtrar Representaciones");
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
	 * Metodo para obtener el JComboBox con los tipos de eventos
	 * 
	 * @return JComboBox con los tipos de eventos
	 */
	public JComboBox<String> getCombobox() {
		return this.cbRepresentaciones;
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

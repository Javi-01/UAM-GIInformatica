package vista.panelAdministrador.pestanaCrearEvento.rellenarCampos;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaCrearEventoWest para la pestana en el west de crear evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearEventoWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4120431724484394763L;

	private static final String[] tiposEventos = { "Danza", "Teatro", "Concierto" };
	JComboBox<String> CbTiposEventos = new JComboBox<>(tiposEventos);

	/**
	 * Constructor de PestanaCrearEventoWest
	 */
	public PestanaCrearEventoWest() {

		TitledBorder oeste = new TitledBorder("Tipo de Evento");
		setBorder(oeste);
		setLayout(new FlowLayout());
		add(CbTiposEventos);
	}

	/**
	 * Metodo para establecer el controlador del JCombobox de los tipos
	 * 
	 * @param c ItemListener con el controlador
	 */
	public void setControlador(ItemListener c) {
		CbTiposEventos.addItemListener(c);
	}

	/**
	 * Metodo para obtener el JComboboxde los tipos de eventos
	 * 
	 * @return JComboBox de los tipos de eventos
	 */
	public JComboBox<String> getCombobox() {
		return this.CbTiposEventos;
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

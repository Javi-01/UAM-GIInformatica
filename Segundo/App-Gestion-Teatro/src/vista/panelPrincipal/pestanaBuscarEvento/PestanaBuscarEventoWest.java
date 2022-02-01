package vista.panelPrincipal.pestanaBuscarEvento;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaBuscarEventoWest para la busqueda de eventos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaBuscarEventoWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 728679869346462349L;

	private JTextField txtBuscarEvento = new JTextField(12);
	private JButton btBuscarEvento = new JButton("Buscar");

	/**
	 * Constructor de PestanaBuscarEventoWest
	 */
	public PestanaBuscarEventoWest() {

		TitledBorder oeste = new TitledBorder("Introduce el nombre");
		setBorder(oeste);
		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes
	 */
	public void anadirComponentes() {
		add(txtBuscarEvento);
		add(btBuscarEvento);
	}

	/**
	 * Metodo para obtener la dimension
	 * 
	 * @return Dimension con la dimension
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}

	/**
	 * Metodo para establecer el controlador del boton de buscar evento
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		btBuscarEvento.addActionListener(c);
	}

	/**
	 * Metodo para obtener el evento a buscar
	 * 
	 * @return String con el evento a buscar
	 */
	public String getPestanaNombreBuscarEvento() {
		return txtBuscarEvento.getText();
	}

	/**
	 * Metodo para limpiar los textfield
	 */
	public void limpiarPestanaBuscarEvento() {
		txtBuscarEvento.setText("");
	}
}

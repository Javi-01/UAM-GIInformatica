package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase CardCrearZonaCompuestaAceptar para aceptar la creacion de la zona
 * compuesta
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaCompuestaAceptar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -453899139710290710L;

	private JButton btAceptar = new JButton("Aceptar");
	private GridLayout layoutCrear = new GridLayout(2, 2, 25, 60);

	/**
	 * Constructor de CardCrearZonaCompuestaAceptar
	 */
	public CardCrearZonaCompuestaAceptar() {
		setLayout(layoutCrear);

		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btAceptar);
	}

	/**
	 * Metodo para establecer el controlador del boton de aceptar de la creacion
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorCrear(ActionListener e) {
		btAceptar.addActionListener(e);
	}
}

package vista.panelAdministrador.pestanaCrearAbonos.ciclo;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase PestanaCrearCicloVolver para el card para volver al crear un ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearCicloVolver extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2420256116723785911L;
	private JButton btVolverCiclo = new JButton("Volver");

	GridLayout layoutVolverCiclo = new GridLayout(3, 2, 179, 59);

	/**
	 * Constructor de PestanaCrearCicloVolver
	 */
	public PestanaCrearCicloVolver() {

		setLayout(layoutVolverCiclo);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btVolverCiclo);
	}

	/**
	 * Metodo para establecer el controlador del boton de volver
	 * 
	 * @param e ActionListener con el controlador a anadir
	 */
	public void setControlador(ActionListener e) {
		btVolverCiclo.addActionListener(e);
	}
}

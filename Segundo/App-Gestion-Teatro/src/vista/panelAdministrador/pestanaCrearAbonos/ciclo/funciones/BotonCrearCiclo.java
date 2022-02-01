package vista.panelAdministrador.pestanaCrearAbonos.ciclo.funciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase BotonCrearCiclo para el boton para crear un ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class BotonCrearCiclo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6476328987312036062L;

	private JButton btCrearCiclo = new JButton("Crear");
	private GridLayout layoutCrear = new GridLayout(2, 2, 18, 79);

	/**
	 * Constructor de BotonCrearCiclo
	 */
	public BotonCrearCiclo() {
		setLayout(layoutCrear);

		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btCrearCiclo);

	}

	/**
	 * Metodo para establecer el controlador del boton de crear
	 * 
	 * @param e ActionListener con el controlador a anadir
	 */
	public void setControladorCrear(ActionListener e) {
		btCrearCiclo.addActionListener(e);
	}
}

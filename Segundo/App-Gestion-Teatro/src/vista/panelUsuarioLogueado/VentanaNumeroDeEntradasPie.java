package vista.panelUsuarioLogueado;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import modelo.sistema.Sistema;

/**
 * Clase VentanaNumeroDeEntradasPie para el panel para obtener el numero de
 * entradas de pie
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaNumeroDeEntradasPie extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -116377376683769492L;

	GridLayout layoutEntradas = new GridLayout(3, 1, 10, 10);

	JSlider slEntradas = new JSlider(1, Sistema.getInstance().getNumMaxEntradas());
	JButton btAceptar = new JButton("Confirmar");

	/**
	 * Constructor de VentanaNumeroDeEntradasPie
	 */
	public VentanaNumeroDeEntradasPie() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Numero de entradas Pie");
		setBounds(0, 0, 300, 180);
		setLocationRelativeTo(null);
		setLayout(layoutEntradas);

		slEntradas.setMajorTickSpacing(1);
		slEntradas.setMinorTickSpacing(0);
		slEntradas.setPaintTicks(true);
		slEntradas.setPaintLabels(true);
		slEntradas.setSnapToTicks(true);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir componentes
	 */
	public void anadirComponentes() {

		add(slEntradas);
		add(new JLabel(""));
		add(btAceptar);
	}

	/**
	 * Metodo para establecer el controlador del boton de aceptar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorBoton(ActionListener e) {
		btAceptar.addActionListener(e);
	}

	/**
	 * Metodo para establecer el controlador de JSlider
	 * 
	 * @param e ChangeListener con el controlador
	 */
	public void setControladorSlider(ChangeListener e) {
		slEntradas.addChangeListener(e);
	}

	/**
	 * Metodo para obtener el JSlider de las entradas elegidas
	 * 
	 * @return JSlider con las entradas elegidas
	 */
	public JSlider getJSliderNumEntradas() {
		return slEntradas;
	}
}

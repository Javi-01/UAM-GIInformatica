package vista.panelUsuarioLogueado;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import modelo.sistema.Sistema;

/**
 * Clase VentanaNumeroDeEntradasSentado para el panel para obtener del numero de
 * entradas elegidas sentado
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaNumeroDeEntradasSentado extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -116377376683769492L;
	private static final String[] preferenciasEntradas = { "Juntas", "Al Frente", "Detras", "Alejadas",
			"Sin sugerencia" };

	GridLayout layoutEntradas = new GridLayout(4, 1, 10, 22);

	JSlider slEntradas = new JSlider(1, Sistema.getInstance().getNumMaxEntradas());
	JComboBox<String> cbSugerirEntradas = new JComboBox<>(preferenciasEntradas);
	JButton btAceptar = new JButton("Confirmar");

	/**
	 * Constructor de VentanaNumeroDeEntradasSentado
	 */
	public VentanaNumeroDeEntradasSentado() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Numero de entradas");
		setBounds(0, 0, 300, 280);
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
		add(cbSugerirEntradas);
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
	 * Metodo para establecer el controlador del JComboBox de las sugerencias
	 * 
	 * @param e ItemListener con el controlador
	 */
	public void setControladorCb(ItemListener e) {
		cbSugerirEntradas.addItemListener(e);
	}

	/**
	 * Metodo para obtener el JSlider de las entradas elegidas
	 * 
	 * @return JSlider con las entradas elegidas
	 */
	public JSlider getJSliderNumEntradas() {
		return slEntradas;
	}

	/**
	 * Metodo para obtener el JComboBox de la sugerencia seleccionada
	 * 
	 * @return JComboBox con la sugerencia
	 */
	public JComboBox<String> getCbSugerirEntradas() {
		return cbSugerirEntradas;
	}

}

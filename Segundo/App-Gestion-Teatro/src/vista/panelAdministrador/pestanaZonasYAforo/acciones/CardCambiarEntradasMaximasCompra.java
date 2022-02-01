package vista.panelAdministrador.pestanaZonasYAforo.acciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 * Clase CardCambiarEntradasMaximasCompra para la card para cambiar el numero
 * maximo de entradas a comprar
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCambiarEntradasMaximasCompra extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8287395585823941562L;

	private JLabel lblNumMax = new JLabel("Seleccione un numero maximo de entradas en cada venta: ");
	private JSlider slMaxEntradas = new JSlider(1, 10);
	private JButton btConfirmar = new JButton("Aceptar");

	private GridLayout layoutMax = new GridLayout(8, 1, 20, 20);

	/**
	 * Constructor de CardCambiarEntradasMaximasCompra
	 */
	public CardCambiarEntradasMaximasCompra() {
		setLayout(layoutMax);

		slMaxEntradas.setMajorTickSpacing(1);
		slMaxEntradas.setMinorTickSpacing(0);
		slMaxEntradas.setPaintTicks(true);
		slMaxEntradas.setPaintLabels(true);
		slMaxEntradas.setSnapToTicks(true);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes al card
	 */
	private void anadirComponentes() {

		add(lblNumMax);
		add(slMaxEntradas);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btConfirmar);
	}

	/**
	 * Metodo para establecer el controlador del JSlider
	 * 
	 * @param e ChangeListener con el metodo
	 */
	public void setControladorSlider(ChangeListener e) {
		slMaxEntradas.addChangeListener(e);
	}

	/**
	 * Metodo para establecer el controlador del JButton
	 * 
	 * @param e ActionListener con el metodo
	 */
	public void setControladorAceptar(ActionListener e) {
		btConfirmar.addActionListener(e);
	}

	/**
	 * Metodo para obtener el JSlider del maximo de entradas establecido
	 * 
	 * @return JSlider
	 */
	public JSlider getJSliderNumEntradas() {
		return slMaxEntradas;
	}
}

package vista.panelUsuarioLogueado.pestanaCompraAbono;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase PestanaAbonoResumenCompraBasico para la pestana de resumen de compra
 * con mas informacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaAbonoResumenCompraMasInfo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2800756850813247087L;

	private JTextField txtElementos = new JTextField(10);
	private JButton btComprar = new JButton("Comprar");

	private GridLayout layoutCompra = new GridLayout(4, 1, 25, 25);

	/**
	 * Constructor de PestanaAbonoResumenCompraMasInfo
	 */
	public PestanaAbonoResumenCompraMasInfo() {
		setLayout(layoutCompra);

		txtElementos.setEditable(false);
		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes
	 */
	private void anadirComponentes() {

		add(txtElementos);
		add(new JLabel(""));
		add(new JLabel(""));
		add(btComprar);
	}

	/**
	 * Metodo para establecer el controlador del boton comprar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControlador(ActionListener e) {
		btComprar.addActionListener(e);
	}

	/**
	 * Metodo para establecer los elementos del abono
	 * 
	 * @param elemns String con el nomrbe
	 */
	public void setTxtElementosAbono(String elemns) {
		txtElementos.setText(elemns);
	}

	/**
	 * Metodo para limpiar los textfield
	 */
	public void limpiarTxtElementosAbono() {
		txtElementos.setText("");
	}
}

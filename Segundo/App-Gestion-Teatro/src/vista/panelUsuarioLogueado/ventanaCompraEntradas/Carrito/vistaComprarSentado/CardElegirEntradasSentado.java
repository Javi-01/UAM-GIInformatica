package vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.vistaComprarSentado;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardElegirEntradasSentado card para elegir en sentado
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardElegirEntradasSentado extends JPanel {

	private static final long serialVersionUID = -7287663609533396755L;

	private JLabel lblElegirSentado = new JLabel("Introduce los numeros de las butacas (Separado por espacios): ");
	private JTextField txtElegirSentado = new JTextField(10);

	private JButton btAceptar = new JButton("Aceptar");

	private GridLayout layoutElegir = new GridLayout(2, 2, 10, 10);

	/**
	 * Constructor de CardElegirEntradasSentado
	 */
	public CardElegirEntradasSentado() {
		setLayout(layoutElegir);

		add(lblElegirSentado);
		add(new JLabel(""));
		add(txtElegirSentado);
		add(btAceptar);
	}

	public String getTxtElegirSentado() {
		return txtElegirSentado.getText();
	}

	public void setControladorAceptar(ActionListener e) {
		btAceptar.addActionListener(e);
	}

	public void limpiarEntradas() {
		txtElegirSentado.setText("");
	}
}

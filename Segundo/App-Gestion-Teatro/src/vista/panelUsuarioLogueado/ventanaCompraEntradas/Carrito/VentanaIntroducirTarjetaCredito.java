package vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Clase VentanaIntroducirTarjetaCredito ventana para introducir la tarjeta de
 * credito
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaIntroducirTarjetaCredito extends JFrame {

	private static final long serialVersionUID = -7441176345105174794L;
	private GridLayout layoutTarjeta = new GridLayout(2, 2, 10, 10);

	private JTextField txtTarjeta = new JTextField(10);
	private JLabel lblTarjeta = new JLabel("Numero de Tarjeta: ");
	private JButton btComprar = new JButton("Comprar");

	/**
	 * Constructor de VentanaIntroducirTarjetaCredito
	 */
	public VentanaIntroducirTarjetaCredito() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Tarjeta de Credito");
		setBounds(0, 0, 350, 100);
		setLocationRelativeTo(null);
		setLayout(layoutTarjeta);

		add(lblTarjeta);
		add(txtTarjeta);
		add(new JLabel(""));
		add(btComprar);
	}

	public void setControladorCompra(ActionListener e) {
		btComprar.addActionListener(e);
	}

	public String getTxtTarjetaCompra() {
		return txtTarjeta.getText();
	}

	public void limpiarTxtTarjetaCompra() {
		txtTarjeta.setText("");
	}
}

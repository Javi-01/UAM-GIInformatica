package vista.panelUsuarioLogueado.misEntradas;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Clase VentanaTarjetaMisEntradas para la ventana comprar con tarjeta
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaTarjetaMisEntradas extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1930723072196240183L;

	private GridLayout layoutTarjeta = new GridLayout(2, 2, 10, 10);

	private JTextField txtTarjeta = new JTextField(10);
	private JLabel lblTarjeta = new JLabel("Numero de Tarjeta: ");
	private JButton btComprar = new JButton("Comprar");

	/**
	 * Constructor de VentanaTarjetaMisEntradas
	 */
	public VentanaTarjetaMisEntradas() {

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

	/**
	 * Metodo para establecer el controlador del boton de comprar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorCompra(ActionListener e) {
		btComprar.addActionListener(e);
	}

	/**
	 * Metodo para obtener el numero de tarjeta del JTextField
	 * 
	 * @return String con el numero de tarjeta
	 */
	public String getTxtTarjetaCompra() {
		return txtTarjeta.getText();
	}

	/**
	 * Metodo para liberar el JTextField
	 */
	public void limpiarTxtTarjetaCompra() {
		txtTarjeta.setText("");
	}
}

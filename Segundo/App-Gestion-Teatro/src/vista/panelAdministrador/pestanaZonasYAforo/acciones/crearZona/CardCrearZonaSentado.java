package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCrearZonaSentado para crear zonas sentado
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaSentado extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1025770870610865089L;

	private JLabel lblCrearSentado = new JLabel("Nombre zona Sentado: ");
	private JTextField txtNombreSentado = new JTextField(12);

	private JLabel lblFilas = new JLabel("Numero de filas: ");
	private JTextField txtFilas = new JTextField(12);
	private JLabel lblColumnas = new JLabel("Numero de columnas: ");
	private JTextField txtColumnas = new JTextField(12);

	private JButton btAceptar = new JButton("Aceptar");

	private GridLayout layoutSentado = new GridLayout(7, 2, 0, 30);

	/**
	 * Constructor de CardCrearZonaSentado
	 */
	public CardCrearZonaSentado() {

		setLayout(layoutSentado);
		anadirComponentes();
	}

	/**
	 * Metodo para anadir componentes al card
	 */
	private void anadirComponentes() {

		add(lblCrearSentado);
		add(txtNombreSentado);
		add(lblFilas);
		add(txtFilas);
		add(lblColumnas);
		add(txtColumnas);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btAceptar);
	}

	/**
	 * Metodo para establecer el controlador del boton de aceptar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControlador(ActionListener e) {
		btAceptar.addActionListener(e);
	}

	/**
	 * Metodo para obtener el nombre de la zona indicado en el JTextField
	 * 
	 * @return String con el nombre de la zona
	 */
	public String getTxtNombreZonaSentado() {
		return txtNombreSentado.getText();
	}

	/**
	 * Metodo para obtener el numero de columnas de la zona indicado en el
	 * JTextField
	 * 
	 * @return Integer con el numero de columnas de la zona
	 */
	public Integer getTxtColumnasSentado() {
		try {
			return Integer.parseInt(txtColumnas.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo para obtener el numero de filas de la zona indicado en el JTextField
	 * 
	 * @return Integer con el numero de filas de la zona
	 */
	public Integer getTxtFilasSentado() {
		try {
			return Integer.parseInt(txtFilas.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo limpiar los JTextField
	 */
	public void limpiarTxtZonaSentado() {
		txtFilas.setText("");
		txtColumnas.setText("");
		txtNombreSentado.setText("");
	}
}

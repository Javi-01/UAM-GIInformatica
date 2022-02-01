package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCrearZonaPie para crear zonas de pie
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaPie extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7685333591836863237L;

	private JLabel lblCrearPie = new JLabel("Nombre zona Pie: ");
	private JTextField txtNombrePie = new JTextField(12);

	private JLabel lblAforoPie = new JLabel("Aforo de la zona");
	private JTextField txtAforoPie = new JTextField(12);

	private JButton btAceptar = new JButton("Aceptar");

	private GridLayout layoutPie = new GridLayout(7, 2, 0, 30);

	/**
	 * Constructor de CardCrearZonaPie
	 */
	public CardCrearZonaPie() {
		setLayout(layoutPie);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir componentes al card
	 */
	private void anadirComponentes() {

		add(lblCrearPie);
		add(txtNombrePie);
		add(lblAforoPie);
		add(txtAforoPie);
		add(new JLabel(""));
		add(new JLabel(""));
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
	public String getTxtNombreZonaPie() {
		return txtNombrePie.getText();
	}

	/**
	 * Metodo para obtener el aforo de la zona indicado en el JTextField
	 * 
	 * @return Integer con el aforo de la zona
	 */
	public Integer getTxtAforoZona() {
		try {
			return Integer.parseInt(txtAforoPie.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo limpiar los JTextField
	 */
	public void limpiarTxtZonaPie() {
		txtAforoPie.setText("");
		txtNombrePie.setText("");
	}
}

package vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCrearZonaCompuestaComponentes obtener las componentes de la zona
 * compuesta a crear
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaCompuestaComponentes extends JPanel {

	private static final long serialVersionUID = -9216179332740182787L;

	private JLabel lblCrearCompuesta = new JLabel("Nombre zona Compuesta: ");
	private JTextField txtNombreCompuesta = new JTextField(12);

	private JLabel lblAnadirZona = new JLabel("A�ade zonas: ");
	private JButton btAnadirZona = new JButton("Buscar");
	private GridLayout layoutCompuesta = new GridLayout(2, 3, 20, 60);

	/**
	 * Constructor de CardCrearZonaCompuestaComponentes
	 */
	public CardCrearZonaCompuestaComponentes() {
		setLayout(layoutCompuesta);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir componentes al card
	 */
	private void anadirComponentes() {
		add(lblCrearCompuesta);
		add(txtNombreCompuesta);
		add(lblAnadirZona);
		add(btAnadirZona);
	}

	/**
	 * Metodo para establecer el controlador del boton de anadir zona
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControlador(ActionListener e) {
		btAnadirZona.addActionListener(e);
	}

	/**
	 * Metodo para obtener el nombre de la zona indicado en el JTextField
	 * 
	 * @return String con el nombre de la zona
	 */
	public String getTxtNombreCompuesta() {
		return txtNombreCompuesta.getText();
	}

	/**
	 * Metodo limpiar el JTextField
	 */
	public void limpiarTxtZonaCompuesta() {
		txtNombreCompuesta.setText("");
	}
}

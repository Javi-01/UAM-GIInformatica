package vista.panelAdministrador.pestanaCrearAbonos.anual;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controlador.abonos.ControladorCrearAbonoAnual;
import modelo.sistema.Sistema;

/**
 * Clase PestanaCardCrearAnual para la pestana necesaria para crear abonos
 * anuales
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCardCrearAnual extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4618394843982743533L;

	private GridLayout layoutAbonoAnual = new GridLayout(8, 2, 10, 25);

	private JLabel lblNombreAbono = new JLabel("Abono Anual " + Sistema.getInstance().getSistemaAbonos().size());
	private JLabel lblPrecioAbono = new JLabel("Precio del Abono: ");
	private JLabel lblZonaAbono = new JLabel("Zona del Abono: ");

	private JTextField txtPrecioAbono = new JTextField(10);
	private JTextField txtZonaAbono = new JTextField(10);

	private JButton btCrearAbono = new JButton("Crear");

	/**
	 * Constructor de PestanaCardCrearAnual
	 */
	public PestanaCardCrearAnual() {
		setLayout(layoutAbonoAnual);
		anadirComponentes();

		ControladorCrearAbonoAnual cCrearAnual = new ControladorCrearAbonoAnual(this);
		this.setControlador(cCrearAnual);
	}

	/**
	 * Metodo para anadir los componentes del Layout de abono anual
	 */
	private void anadirComponentes() {

		add(lblNombreAbono);
		add(new JLabel(""));
		add(lblPrecioAbono);
		add(txtPrecioAbono);
		add(lblZonaAbono);
		add(txtZonaAbono);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btCrearAbono);

	}

	/**
	 * Metodo para establecer el controlador del botton de crear abono
	 * 
	 * @param e ActionListener con el controlador a anadir
	 */
	public void setControlador(ActionListener e) {
		btCrearAbono.addActionListener(e);
	}

	/**
	 * Metodo para recoger el texto introducido del precio del abono
	 * 
	 * @return double con el precio del abono
	 */
	public double getTextPrecioAbono() {
		try {
			return Double.parseDouble(txtPrecioAbono.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0.0;
	}

	/**
	 * Metodo para recoger el texto introducido de la zona del abono
	 * 
	 * @return String con la zona del abono
	 */
	public String getTextZonaAbono() {
		return txtZonaAbono.getText();
	}

	/**
	 * Metodo para limpiar los campos de los JtextField
	 */
	public void limpiarCampos() {
		txtPrecioAbono.setText("");
		txtZonaAbono.setText("");
	}

}

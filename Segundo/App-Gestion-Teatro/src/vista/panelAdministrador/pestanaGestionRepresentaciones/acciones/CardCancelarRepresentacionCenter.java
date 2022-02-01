package vista.panelAdministrador.pestanaGestionRepresentaciones.acciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCancelarRepresentacionCenter para el card para cancelar una
 * representacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCancelarRepresentacionCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7803653226235248520L;

	private JLabel lblEventoRepre = new JLabel("Seleccione el evento: ");
	private JTextField txtEventoAsociado = new JTextField(10);

	private JLabel lblRepre = new JLabel("Seleccione la representacion: ");
	private JTextField txtRepreAsociada = new JTextField(10);

	private JButton btCancelarRepre = new JButton("Confirmar");
	private JButton btEventoAsociado = new JButton("Buscar evento");
	private JButton btRepreAsociado = new JButton("Buscar representacion");

	private GridLayout layoutAnadir = new GridLayout(7, 2, 30, 30);

	/**
	 * Constructor de CardCancelarRepresentacionCenter
	 */
	public CardCancelarRepresentacionCenter() {

		setLayout(layoutAnadir);
		txtEventoAsociado.setEditable(false);
		txtRepreAsociada.setEditable(false);
		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes al card
	 */
	private void anadirComponentes() {

		add(lblEventoRepre);
		add(txtEventoAsociado);
		add(btEventoAsociado);
		add(new JLabel(""));
		add(lblRepre);
		add(txtRepreAsociada);
		add(btRepreAsociado);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btCancelarRepre);
	}

	/**
	 * Metodo para establecer el controlador del boton para buscar evento
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorEvento(ActionListener e) {
		btEventoAsociado.addActionListener(e);
	}

	/**
	 * Metodo para establecer el controlador del boton confirmar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorRepre(ActionListener e) {
		btRepreAsociado.addActionListener(e);
	}

	/**
	 * Metodo para establecer el controlador del boton para cancelar la
	 * representacion
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorCrancelar(ActionListener e) {
		btCancelarRepre.addActionListener(e);
	}

	/**
	 * Metodo para establecer el nombre a buscar del evento
	 * 
	 * @param nombre String con el nombre a buscar del evento
	 */
	public void setEventoAsociado(String nombre) {
		txtEventoAsociado.setText(nombre);
	}

	/**
	 * Metodo para establecer el nombre a buscar de la representacion
	 * 
	 * @param nombre String con el nombre a buscar de la representacion
	 */
	public void setRepreAsociado(String nombre) {
		txtRepreAsociada.setText(nombre);
	}

	/**
	 * Metodo para limpiar el textField de Evento
	 */
	public void limpiarTxtEventoAsocido() {
		txtEventoAsociado.setText("");
	}

	/**
	 * Metodo para limpiar el textField de Representacion
	 */
	public void limpiarTxtRepreAsocido() {
		txtRepreAsociada.setText("");
	}
}

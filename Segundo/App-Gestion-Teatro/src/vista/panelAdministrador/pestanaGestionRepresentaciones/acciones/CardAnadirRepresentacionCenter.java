package vista.panelAdministrador.pestanaGestionRepresentaciones.acciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 * Clase CardAnadirRepresentacionCenter para el card para anadir una
 * representacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardAnadirRepresentacionCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1427884208869255293L;

	private JLabel lblEventoRepre = new JLabel("Seleccione el evento: ");
	private JTextField txtEventoAsociado = new JTextField(10);

	private JLabel lblFechaRepre = new JLabel("Introduce fecha (yyyy-MM-dd hh:mm): ");

	private JFormattedTextField txtFechaAsociada;

	private JButton btAnadirRepre = new JButton("Confirmar");
	private JButton btEventoAsociado = new JButton("Buscar evento");

	private GridLayout layoutAnadir = new GridLayout(7, 2, 30, 30);

	/**
	 * Constructor de CardAnadirRepresentacionCenter
	 */
	public CardAnadirRepresentacionCenter() {
		setLayout(layoutAnadir);
		txtEventoAsociado.setEditable(false);
		txtFechaAsociada = new JFormattedTextField(createFormatter("####-##-## ##:##"));
		anadirComponentes();
	}

	/**
	 * Metodo privado para crear un formater para el formato de la fecha
	 * 
	 * @param string String con la cadena a formatear
	 * 
	 * @return MaskFormatter con el formato
	 */
	private MaskFormatter createFormatter(String string) {

		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(string);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
		}
		return formatter;
	}

	/**
	 * Metodo para anadir los componentes al card
	 */
	private void anadirComponentes() {

		add(lblEventoRepre);
		add(txtEventoAsociado);
		add(btEventoAsociado);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(lblFechaRepre);
		add(new JLabel(""));
		add(txtFechaAsociada);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btAnadirRepre);
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
	 * Metodo para establecer el controlador del boton para anadir representacion
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorCrear(ActionListener e) {
		btAnadirRepre.addActionListener(e);
	}

	/**
	 * Metodo para establecer el evento a buscar
	 * 
	 * @param nombre String con el nombre del evento a buscar
	 */
	public void setEventoAsociado(String nombre) {
		txtEventoAsociado.setText(nombre);
	}

	/**
	 * Metodo para obtener la fecha indicada en el textField
	 * 
	 * @return JFormattedTextField con la fecha indicada
	 */
	public JFormattedTextField getFechaAsociada() {
		return txtFechaAsociada;
	}

	/**
	 * Metodo para limpìar el textField
	 */
	public void limpiarEventoAsociado() {
		txtEventoAsociado.setText("");
	}
}

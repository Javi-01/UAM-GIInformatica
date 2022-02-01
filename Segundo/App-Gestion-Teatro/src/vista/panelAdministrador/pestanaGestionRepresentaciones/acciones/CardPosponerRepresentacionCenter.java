package vista.panelAdministrador.pestanaGestionRepresentaciones.acciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 * Clase CardPosponerRepresentacionCenter para el card para posponer una
 * representacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardPosponerRepresentacionCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2032047292950231261L;

	private JLabel lblEventoRepre = new JLabel("Seleccione el evento: ");
	private JTextField txtEventoAsociado = new JTextField(10);

	private JLabel lblRepre = new JLabel("Seleccione la representacion: ");
	private JTextField txtRepreAsociada = new JTextField(10);

	private JLabel lblFechaRepre = new JLabel("Introduce fecha (yyyy-MM-dd HH:mm): ");
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String strFecha;
	private Date fecha = new Date();
	private JFormattedTextField txtFechaAsociada;

	private JButton btPosponerRepre = new JButton("Confirmar");
	private JButton btEventoAsociado = new JButton("Buscar evento");
	private JButton btRepreAsociado = new JButton("Buscar representacion");

	private GridLayout layoutAnadir = new GridLayout(7, 2, 30, 30);

	/**
	 * Constructor de CardPosponerRepresentacionCenter
	 */
	public CardPosponerRepresentacionCenter() {
		setLayout(layoutAnadir);
		txtEventoAsociado.setEditable(false);
		txtRepreAsociada.setEditable(false);

		txtFechaAsociada = new JFormattedTextField(createFormatter("####-##-## ##:##"));
		strFecha = formatoFecha.format(fecha);
		txtFechaAsociada.setText(strFecha);
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
		add(lblRepre);
		add(txtRepreAsociada);
		add(btRepreAsociado);
		add(new JLabel(""));
		add(lblFechaRepre);
		add(new JLabel(""));
		add(txtFechaAsociada);
		add(new JLabel(""));
		add(new JLabel(""));
		add(btPosponerRepre);
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
	 * Metodo para establecer el controlador del boton de buscar representacion
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorRepre(ActionListener e) {
		btRepreAsociado.addActionListener(e);
	}

	/**
	 * Metodo para establecer el controlador del boton para posponer representacion
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorPosponer(ActionListener e) {
		btPosponerRepre.addActionListener(e);
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
	 * Metodo para establecer la representacion a buscar
	 * 
	 * @param nombre String con el nombre de la representacion a buscar
	 */
	public void setRepreAsociado(String nombre) {
		txtRepreAsociada.setText(nombre);
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

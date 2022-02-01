package vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.tipo;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCrearEventoDanzaCenter para el Card central de crear el evento de
 * tipo danza
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearEventoDanzaCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5662009298910741392L;

	private GridLayout layoutDanza = new GridLayout(9, 2, 8, 20);

	private JLabel lblTituloEvento = new JLabel("Nombre del Evento: ");
	private JLabel lblDuracion = new JLabel("Duracion del Evento: ");
	private JLabel lblDescripcion = new JLabel("Descripcion: ");
	private JLabel lblAutor = new JLabel("Autor del Evento: ");
	private JLabel lblDirector = new JLabel("Director del Evento: ");
	private JLabel lblDanzaOrquesta = new JLabel("Orquesta de Danza: ");
	private JLabel lblDirectorOrquesta = new JLabel("Director de orquesta: ");
	private JLabel lblBailarines = new JLabel("Bailarines de la Danza: ");

	private JTextField txtTituloEvento = new JTextField(10);
	private JTextField txtDuracion = new JTextField(10);
	private JTextField txtDescripcion = new JTextField(10);
	private JTextField txtAutor = new JTextField(10);
	private JTextField txtDirector = new JTextField(10);
	private JTextField txtDanzaOrquesta = new JTextField(10);
	private JTextField txtDirectorOrquesta = new JTextField(10);
	private JTextField txtBailarines = new JTextField(10);

	private JButton botonCrearEvento = new JButton("Siguiente");

	/**
	 * Constructor de CardCrearEventoDanzaCenter
	 */
	public CardCrearEventoDanzaCenter() {

		setLayout(layoutDanza);
		anadirComponentesDanza();

	}

	/**
	 * Metodo para anadir los componentes al Card
	 */
	public void anadirComponentesDanza() {

		add(lblTituloEvento);
		add(txtTituloEvento);

		add(lblDuracion);
		add(txtDuracion);

		add(lblDescripcion);
		add(txtDescripcion);

		add(lblAutor);
		add(txtAutor);

		add(lblDirector);
		add(txtDirector);

		add(lblDanzaOrquesta);
		add(txtDanzaOrquesta);

		add(lblDirectorOrquesta);
		add(txtDirectorOrquesta);

		add(lblBailarines);
		add(txtBailarines);

		add(new JLabel(""));
		add(botonCrearEvento);
	}

	/**
	 * Metodo para obtener el boton de crear evento de tipo danza
	 * 
	 * @return JButton con el boton
	 */
	public JButton getBtCrearEventoDanza() {
		return this.botonCrearEvento;
	}

	/**
	 * Metodo para obtener el titulo introducido de danza
	 * 
	 * @return String con el titulo
	 */
	public String getTituloEventoDanza() {
		return txtTituloEvento.getText();
	}

	/**
	 * Metodo para obtener la duracion introducida de danza
	 * 
	 * @return double con la duracion
	 */
	public double getDuracionDanza() {

		try {
			return Double.parseDouble(txtDuracion.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo para obtener la descripcion introducida de la danza
	 * 
	 * @return String con la descripcion
	 */
	public String getDescripcionDanza() {
		return txtDescripcion.getText();
	}

	/**
	 * Metodo para obtener el autor introducido de la danza
	 * 
	 * @return String con el autor
	 */
	public String getAutorDanza() {
		return txtAutor.getText();
	}

	/**
	 * Metodo para obtener el director introducido de la danza
	 * 
	 * @return String con el director
	 */
	public String getDirectorDanza() {
		return txtDirector.getText();
	}

	/**
	 * Metodo para obtener la orquesta introducida de la danza
	 * 
	 * @return String con la orquesta
	 */
	public String getDanzaOrquesta() {
		return txtDanzaOrquesta.getText();
	}

	/**
	 * Metodo para obtener el director de la orquesta introducido de la danza
	 * 
	 * @return String con el director de la orquesta
	 */
	public String getDirectorOrquesta() {
		return txtDirectorOrquesta.getText();
	}

	/**
	 * Metodo para obtener los bailarines introducidos de la danza
	 * 
	 * @return String con los bailarines
	 */
	public String getBailarinesDanza() {
		return txtBailarines.getText();
	}

	/**
	 * Metodo para establecer el controlador del boton de crear de danza
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		botonCrearEvento.addActionListener(c);
	}

	/**
	 * Metodo para limpiar todos los campos de danza
	 */
	public void limpiarCardCrearEventoDanza() {
		txtTituloEvento.setText("");
		txtDuracion.setText("");
		txtDirector.setText("");
		txtDescripcion.setText("");
		txtAutor.setText("");
		txtBailarines.setText("");
		txtDanzaOrquesta.setText("");
		txtDirectorOrquesta.setText("");
	}
}

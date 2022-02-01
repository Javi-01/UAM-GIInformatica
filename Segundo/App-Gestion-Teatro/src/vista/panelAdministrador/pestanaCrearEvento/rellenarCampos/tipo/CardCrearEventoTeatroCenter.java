package vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.tipo;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCrearEventoTeatroCenter para el Card central de crear el evento de
 * tipo teatro
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearEventoTeatroCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5662009298910741392L;

	private GridLayout layoutTeatro = new GridLayout(7, 2, 8, 40);

	private JLabel lblTituloEvento = new JLabel("Nombre del Evento: ");
	private JLabel lblDuracion = new JLabel("Duracion del Evento: ");
	private JLabel lblDescripcion = new JLabel("Descripcion: ");
	private JLabel lblAutor = new JLabel("Autor del Evento: ");
	private JLabel lblDirector = new JLabel("Director del Evento: ");
	private JLabel lblActores = new JLabel("Actores del Teatro: ");

	private JTextField txtTituloEvento = new JTextField(10);
	private JTextField txtDuracion = new JTextField(10);
	private JTextField txtDescripcion = new JTextField(10);
	private JTextField txtAutor = new JTextField(10);
	private JTextField txtDirector = new JTextField(10);
	private JTextField txtActores = new JTextField(10);

	private JButton botonCrearEvento = new JButton("Siguiente");

	/**
	 * Constructor de CardCrearEventoTeatroCenter
	 */
	public CardCrearEventoTeatroCenter() {

		setLayout(layoutTeatro);
		anadirComponentesTeatro();

	}

	/**
	 * Metodo para anadir los componentes al Card
	 */
	public void anadirComponentesTeatro() {

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

		add(lblActores);
		add(txtActores);

		add(new JLabel(""));
		add(botonCrearEvento);
	}

	/**
	 * Metodo para obtener el boton de crear evento de tipo teatro
	 * 
	 * @return JButton con el boton
	 */
	public JButton getBtCrearEventoTeatro() {
		return this.botonCrearEvento;
	}

	/**
	 * Metodo para obtener el titulo introducido del teatro
	 * 
	 * @return String con el titulo
	 */
	public String getTituloEventoTeatro() {
		return txtTituloEvento.getText();
	}

	/**
	 * Metodo para obtener la duracion introducida del teatro
	 * 
	 * @return double con la duracion
	 */
	public double getDuracionTeatro() {

		try {
			return Double.parseDouble(txtDuracion.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo para obtener la descripcion introducida del teatro
	 * 
	 * @return String con la descripcion
	 */
	public String getDescripcionTeatro() {
		return txtDescripcion.getText();
	}

	/**
	 * Metodo para obtener el autor introducido del teatro
	 * 
	 * @return String con el autor
	 */
	public String getAutorTeatro() {
		return txtAutor.getText();
	}

	/**
	 * Metodo para obtener el director introducido del teatro
	 * 
	 * @return String con el director
	 */
	public String getDirectorTeatro() {
		return txtDirector.getText();
	}

	/**
	 * Metodo para obtener los actores introducida del teatro
	 * 
	 * @return String con los actores
	 */
	public String getActoresTeatro() {
		return txtActores.getText();
	}

	/**
	 * Metodo para establecer el controlador del boton de crear del teatro
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		botonCrearEvento.addActionListener(c);
	}

	/**
	 * Metodo para limpiar todos los campos del teatro
	 */
	public void limpiarCardCrearEventoTeatro() {
		txtTituloEvento.setText("");
		txtDuracion.setText("");
		txtDirector.setText("");
		txtDescripcion.setText("");
		txtAutor.setText("");
		txtActores.setText("");
	}
}

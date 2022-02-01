package vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.tipo;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardCrearEventoConciertoCenter para el Card central de crear el evento
 * de tipo concierto
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearEventoConciertoCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7001918501069253598L;

	private GridLayout layoutDanza = new GridLayout(9, 2, 8, 20);

	private JLabel lblTituloEvento = new JLabel("Nombre del Evento: ");
	private JLabel lblDuracion = new JLabel("Duracion del Evento: ");
	private JLabel lblDescripcion = new JLabel("Descripcion: ");
	private JLabel lblAutor = new JLabel("Autor del Evento: ");
	private JLabel lblDirector = new JLabel("Director del Evento: ");
	private JLabel lblOrquestaConcierto = new JLabel("Orquesta del Concierto: ");
	private JLabel lblSolista = new JLabel("Solista del concierto: ");
	private JLabel lblPieza = new JLabel("Pieza del concierto: ");

	private JTextField txtTituloEvento = new JTextField(10);
	private JTextField txtDuracion = new JTextField(10);
	private JTextField txtDescripcion = new JTextField(10);
	private JTextField txtAutor = new JTextField(10);
	private JTextField txtDirector = new JTextField(10);
	private JTextField txtOrquestaConcierto = new JTextField(10);
	private JTextField txtSolista = new JTextField(10);
	private JTextField txtPieza = new JTextField(10);

	private JButton botonCrearEvento = new JButton("Siguiente");

	/**
	 * Constructor de CardCrearEventoConciertoCenter
	 */
	public CardCrearEventoConciertoCenter() {

		setLayout(layoutDanza);
		anadirComponentesConcierto();

	}

	/**
	 * Metodo para anadir los componentes al Card
	 */
	public void anadirComponentesConcierto() {

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

		add(lblOrquestaConcierto);
		add(txtOrquestaConcierto);

		add(lblSolista);
		add(txtSolista);

		add(lblPieza);
		add(txtPieza);

		add(new JLabel(""));
		add(botonCrearEvento);
	}

	/**
	 * Metodo para obtener el boton de crear evento de tipo concierto
	 * 
	 * @return JButton con el boton
	 */
	public JButton getBtCrearEventoConcierto() {
		return this.botonCrearEvento;
	}

	/**
	 * Metodo para obtener el titulo introducido del concierto
	 * 
	 * @return String con el titulo
	 */
	public String getTituloEventoConcierto() {
		return txtTituloEvento.getText();
	}

	/**
	 * Metodo para obtener la duracion introducida del concierto
	 * 
	 * @return double con la duracion
	 */
	public double getDuracionConcierto() {

		try {
			return Double.parseDouble(txtDuracion.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo para obtener la descripcion introducida del concierto
	 * 
	 * @return String con la descripcion
	 */
	public String getDescripcionConcierto() {
		return txtDescripcion.getText();
	}

	/**
	 * Metodo para obtener el autor introducido del concierto
	 * 
	 * @return String con el autor
	 */
	public String getAutorConcierto() {
		return txtAutor.getText();
	}

	/**
	 * Metodo para obtener el director introducido del concierto
	 * 
	 * @return String con el director
	 */
	public String getDirectorConcierto() {
		return txtDirector.getText();
	}

	/**
	 * Metodo para obtener la orquesta introducida del concierto
	 * 
	 * @return String con la orquesta
	 */
	public String getOrquestaConcierto() {
		return txtOrquestaConcierto.getText();
	}

	/**
	 * Metodo para obtener el solista de la orquesta introducido del concierto
	 * 
	 * @return String con el solista de la orquesta
	 */
	public String getSolistaOrquesta() {
		return txtSolista.getText();
	}

	/**
	 * Metodo para obtener la pieza introducido del concierto
	 * 
	 * @return String con la pieza
	 */
	public String getPiezaConcierto() {
		return txtPieza.getText();
	}

	/**
	 * Metodo para establecer el controlador del boton de crear de concierto
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		botonCrearEvento.addActionListener(c);
	}

	/**
	 * Metodo para limpiar todos los campos de concierto
	 */
	public void limpiarCardCrearEventoConcierto() {
		txtTituloEvento.setText("");
		txtDuracion.setText("");
		txtDirector.setText("");
		txtDescripcion.setText("");
		txtAutor.setText("");
		txtOrquestaConcierto.setText("");
		txtPieza.setText("");
		txtSolista.setText("");
	}

}

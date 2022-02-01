package vista.panelAdministrador.pestanaZonasYAforo.acciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase CardReducirAforoEventos para el card para reducir el aforo de eventos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardReducirAforoEventos extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3967451818187520052L;

	private JLabel lblEventoAsociado = new JLabel("Evento a reducir su aforo: ");
	private JTextField txtEventoAsociado = new JTextField(10);
	private JButton btEventoAsociado = new JButton("Buscar");

	private JLabel lblPorcentaje = new JLabel("Indice un porcentaje de reduccion de aforo(%): ");
	private JTextField txtPorcentaje = new JTextField(10);

	private JButton btAceptar = new JButton("Confirmar");
	private GridLayout layoutReducir = new GridLayout(7, 2, 25, 25);

	/**
	 * Constructor de CardReducirAforoEventos
	 */
	public CardReducirAforoEventos() {

		setLayout(layoutReducir);
		txtEventoAsociado.setEditable(false);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes al card
	 */
	private void anadirComponentes() {

		add(lblEventoAsociado);
		add(txtEventoAsociado);
		add(btEventoAsociado);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(lblPorcentaje);
		add(txtPorcentaje);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btAceptar);
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
	 * Metodo para establecer el controlador del boton aceptar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorAceptar(ActionListener e) {
		btAceptar.addActionListener(e);
	}

	/**
	 * Metodo para obtener el porcentage indicado en el textField
	 * 
	 * @return Integer con el porcentag indicado
	 */
	public Integer getPorcentajeEvento() {
		try {
			return Integer.parseInt(txtPorcentaje.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	/**
	 * Metodo para establecer el evento a buscar
	 * 
	 * @param evento String con el nombre del evento a buscar
	 */
	public void setTxtEventoAforo(String evento) {
		txtEventoAsociado.setText(evento);
	}

	/**
	 * Metodo para obtener el evento indicado en el textField
	 * 
	 * @return String con el evento indicado
	 */
	public String getTxtEventoAsociado() {
		return txtEventoAsociado.getText();
	}

	/**
	 * Metodo para limpiar los textField
	 */
	public void limpiarCamposAforo() {
		txtEventoAsociado.setText("");
		txtPorcentaje.setText("");
	}

}

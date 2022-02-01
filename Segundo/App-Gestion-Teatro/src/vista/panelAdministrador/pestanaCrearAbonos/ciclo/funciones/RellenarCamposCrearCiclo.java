package vista.panelAdministrador.pestanaCrearAbonos.ciclo.funciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import modelo.abono.Ciclo;
import modelo.evento.Evento;

/**
 * Clase RellenarCamposCrearCiclo para rellenar la informacion del ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class RellenarCamposCrearCiclo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6942993899227340571L;

	private ArrayList<Ciclo> ciclosACiclo = new ArrayList<>();
	private ArrayList<Evento> eventosACiclo = new ArrayList<>();

	GridLayout layoutCrearCiclo = new GridLayout(4, 2, 15, 2);

	private JLabel lblNombreCiclo = new JLabel("Nombre Ciclo: ");
	private JLabel lblPrecioCiclo = new JLabel("Porcentaje de reduccion (Precio): ");
	private JLabel lblZonaCiclo = new JLabel("Zona del Ciclo: ");

	private JTextField txtNombreCiclo = new JTextField(10);
	private JTextField txtPrecioCiclo = new JTextField(10);
	private JTextField txtZonaCiclo = new JTextField(10);

	private JButton btAnadeCiclos = new JButton("Anadir un Ciclo");
	private JButton btAnadeEventos = new JButton("Anadir un Evento");

	/**
	 * Constructor de RellenarCamposCrearCiclo
	 */
	public RellenarCamposCrearCiclo() {
		setLayout(layoutCrearCiclo);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes del Layout de crear ciclo
	 */
	private void anadirComponentes() {
		add(lblNombreCiclo);
		add(txtNombreCiclo);
		add(lblPrecioCiclo);
		add(txtPrecioCiclo);
		add(lblZonaCiclo);
		add(txtZonaCiclo);
		add(btAnadeEventos);
		add(btAnadeCiclos);
	}

	/**
	 * Metodo para establecer el controlador para el boton de anadir ciclos
	 * 
	 * @param e ActionListener con el controlador a anadir
	 */
	public void setControladorAnadeCiclo(ActionListener e) {
		btAnadeCiclos.addActionListener(e);
	}

	/**
	 * Metodo para establecer el controlador para el boton de anadir eventos
	 * 
	 * @param e ActionListener con el controlador a anadir
	 */
	public void setControladorAnadeEvento(ActionListener e) {
		btAnadeEventos.addActionListener(e);
	}

	/**
	 * Metodo para obtener el nombre del ciclo introducido
	 * 
	 * @return String con el nombre del ciclo introducido
	 */
	public String getNombreCiclo() {
		return txtNombreCiclo.getText();
	}

	/**
	 * Metodo para obtener el nombre de la zona introducida
	 * 
	 * @return String con el nombre de la zona del ciclo
	 */
	public String getZonaCiclo() {
		return txtZonaCiclo.getText();
	}

	/**
	 * Metodo para obtener el precio introducido del ciclo
	 * 
	 * @return double con el precio del ciclo introducido
	 */
	public double getPrecioCiclo() {

		try {
			return Double.parseDouble(txtPrecioCiclo.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0.0;
	}

	/**
	 * Metodo para obtener los ciclos a anadir
	 * 
	 * @return ArrayList de los ciclos a anadir
	 */
	public ArrayList<Ciclo> getCiclosAnadidosACiclo() {
		return ciclosACiclo;
	}

	/**
	 * Metodo para anadir un ciclo al ciclo a crear
	 * 
	 * @param c Ciclo a anadir
	 */
	public void anadirCicloACiclo(Ciclo c) {
		ciclosACiclo.add(c);
	}

	/**
	 * Metodo para obtener los eventos a anadir
	 * 
	 * @return ArrayList de los evemtos a anadir
	 */
	public ArrayList<Evento> getEventosAnadidosACiclo() {
		return eventosACiclo;
	}

	/**
	 * Metodo para anadir un evento al ciclo a crear
	 * 
	 * @param e Evento a anadir
	 */
	public void anadirEventoACiclo(Evento e) {
		eventosACiclo.add(e);
	}

	/**
	 * Metodo para destruir los elemntos del ciclo a crear
	 */
	public void destruirElementosEnCiclo() {
		eventosACiclo.removeAll(eventosACiclo);
		ciclosACiclo.removeAll(ciclosACiclo);
	}

	/**
	 * Metodo para limpiar los campos de los JTextField
	 */
	public void limpiarCamposCrearCiclo() {
		txtNombreCiclo.setText("");
		txtPrecioCiclo.setText("");
		txtZonaCiclo.setText("");
	}
}

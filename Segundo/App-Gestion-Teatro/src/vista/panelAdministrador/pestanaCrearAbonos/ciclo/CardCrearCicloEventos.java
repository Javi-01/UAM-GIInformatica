package vista.panelAdministrador.pestanaCrearAbonos.ciclo;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;

/**
 * Clase CardCrearCicloEventos para el card para crear un ciclo simple
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearCicloEventos extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5456664317533836317L;

	private static final String[] nombresTablaEvento = { "Titulo", "Duracion", "Mas Informaci�n" };
	private String[] registrosTablaEvento = new String[3];

	private DefaultTableModel modeloTablaEvento;
	private JTable tablaEventos;
	private PestanaCrearCicloVolver btVolverCiclo;

	/**
	 * Constructor de CardCrearCicloEventos
	 */
	public CardCrearCicloEventos() {

		setLayout(new GridLayout(2, 1));

		modeloTablaEvento = new DefaultTableModel(null, nombresTablaEvento) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaEventos = new JTable(modeloTablaEvento);
		add(new JScrollPane(tablaEventos));
		btVolverCiclo = new PestanaCrearCicloVolver();
		add(btVolverCiclo);
	}

	/**
	 * Metodo para actualizar las tablas que muestran los eventos del ciclo
	 * 
	 * @param eventosCoincidentes ArrayList de los eventos coincidentes
	 */
	public void actualizarTablaEventos(ArrayList<Evento> eventosCoincidentes) {
		for (Evento e : eventosCoincidentes) {
			registrosTablaEvento[0] = e.getEventoTitulo();
			registrosTablaEvento[1] = String.valueOf(e.getEventoDuracion());
			registrosTablaEvento[2] = e.getEventoDescripcion();

			modeloTablaEvento.addRow(registrosTablaEvento);
		}
	}

	/**
	 * Metodo para destruir la tabla de eventos
	 */
	public void destruirTablaEventos() {

		int contRow = modeloTablaEvento.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEvento.removeRow(0);
		}
	}

	/**
	 * Metodo para establecer el controlador de la tabla de eventos
	 * 
	 * @param m MouseListener con el controlador a anadir
	 */
	public void setControlador(MouseListener m) {
		tablaEventos.addMouseListener(m);
	}

	/**
	 * Metodo para obtener el modelo de tabla de eventos
	 * 
	 * @return DefaultTableModel con el modelo de tabla de eventos
	 */
	public DefaultTableModel getTablaModeloEvento() {
		return modeloTablaEvento;
	}

	/**
	 * Metodo para obtener la tabla de eventos
	 * 
	 * @return JTable con la tabla de eventos
	 */
	public JTable getTablaEvento() {
		return tablaEventos;
	}

	/**
	 * Metodo para obtener el boton de volver
	 * 
	 * @return PestanaCrearCicloVolver con el boton de volver
	 */
	public PestanaCrearCicloVolver getBtVolverAnadeCiclo() {
		return btVolverCiclo;
	}
}

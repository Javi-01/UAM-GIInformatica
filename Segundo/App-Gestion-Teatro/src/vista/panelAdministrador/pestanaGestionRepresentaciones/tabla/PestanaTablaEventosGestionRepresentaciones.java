package vista.panelAdministrador.pestanaGestionRepresentaciones.tabla;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;

/**
 * Clase PestanaTablaEventosGestionRepresentaciones para la pestana de la tabla
 * de eventos en gestion representaciones
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaTablaEventosGestionRepresentaciones extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332699737976230325L;
	private static final String[] nombresTablaEvento = { "Titulo", "Duracion", "Mas Informaci�n" };
	private String[] registrosTablaEvento = new String[3];

	private DefaultTableModel modeloTablaEvento;
	private JTable tablaEventos;

	/**
	 * Constructor de PestanaTablaEventosGestionRepresentaciones
	 */
	public PestanaTablaEventosGestionRepresentaciones() {

		TitledBorder centro = new TitledBorder("Vista de eventos");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));

		modeloTablaEvento = new DefaultTableModel(null, nombresTablaEvento) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaEventos = new JTable(modeloTablaEvento);
		add(new JScrollPane(tablaEventos));
	}

	/**
	 * Metodo para actualizar la tabla de eventos
	 * 
	 * @param eventosCoincidentes ArrayList con los eventos a actualizar
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
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaEventos.addMouseListener(m);
	}

	/**
	 * Metodo para obtener la tabla de eventos
	 * 
	 * @return JTable con la tabla de eventos
	 */
	public JTable getTablaRepres() {
		return tablaEventos;
	}

	/**
	 * Metodo para obtener el modelo de la tabla de eventos
	 * 
	 * @return DefaultTableModel con el modelo de la tabla de eventos
	 */
	public DefaultTableModel getTablaModeloRepres() {
		return modeloTablaEvento;
	}
}

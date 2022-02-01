package vista.panelPrincipal.pestanaBuscarEvento;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;

/**
 * Clase PestanaBuscarEventoCenter para la busqueda de eventos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaBuscarEventoCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6781365079816003830L;

	private static final String[] nombresTablaEvento = { "Titulo", "Duracion", "Mas Informaci�n" };
	private String[] registrosTablaEvento = new String[3];

	private DefaultTableModel modeloTablaEvento;
	private JTable tablaEventos;

	/**
	 * Constructor de PestanaBuscarEventoCenter
	 */
	public PestanaBuscarEventoCenter() {

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
	 * @param eventosCoincidentes ArrayList de los eventos
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
	 * Metodo para destruir la tabla
	 */
	public void destruirTablaEventos() {

		int contRow = modeloTablaEvento.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEvento.removeRow(0);
		}
	}
}

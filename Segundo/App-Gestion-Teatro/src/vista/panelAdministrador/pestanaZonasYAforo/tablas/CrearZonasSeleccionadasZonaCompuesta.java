package vista.panelAdministrador.pestanaZonasYAforo.tablas;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.zona.Zona;

/**
 * Clase CrearZonasSeleccionadasZonaCompuesta para gestionar las zonas
 * seleecionadas de la creacion de las zonas compuestas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CrearZonasSeleccionadasZonaCompuesta extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6586759031930588036L;

	private static final String[] nombresTablaZonas = { "Zonas seleccionadas" };
	private String[] registrosTablaZonas = new String[1];

	private DefaultTableModel modeloTablaCrearZonas;
	private JTable tablaZonas;

	/**
	 * Constructor de CrearZonasSeleccionadasZonaCompuesta
	 */
	public CrearZonasSeleccionadasZonaCompuesta() {

		setLayout(new GridLayout(1, 1));

		modeloTablaCrearZonas = new DefaultTableModel(null, nombresTablaZonas) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaZonas = new JTable(modeloTablaCrearZonas);
		add(new JScrollPane(tablaZonas));
	}

	/**
	 * Metodo para actualizar la tablade las zonas
	 * 
	 * @param zonas ArrayList de las zonas a actualizar
	 */
	public void actualizarTablaZonas(ArrayList<Zona> zonas) {

		for (Zona z : zonas) {
			registrosTablaZonas[0] = z.getZonaNombre();

			modeloTablaCrearZonas.addRow(registrosTablaZonas);
		}
	}

	/**
	 * Metodo para destruir la tabla
	 */
	public void destruirTablaZonaCompuesta() {

		int contRow = modeloTablaCrearZonas.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaCrearZonas.removeRow(0);
		}
	}

	/**
	 * Metodo para establecer el controlador la tabla de zonas
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaZonas.addMouseListener(m);
	}
}

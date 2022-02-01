package vista.panelAdministrador.pestanaZonasYAforo.tablas;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.zona.Zona;

/**
 * Clase PestanaZonasAforoTablaZonas para las tablas del aforo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaZonasAforoTablaZonas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -670421268608682734L;

	private static final String[] nombresTablaZonas = { "Zona", "Tipo de Zona" };
	private String[] registrosTablaZonas = new String[2];

	private DefaultTableModel modeloTablaZona;
	private JTable tablaZonas;

	/**
	 * Constructor de PestanaZonasAforoTablaZonas
	 */
	public PestanaZonasAforoTablaZonas() {

		TitledBorder centro = new TitledBorder("Selecciona la zona del Teatro para la representacion ");
		setBorder(centro);
		setLayout(new BorderLayout(1, 1));

		modeloTablaZona = new DefaultTableModel(null, nombresTablaZonas) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaZonas = new JTable(modeloTablaZona);
		add(new JScrollPane(tablaZonas));

	}

	/**
	 * Metodo para obtener el modelo de las tablas
	 * 
	 * @return DefaultTableModel con el modelo de las tablas
	 */
	public DefaultTableModel getTablaModelo() {
		return modeloTablaZona;
	}

	/**
	 * Metodo para obtener las tablas
	 * 
	 * @return JTable con las tablas
	 */
	public JTable getTablaZonas() {
		return tablaZonas;
	}

	/**
	 * Metodo para establecer el controlador la tabla de zonas
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaZonas.addMouseListener(m);
	}

	/**
	 * Metodo para actualizar la tabla de las zonas
	 * 
	 * @param zonas ArrayList de las zonas a actualizar
	 */
	public void actualizarTablaZonas(ArrayList<Zona> zonas) {

		for (Zona z : zonas) {

			registrosTablaZonas[0] = z.getZonaNombre();
			registrosTablaZonas[1] = "sentado";

			modeloTablaZona.addRow(registrosTablaZonas);

		}
	}

	/**
	 * Metodo para destruir la tabla
	 */
	public void destruirTablaZonas() {

		int contRow = modeloTablaZona.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaZona.removeRow(0);
		}
	}
}

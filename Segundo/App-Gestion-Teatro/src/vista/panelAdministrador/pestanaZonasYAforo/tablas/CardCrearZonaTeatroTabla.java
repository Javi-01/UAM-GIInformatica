package vista.panelAdministrador.pestanaZonasYAforo.tablas;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.zona.Zona;
import modelo.zona.ZonaPie;
import modelo.zona.ZonaSentado;

/**
 * Clase CardCrearZonaTeatroTabla para crear la tabla de las zonas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearZonaTeatroTabla extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3798158135988229879L;

	private static final String[] nombresTablaZonas = { "Nombre", "Tipo", "Aforo" };
	private String[] registrosTablaZonas = new String[3];

	private DefaultTableModel modeloTablaZonas;
	private JTable tablaZonas;

	/**
	 * Constructor de CardCrearZonaTeatroTabla
	 */
	public CardCrearZonaTeatroTabla() {

		setLayout(new GridLayout(1, 1));

		modeloTablaZonas = new DefaultTableModel(null, nombresTablaZonas) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaZonas = new JTable(modeloTablaZonas);
		add(new JScrollPane(tablaZonas));
	}

	/**
	 * Metodo para actualizar la tabla de las zonas
	 * 
	 * @param zonas ArrayList de las zonas a actualizar
	 */
	public void actualizarTablaZonas(ArrayList<Zona> zonas) {
		for (Zona z : zonas) {
			registrosTablaZonas[0] = z.getZonaNombre();
			if (z.getClass().equals(ZonaPie.class)) {
				registrosTablaZonas[1] = "Zona Pie";
			} else if (z.getClass().equals(ZonaSentado.class)) {
				registrosTablaZonas[1] = "Zona Sentado";
			} else {
				registrosTablaZonas[1] = "Zona Compuesta";
			}
			registrosTablaZonas[2] = String.valueOf(z.getZonaAforo());

			modeloTablaZonas.addRow(registrosTablaZonas);
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

	/**
	 * Metodo para obtener el modelo de las tablas
	 * 
	 * @return DefaultTableModel con el modelo de las tablas
	 */
	public DefaultTableModel getTablaModeloZonas() {
		return modeloTablaZonas;
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
	 * Metodo para destruir las tablas
	 */
	public void destruirTablaZonas() {

		int contRow = modeloTablaZonas.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaZonas.removeRow(0);
		}
	}
}
